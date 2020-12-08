package mta.AbstractClasses;
import mta.Classes.Page;
import mta.Interfaces.Crawl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class for task
 * */
public abstract class Task implements Runnable {

    protected Page page;
    protected String downloadDir;
    protected int currentDepth;
    protected String logFile;
    protected int delay;
    public Crawl crawlStrategy;


    public void addPage(Page page){
        this.page = page;
    }
    public void setDownloadDir(String dir)
    {
        downloadDir = dir;
    }
    public void setLogFile(String file)
    {
        logFile = file;
    }
    public void setCrawlStrategy(Crawl strategy)
    {
        crawlStrategy = strategy;
    }
    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    /**
     * Gets the content of the site
     * @param writeToFile tells the method if to write the content to a file or not
     * */
    protected String fetch(boolean writeToFile)
    {
        StringBuilder content = new StringBuilder();
        try {
                //Opening a connection
                HttpURLConnection conn = (HttpURLConnection) page.getURL().openConnection();
                //Sending the request
                conn.setRequestMethod("GET");
                int response = conn.getResponseCode();
                if (response == 200) {
                    Scanner responseReader = new Scanner(conn.getInputStream());
                    while (responseReader.hasNextLine()) {
                        content.append(responseReader.nextLine()).append("\n");
                    }
                    responseReader.close();
                }
            }catch(Exception e)
            {
                System.out.println("Invalid url");
                return null;
            }

        if(writeToFile)
        {
                URL url = page.getURL();
                String old = url.getProtocol()+"://"+url.getHost();
                String news = downloadDir+ "/" + url.getHost();
                String location = url.toString();
                //changes domain with local folder
                location = location.replace(old, news);
                File file = new File(location);
                if(file.getParentFile().mkdirs())
                {
                    System.out.print("Created directory.\n");
                }
                //if it`s the first page of the site save locally as mainPage
                if(url.getFile().equals("/"))
                    location=location+"mainPage";
                if(url.getFile().equals(""))
                    location=location+"/mainPage";
                try {
                    FileOutputStream fos = new FileOutputStream(location);
                    fos.write(String.valueOf(content).getBytes(), 0, content.length());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

        }

        return content.toString();
    }

    /**
     * Gets all the links in the page
     * */
    protected ArrayList<Page>getLinks(String content) throws MalformedURLException {
        ArrayList<Page>linkList = new ArrayList<>();
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find())
        {
            Page page = new Page();
            page.addURL(new URL(m.group()));
            linkList.add(page);
        }

        return linkList;

    }
}
