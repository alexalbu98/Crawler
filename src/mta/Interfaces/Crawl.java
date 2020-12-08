package mta.Interfaces;

import mta.Exceptions.CannotCrawlException;

import java.util.ArrayList;

/**
 * Interface for crawl strategy which will determine what type of task is used
 * */
public interface Crawl {

    void getRobotsFile() throws CannotCrawlException;
    ArrayList<String> getDisallowedList() throws CannotCrawlException;
    ArrayList<String> getSitemaps() throws CannotCrawlException;
    int getCrawlDelay() throws CannotCrawlException;
    void setReadRobots(boolean state);
    boolean getReadRobots();
}

