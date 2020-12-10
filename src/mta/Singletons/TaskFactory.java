package mta.Singletons;
import mta.Classes.*;
import mta.Exceptions.TaskTypeNotSupportedException;


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

    public Runnable makeCrawlTask(Page page, int depth, int maxDepth, String logFile, String rootDir, boolean useRobots)
    {
        return new CrawlTask(page, depth, maxDepth, logFile, rootDir, useRobots);
    }

    public Runnable makeFilterTask()
    {
        return new FilterTask();
    }
    public Runnable makeLimitDimensionTask()
    {
        return new LimitDimensionTask();
    }
    public Runnable makeSearchWordsTask()
    {
        return new SearchWordsTask();
    }
    public Runnable makeSitemapTask()
    {
        return new SitemapTask();
    }

}
