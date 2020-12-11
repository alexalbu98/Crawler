package mta.Classes;
import mta.Exceptions.CouldNotReachSiteException;
import mta.Singletons.LogFile;
import mta.Singletons.ThreadPool;
import mta.Singletons.VisitedPageList;
import mta.Utilities.FileType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Integer.parseInt;


public class CrawlTask implements Runnable {

    private Page currentPage;
    private int currentDepth;
    private int maxDepth;
    private int delay;
    private boolean useRobots;
    private String downloadDirectory;
    private ArrayList<String>disallowed;
    private ArrayList<String>allowed;
    private LogFile logger;

    public CrawlTask(){}
    public CrawlTask(Page page, int currentDepth, int maxDepth, int delay, int logLevel, String logFile, String rootDir, boolean useRobots)
    {
        this.currentPage = page;
        this.currentDepth = currentDepth;
        this.maxDepth = maxDepth;
        this.downloadDirectory = rootDir;
        this.useRobots = useRobots;
        this.delay = delay;
        allowed = new ArrayList<>();
        disallowed = new ArrayList<>();
        logger = LogFile.getInstance(logFile, logLevel);
    }

    /**
     * This method returns the page content
     * @returns a byte array with content or null in case of failure
     * */
    private byte[] fetch() throws IOException, CouldNotReachSiteException {
            //checks first to see if the resource is allowed to be downloaded
            if(!resourceIsAllowed(currentPage.getURL().toString()))
            {
                throw new CouldNotReachSiteException("Could not reach site: "+currentPage.getURL().toString());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //Opening a connection
            HttpURLConnection conn = (HttpURLConnection) currentPage.getURL().openConnection();
            //Sending the request
            conn.setRequestMethod("GET");
            int response = conn.getResponseCode();
            //if response message is 200(success) otherwise error
            if (response == 200) {
                InputStream is = currentPage.getURL().openStream();
                byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                int n;
                while ( (n = is.read(byteChunk)) > 0 ) {
                    baos.write(byteChunk, 0, n);
                }
            }else
                {
                   throw new CouldNotReachSiteException("Response from site " + currentPage.getURL().toString()+ " was " +response);
                }
            return baos.toByteArray();
    }

    /**
     * This function will write the content to correct file
     * @param  content byte array containing the file content
     * */
    private void writeToFile(byte[] content)
    {
        String host = currentPage.getURL().getHost();
        String filename = currentPage.getURL().getFile();
        if(filename.equals("/") || filename.equals(""))
        {
            filename = "mainPage";
        }
        String[]tokens = downloadDirectory.split("/");
        //checks if the file belongs to the target site or not
        if(!tokens[tokens.length - 1].equals(host)) {
            downloadDirectory = downloadDirectory + "/" + host;
        }
        String fileLocation = downloadDirectory+"/"+filename+FileType.getFileExtension(new String(content));

        //creates the site directory for the first file to be saved
        File dir = new File(fileLocation);
        File parentDirs =  new File(dir.getParent());
        parentDirs.mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(fileLocation);
            fos.write(content);
            fos.close();
        }catch (Exception e)
        {
            logger.writToFile("Failed to save file " + fileLocation, "WARNING");
        }
    }

    /**
     * Method will return all links from content
     * @param content file content
     * @returns array list containing all the links on the page
     * */
    private ArrayList<Page>getLinks(String content) throws MalformedURLException {
        ArrayList<Page> linkList = new ArrayList<>();
        //regex that matches all links
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            Page page = new Page();
            page.addURL(new URL(m.group()));
            linkList.add(page);
        }
        return linkList;
    }

    /**
     * This method reads the robots txt file from site
     * */
    private void readRobotsTxt(){
        if(!useRobots) //use robots must be specified by the user
        {
            return;
        }
        String protocol = currentPage.getURL().getProtocol();
        String host = currentPage.getURL().getHost();
        String robotsTxtLink = protocol+"://"+host+"/"+"robots.txt";
        try
        {
            URL url = new URL(robotsTxtLink);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            ArrayList<String>lines = new ArrayList<>();
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if(line.equals(""))
                    found = false;
                if(found)
                    lines.add(line);
                if(line.equals("User-agent: *"))
                {
                    found = true;
                }
            }
            for(String row:lines)
            {
               String[]tokens =  row.split(":");
               if(tokens[0].equals("Disallow"))
               {
                    disallowed.add(tokens[1]);
               }
               if(tokens[0].equals("Allow"))
               {
                   disallowed.add(tokens[1]);
               }
               if(tokens[0].equals("Crawl-delay"))
               {
                    delay = parseInt(tokens[1]);
               }
            }

        } catch (Exception e) {
            allowed.clear();
            disallowed.clear();
        }
    }

    /**
     * This method tells if the crawler is allowed to download the resource or not.
     * */
    private boolean resourceIsAllowed(String fileName)
    {
        boolean result = true;
        for(String disallowedRule: disallowed) {
            Pattern r = Pattern.compile(disallowedRule);
            Matcher m = r.matcher(fileName);
            if(m.find()) //resource is not allowed
            {
                result = false;
                for(String allowedRule: allowed)
                {
                    r = Pattern.compile(allowedRule);
                    m = r.matcher(fileName);
                    if(m.find()) //resource is allowed
                    {
                        result = true;
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    @Override
    public void run() {
        VisitedPageList list = VisitedPageList.getInstance();
        ThreadPool pool = ThreadPool.getInstance();
        try
        {
            readRobotsTxt();
            Thread.sleep(delay); //applies the delay specified by the user or in robots.txt
            byte[] content = fetch();
            writeToFile(content);
            logger.writToFile("Successfully crawled page " + currentPage.getURL().toString(), "INFO");
            //adds current page to visited list
            list.addSites(currentPage);
            ArrayList<Page>links = getLinks(new String(content));
            //for each page the current page has a link to will be created a new task
            for(Page page: links) {
                //the next page must not be visited
                if(!list.pageAlreadyVisited(page) && (currentDepth+1<maxDepth)) {
                    CrawlTask newTask = new CrawlTask(page, currentDepth + 1, maxDepth, delay, logger.getLogLevel(), logger.getFileName(), downloadDirectory, useRobots);
                    pool.incrementActiveTasks();
                    pool.runTask(newTask);
                }
            }
        } catch (Exception e) {
            logger.writToFile(e.getMessage(), "ERROR");
        }finally {
            try {
                pool.decrementActiveTasks();
            } catch (InterruptedException e) {
                System.out.println("Unexpected error");
                pool.shutdownThreadPool();
                System.exit(-1);
            }
        }
    }
}
