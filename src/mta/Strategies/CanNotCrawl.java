package mta.Strategies;

import mta.Exceptions.CannotCrawlException;
import mta.Interfaces.Crawl;

import java.util.ArrayList;

public class CanNotCrawl implements Crawl {
    @Override
    public boolean canCrawl() {
        return false;
    }

    @Override
    public void getRobotsFile() throws CannotCrawlException {
        throw new CannotCrawlException();
    }

    @Override
    public ArrayList<String> getDisallowedList() throws CannotCrawlException {
        throw new CannotCrawlException();
    }

    @Override
    public ArrayList<String> getSitemaps() throws CannotCrawlException {
        throw  new CannotCrawlException();
    }

    @Override
    public int getCrawlDelay() throws CannotCrawlException {
        throw new CannotCrawlException();
    }

    @Override
    public boolean getUseRobots() throws CannotCrawlException {
        throw new CannotCrawlException();
    }
}
