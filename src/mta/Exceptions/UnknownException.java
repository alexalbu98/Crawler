package mta.Exceptions;

public class UnknownException extends Exception{
    public UnknownException()
    {
        super("An undefined error has occurred");
    }
}
