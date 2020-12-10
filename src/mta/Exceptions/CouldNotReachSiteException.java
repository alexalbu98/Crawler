package mta.Exceptions;

public class CouldNotReachSiteException extends Exception {
    public CouldNotReachSiteException()
    {
        super("Could not reach site!\n");
    }
    public CouldNotReachSiteException(String message)
    {
        super(message);
    }
}
