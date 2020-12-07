package mta.Singletons;
import mta.AbstractClasses.Task;
import mta.Classes.*;
import mta.Exceptions.TaskTypeNotSupportedException;
import mta.Strategies.CanCrawl;
import mta.Strategies.CanNotCrawl;

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

    public Task makeTask(String type) throws TaskTypeNotSupportedException {
        switch (type) {
            case "crawl" -> {
                CrawlTask crawlTask = new CrawlTask();
                crawlTask.setCrawlStrategy(new CanCrawl());
                return crawlTask;
            }
            case "sitemap" -> {
                SitemapTask sitemapTask = new SitemapTask();
                sitemapTask.setCrawlStrategy(new CanNotCrawl());
                return sitemapTask;
            }
            case "filter_type" -> {
                FilterTask filterTask = new FilterTask();
                filterTask.setCrawlStrategy(new CanNotCrawl());
                return filterTask;
            }
            case "filter_size" -> {
                LimitDimensionTask dimensionTask = new LimitDimensionTask();
                dimensionTask.setCrawlStrategy(new CanNotCrawl());
                return dimensionTask;
            }
            case "search" -> {
                SearchWordsTask searchWordsTask = new SearchWordsTask();
                searchWordsTask.setCrawlStrategy(new CanNotCrawl());
                return searchWordsTask;
            }
            default -> throw new TaskTypeNotSupportedException();
        }
    }

}
