package mta.Exceptions;

public class SitesFileNotSpecifiedException extends Exception {

        public SitesFileNotSpecifiedException()
        {
            super("Site file must be specified using -s.");
        }
}
