package mta.main;
import mta.Classes.Crawler;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws MalformedURLException, InterruptedException, ExecutionException {
        Crawler crawler = new Crawler(args);
        crawler.runCrawler();
    }
}