package mta.Classes;
import mta.AbstractClasses.Task;
import mta.Exceptions.ArgumentNotSupportedException;
import mta.Exceptions.SitesFileNotSpecifiedException;
import mta.Exceptions.SizeNotSpecifiedException;
import mta.Singletons.TaskFactory;
import mta.Singletons.TaskQueue;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class Crawler {
    private String site_file;
    private String config_file;
    private String log_file;
    private int nThreads;
    private int depth;
    private int log_level;
    private String root_dir;
    private Boolean robots; //variable that tells the crawler if to read robots.txt or not
    private int delay;
    private int size;
    private ArrayList<Page> Pages;
    private String option; //the operation chose by the user in the command line
    ExecutorService pool;

    /**The crawler class that does all the work*/
    public Crawler(String[] args)
    {
        Pages = new ArrayList<>();
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
            createThreadPool();
            readSitesFile();
        }catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * This method check the arguments and warns the user if they are incorrect.
     * @param args the arguments passed by the user in the command line
     * @returns void
     */
    private void checkArgs(String[]args) throws SitesFileNotSpecifiedException, SizeNotSpecifiedException, ArgumentNotSupportedException {
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        if(argsList.get(0).equals("crawl")
                || argsList.get(0).equals("sitemap")
                || argsList.get(0).equals("filter_type") || argsList.get(0).equals("filter_size")
                ||argsList.get(0).equals("search"))
        {
            option = argsList.get(0);
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

    /**
     * This method reads the configuration file and saves the configurations.
     * */
    private void readConfigFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(config_file));
        String st;
        String[] parts;
        while ((st = br.readLine()) != null) {
            parts = st.split("=");
            switch (parts[0]) {
                case "n_threads" -> nThreads = Integer.parseInt(parts[1]);
                case "depth" -> depth = Integer.parseInt(parts[1]);
                case "log_level" -> log_level = Integer.parseInt(parts[1]);
                case "root_dir" -> root_dir = parts[1];
                case "delay" -> delay = Integer.parseInt(parts[1].substring(0, parts[1].length() - 2));
            }
        }
        String print="n_threads:"+nThreads+"\ndepth:"+depth+"\nlog_level:"+log_level+"\nroot_dir:"+root_dir+"\ndelay:"+delay;
        System.out.println(print);
    }
    private void readSitesFile(){

    }
    private void createThreadPool() {

    }

    /**
     * This method creates a task for each file in file list uses the thread pool to executes them.
     * @returns void
     */
    public void runCrawler() {
        TaskFactory factory = TaskFactory.getInstance();
        TaskQueue queue = TaskQueue.getInstance();
        for(Page page : Pages)
        {
            try {
                Task task = factory.makeTask(option);
                task.addPage(page);
                task.setDownloadDir(root_dir);
                task.setLogFile(log_file);
                queue.addTask(task);
                queue.startedTasks++;
            }catch (Exception exp)
            {
               System.out.println(exp.getMessage());
            }
        }

        while(!queue.isQueueEmpty() && queue.startedTasks != queue.finishedTasks)
        {
            pool.execute(queue.getTask());
            queue.removeTask();
        }



    }
}
