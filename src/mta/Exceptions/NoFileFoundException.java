package mta.Exceptions;

public class NoFileFoundException  extends Exception{
    public NoFileFoundException()
    {
        super("One of the input files could not be found\n");
    }
}
