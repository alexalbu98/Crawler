package mta.main;
import mta.AbstractClasses.Task;
import mta.Classes.Crawler;
import mta.Exceptions.CannotCrawlException;
import mta.Exceptions.TaskTypeNotSupportedException;
import mta.Singletons.TaskFactory;

public class Main {

    public static void main(String[] args) throws TaskTypeNotSupportedException, CannotCrawlException {
        TaskFactory factory = TaskFactory.getInstance();
        Task task = factory.makeTask("crawl");
        if(task.crawlStrategy.canCrawl())
        {
            task.crawlStrategy.getRobotsFile();
        }
        task.crawlStrategy.getSitemaps();
        Crawler crawler = new Crawler(args);
        crawler.runCrawler();
    }
}