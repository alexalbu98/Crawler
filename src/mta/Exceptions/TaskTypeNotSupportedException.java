package mta.Exceptions;

public class TaskTypeNotSupportedException extends Exception{
    public TaskTypeNotSupportedException()
    {
        super("Task type is not supported and cannot be created\n");
    }
}
