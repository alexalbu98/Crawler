package mta.Singletons;
import mta.Classes.*;
import java.util.ArrayList;

/**
 *
 * * Task factory that creates tasks
 * */
public class TaskFactory {
    static private TaskFactory factory = null;
    private TaskFactory(){};

    static public TaskFactory getInstance()
    {
        if(factory == null)
        {
            return new TaskFactory();
        }
        return factory;
    }

    public Runnable makeCrawlTask()
    {
        return new CrawlTask();
    }

    public Runnable makeCrawlTask(Page page, int depth, int maxDepth, int delay, int logLevel, String logFile, String rootDir, boolean useRobots)
    {
        return new CrawlTask(page, depth, maxDepth, delay, logLevel, logFile, rootDir, useRobots);
    }

    public Runnable makeFilterTask()
    {
        return new FilterTask();
    }
    public Runnable makeLimitDimensionTask()
    {
        return new LimitDimensionTask();
    }
    public Runnable makeSearchWordsTask(ArrayList<String> S, String path)
    {
        return new SearchWordsTask(S,path);
    }
    public Runnable makeSitemapTask()
    {
        return new SitemapTask();
    }

}
