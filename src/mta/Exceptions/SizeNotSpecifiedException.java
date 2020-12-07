package mta.Exceptions;

public class SizeNotSpecifiedException extends Exception {
    public SizeNotSpecifiedException()
    {
        super("Size in MB must be specified using -size\n");
    }
}
