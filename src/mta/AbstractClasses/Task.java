package mta.AbstractClasses;
import mta.Classes.Page;
import mta.Interfaces.Crawl;

import java.util.ArrayList;

/**
 * Abstract class for task
 * */
public abstract class Task implements Runnable {

    private Page page;
    private String downloadDir;
    private int currentDepth;
    private String logFile;
    private int delay;
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
     * */
    public String fetch()
    {
        return null;
    }

    /**
     * Gets all the links in the page
     * */
    public ArrayList<Page>getLinks()
    {
        return null;
    }
}
