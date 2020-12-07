package mta.Exceptions;

public class UnknownException extends Exception{
    @Override
    public String getMessage() {
        return "An undefined error has occurred";
    }
}
