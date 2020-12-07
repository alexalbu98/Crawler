package mta.Exceptions;

public class NoConnectionException  extends Exception{
    public NoConnectionException()
    {
        super("Connection to the Internet not found\n");
    }
}
