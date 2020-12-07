package mta.Exceptions;

public class NoConnectionException  extends Exception{
    @Override
    public String getMessage() {
       return "Connection to the Internet not found";
    }
}
