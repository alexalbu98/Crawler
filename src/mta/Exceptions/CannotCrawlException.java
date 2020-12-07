package mta.Exceptions;

public class CannotCrawlException extends Exception{
    public CannotCrawlException()
    {
        super("Crawl method is not supported!\n");
    }
}
