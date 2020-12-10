package mta.main;
import mta.Classes.Crawler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws MalformedURLException, InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {
        Crawler crawler = new Crawler(args);
        crawler.runCrawler();
    }
}