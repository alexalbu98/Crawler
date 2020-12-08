package mta.main;
import mta.Classes.Crawler;

import java.net.MalformedURLException;

public class Main {

    public static void main(String[] args) throws MalformedURLException {
        Crawler crawler = new Crawler(args);
        crawler.runCrawler();
    }
}