package mta.Classes;


import mta.Exceptions.MaxDepthReachedException;
import mta.Singletons.ThreadPool;
import mta.Singletons.VisitedPageList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlTask implements Runnable {

    private Page currentPage;
    private int currentDepth;
    private int maxDepth;
    private boolean useRobots;
    private String logFile;
    private String downloadDirectory;
    private ArrayList<String>disallowed = null;
    private ArrayList<String>siteMaps = null;

    public CrawlTask(){}
    public CrawlTask(Page page, int currentDepth, int maxDepth, String logFile, String rootDir, boolean useRobots)
    {
        this.currentPage = page;
        this.currentDepth = currentDepth;
        this.maxDepth = maxDepth;
        this.logFile = logFile;
        this.downloadDirectory = rootDir;
        this.useRobots = useRobots;
    }

    /**
     * This method returns the page content
     * */
    private String fetch() throws IOException {
            StringBuilder buffer = new StringBuilder();
            //Opening a connection
            HttpURLConnection conn = (HttpURLConnection) currentPage.getURL().openConnection();
            //Sending the request
            conn.setRequestMethod("GET");
            int response = conn.getResponseCode();
            //if response message is 200(success)
            if (response == 200) {
                //Reading the response to a StringBuffer
                Scanner responseReader = new Scanner(conn.getInputStream());
                while (responseReader.hasNextLine()) {
                    buffer.append(responseReader.nextLine()).append("\n");
                }
                responseReader.close();

            }else
                {
                    System.out.printf("Link %s is invalid\n", currentPage.getURL().toString());
                }
            return buffer.toString();
    }
    /**
     * This function will write the content to correct file
     * @param  content file content
     * */
    private void writeToFile(String content)
    {
        String host = currentPage.getURL().getHost();
    }

    /**
     * Method will return all links from content
     * @param content file content
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
            String content = fetch();
            writeToFile(content);
            System.out.printf("Visiting page %s at depth %d\n", currentPage.getURL().toString(), currentDepth);
            //adds current page to visited list
            list.addSites(currentPage);
            ArrayList<Page>links = getLinks(content);
            for(Page page: links) {
                //the next page must not be visited
                if(!list.pageAlreadyVisited(page)) {
                    CrawlTask newTask = new CrawlTask(page, currentDepth + 1, maxDepth, logFile, downloadDirectory, useRobots);
                    pool.runTask(newTask);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
