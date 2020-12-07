package mta.Classes;

import mta.Exceptions.ArgumentNotSupportedException;
import mta.Exceptions.SitesFileNotSpecifiedException;
import mta.Exceptions.SizeNotSpecifiedException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Crawler {
    private String site_file;
    private String config_file;
    private String log_file;
    private int nThreads;
    private int depth;
    private int log_level;
    private String root_dir;
    private Boolean robots;
    private int delay;
    private int size;

    /**Sets the default parameters and check if the user prefers other options*/
    public Crawler(String[] args)
    {
        config_file = "config.txt";
        log_file = "log.txt";
        nThreads = 4;
        depth = 3;
        delay = 5;
        robots = true;
        root_dir = "CrawlerDownloads";
        size = 0;
        try {
            checkArgs(args);
            readConfigFile();
        }catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    private void checkArgs(String[]args) throws SitesFileNotSpecifiedException, SizeNotSpecifiedException, ArgumentNotSupportedException {
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        if(argsList.get(0).equals("crawl")
                || argsList.get(0).equals("sitemap")
                || argsList.get(0).equals("filter_type") || argsList.get(0).equals("filter_size")
                ||argsList.get(0).equals("search"))
        {
            if (argsList.contains("-s"))
            {
                int index = argsList.indexOf("-s");
                if(index+1<argsList.size()){
                    site_file = argsList.get(index+1);
                }else
                    {
                        throw new SitesFileNotSpecifiedException();
                    }
            }else
            {
                throw new SitesFileNotSpecifiedException();
            }

            if (argsList.contains("-conf"))
            {
                int index = argsList.indexOf("-conf");
                if(index+1<argsList.size()){
                    config_file = argsList.get(index+1);
                }else
                {
                    System.out.println("No configuration file specified." +
                            " Running with default parameters");
                }
            }else
                {
                    System.out.println("No configuration file specified." +
                            " Running with default parameters");
                }

            if(argsList.contains("filter_size"))
            {
                if(argsList.contains("-size"))
                {
                    int index = argsList.indexOf("-size");
                    if(index+1<argsList.size()) {
                        size = Integer.parseInt(argsList.get(index + 1));
                    }else
                        {
                            throw new SitesFileNotSpecifiedException();
                        }
                }else
                    {
                        throw new SizeNotSpecifiedException();
                    }
            }
        }else
            {
                throw new ArgumentNotSupportedException();
            }
    }

    private void readConfigFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(config_file));
        String st, parts[];
        while ((st = br.readLine()) != null) {
            parts = st.split("=");
            switch (parts[0]) {
                case "n_threads":
                    nThreads = Integer.parseInt(parts[1]);
                    break;
                case "depth":
                    depth = Integer.parseInt(parts[1]);
                    break;
                case "log_level":
                    log_level = Integer.parseInt(parts[1]);
                    break;
                case "root_dir":
                    root_dir = parts[1];
                    break;
                case "delay":
                    delay = Integer.parseInt(parts[1].substring(0, parts[1].length() - 2));
                    break;
            }
        }
        String print="n_threads:"+nThreads+"\ndepth:"+depth+"\nlog_level:"+log_level+"\nroot_dir:"+root_dir+"\ndelay:"+delay;
        System.out.println(print);
    }
    private void readSitesFile(){

    }
    private void createThreadPool() {

    }
    private void runCrawler(){

    }
}
