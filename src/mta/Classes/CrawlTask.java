package mta.Classes;
import mta.Exceptions.CouldNotReachSiteException;
import mta.Singletons.ThreadPool;
import mta.Singletons.VisitedPageList;

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
    private String logFile;
    private String downloadDirectory;
    private ArrayList<String>disallowed;
    private ArrayList<String>allowed;

    public CrawlTask(){}
    public CrawlTask(Page page, int currentDepth, int maxDepth, int delay, String logFile, String rootDir, boolean useRobots)
    {
        this.currentPage = page;
        this.currentDepth = currentDepth;
        this.maxDepth = maxDepth;
        this.logFile = logFile;
        this.downloadDirectory = rootDir;
        this.useRobots = useRobots;
        this.delay = delay;
        allowed = new ArrayList<>();
        disallowed = new ArrayList<>();
    }

    /**
     * This method returns the page content
     * @returns a byte array with content or null in case of failure
     * */
    private byte[] fetch() throws IOException {
            //checks first to see if the resource is allowed to be downloaded
            if(!resourceIsAllowed(currentPage.getURL().toString()))
            {
                return null;
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
                    return null;
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
        //every host will have it`s own folder in the root directory
        String siteDirectory = downloadDirectory+"/"+host;
        String fileLocation = siteDirectory+"/"+filename;
        File dir = new File(siteDirectory);
        //creates the site directory for the first time
        if(!dir.exists()){
            boolean success = dir.mkdirs();
            if(!success)
            {
                System.out.println("Failed to save page " + currentPage.getURL().toString());
                return;
            }
        }
        dir = new File(fileLocation);
        File parentDirs =  new File(dir.getParent());
        parentDirs.mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(fileLocation);
            fos.write(content);
            fos.close();
        }catch (Exception e)
        {
            System.out.println("Failed to write content to file " + fileLocation);
        }
    }

    /**
     * Method will return all links from content
     * @param content file content
     * @returns array list of links
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
    private void readRobotsTxt() throws MalformedURLException {
        String protocol = currentPage.getURL().getProtocol();
        String host = currentPage.getURL().getHost();
        String link = protocol+"://"+host+"/"+"robots.txt";
        try
        {
            URL url = new URL(link);
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
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method tells if the crawler is allowed to download the resource or not.
     * */
    private boolean resourceIsAllowed(String fileName)
    {
        boolean result = true;
        for(String rule: disallowed) {
            Pattern r = Pattern.compile(rule);
            Matcher m = r.matcher(fileName);
            if(m.find())
            {
                result = false;
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
            if(list.pageAlreadyVisited(currentPage)){
                return;
            }
            if(currentDepth >= maxDepth)
            {
                return;
            }

            if(useRobots)
            {
                readRobotsTxt();
            }

            Thread.sleep(delay);
            byte[] content = fetch();
            if(content == null)
            {
                throw new CouldNotReachSiteException("Could not reach site: "+currentPage.getURL().toString());
            }
            writeToFile(content);
            System.out.printf("Crawling page %s\n", currentPage.getURL().toString());
            //adds current page to visited list
            list.addSites(currentPage);
            ArrayList<Page>links = getLinks(new String(content));
            //for each page the current page has a link to will be created a new task
            for(Page page: links) {
                //the next page must not be visited
                if(!list.pageAlreadyVisited(page)) {
                    CrawlTask newTask = new CrawlTask(page, currentDepth + 1, maxDepth, delay, logFile, downloadDirectory, useRobots);
                    pool.incrementActiveTasks();
                    pool.runTask(newTask);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                pool.decrementActiveTasks();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
