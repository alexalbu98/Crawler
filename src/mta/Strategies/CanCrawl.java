package mta.Strategies;
import mta.Interfaces.Crawl;
import java.util.ArrayList;

public class CanCrawl implements Crawl {
    private boolean useRobots = true;
    private int crawlDelay = 0;
    private ArrayList<String> sitemaps = null;
    private ArrayList<String> disallowedList = null;

    public CanCrawl()
    {
        sitemaps = new ArrayList<>();
        disallowedList = new ArrayList<>();
    }



    @Override
    public void getRobotsFile() {
        System.out.println("Reading robots.txt");
    }

    @Override
    public ArrayList<String> getDisallowedList() {
        return disallowedList;
    }

    @Override
    public ArrayList<String> getSitemaps() {
        return sitemaps;
    }

    @Override
    public int getCrawlDelay() {
        return crawlDelay;
    }

    @Override
    public void setReadRobots(boolean state) {
        useRobots = state;
    }

    @Override
    public boolean getReadRobots() {
        return useRobots;
    }
}
