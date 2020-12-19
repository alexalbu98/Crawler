package mta.Singletons;
import mta.Classes.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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

    public Runnable makeFilterTask(String dir)
    {
        return new FilterTask(dir);
    }
    public Runnable makeLimitDimensionTask(String dir)
    {
        return new LimitDimensionTask(dir);
    }
    public Runnable makeSearchWordsTask(ArrayList<String> S, String path)
    {
        return new SearchWordsTask(S,path);
    }
    public void makeSitemapTask(String path,int level) throws FileNotFoundException, UnsupportedEncodingException {
        SitemapTask task=new SitemapTask(path,level);
        task.run(path,level);
        task.close_writer();
    }

}
