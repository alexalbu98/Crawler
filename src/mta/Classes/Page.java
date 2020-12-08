package mta.Classes;

import java.net.MalformedURLException;
import java.net.URL;

public class Page {
    URL url;
    public void addURL(URL link) throws MalformedURLException {
        this.url= link;
    }
    public URL getURL(){
        return this.url;
    }
}