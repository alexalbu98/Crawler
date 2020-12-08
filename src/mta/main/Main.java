package mta.main;
import mta.Classes.Crawler;

<<<<<<< HEAD
import java.net.MalformedURLException;


public class Main {

    public static void main(String[] args) throws MalformedURLException {
=======

public class Main {

    public static void main(String[] args){
>>>>>>> Creare clasa abstracta Task
        Crawler crawler = new Crawler(args);
        crawler.runCrawler();
    }
}