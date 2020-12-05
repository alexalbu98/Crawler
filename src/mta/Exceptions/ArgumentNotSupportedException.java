package mta.Exceptions;

public class ArgumentNotSupportedException extends Exception {
    public ArgumentNotSupportedException()
    {
        super("This argument is not supported, available commands are:\n" +
                "crawl, sitemap, filter_type, filter_size, search");
    }
}
