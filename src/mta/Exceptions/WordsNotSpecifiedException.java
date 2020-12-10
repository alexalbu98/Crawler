package mta.Exceptions;

public class WordsNotSpecifiedException extends Exception {
    public WordsNotSpecifiedException()
    {
        super("Words must be specified using -words.");
    }
}
