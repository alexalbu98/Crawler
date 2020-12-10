package mta.Exceptions;

public class MaxDepthReachedException extends Exception {
    public MaxDepthReachedException()
    {
        super("Max recursive depth reached\n");
    }
}
