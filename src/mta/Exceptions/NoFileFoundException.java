package mta.Exceptions;

public class NoFileFoundException  extends Exception{
    @Override
    public String getMessage() {
        return "One of the input files could not be found";
    }
}
