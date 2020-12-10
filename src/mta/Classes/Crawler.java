package mta.Classes;
import mta.Exceptions.ArgumentNotSupportedException;
import mta.Exceptions.SitesFileNotSpecifiedException;
import mta.Exceptions.SizeNotSpecifiedException;
import mta.Exceptions.WordsNotSpecifiedException;
import mta.Singletons.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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
    private final ArrayList<Page> Pages;
    private String option; //the operation chose by the user in the command line
    private ArrayList<String> searchWords;


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
        root_dir = "C:\\Users\\TEO\\Desktop\\Cursuri\\Anul 4\\Ingineria Programarii\\Tema1\\Implementare\\Crawler\\Download";
        size = 0;
        option="search";
        searchWords=new ArrayList<String>();
        searchWords.add("abc");searchWords.add("src");searchWords.add("main");searchWords.add("link");
        searchWords.add("stack");searchWords.add("Academia");
        try {
            //checkArgs(args);
            readConfigFile();
            readSitesFile();
        }catch (Exception exception)
        {
            System.out.println(exception.getMessage());
            System.exit(0);
        }
    }

    /**
     * This method check the arguments and warns the user if they are incorrect.
     * @param args the arguments passed by the user in the command line
     * @returns void
     */
    private void checkArgs(String[]args) throws SitesFileNotSpecifiedException, SizeNotSpecifiedException, ArgumentNotSupportedException, WordsNotSpecifiedException {
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

            if(argsList.contains("search"))
            {
                if(argsList.contains("-words"))
                {
                    int index = argsList.indexOf("-words");
                    searchWords = new ArrayList<>();
                    while((index+1)!=argsList.size())
                    {
                        String element = argsList.get(index+1);
                        if(!element.equals("-conf") && !element.equals("-s"))
                        {
                            searchWords.add(element);
                        }else
                            {
                                break;
                            }
                        index = index+1;
                    }

                }else
                    {
                        throw new WordsNotSpecifiedException();
                    }
                for(String element : searchWords)
                {
                    System.out.println(element);
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
                case "robots":
                    robots =  Boolean.parseBoolean(parts[1]);
                    break;
                case "extensions":
                    AllowedFilesList allowed_files=AllowedFilesList.getInstance();
                    String[] extension=parts[1].split(",");
                    for (String s: extension) {
                        allowed_files.addType(s);
                    }

            }
        }
    }
    private void readSitesFile(){

    }


    /**
     * This method creates a task for each file in file list uses the thread pool to executes them.
     * @returns void
     */
    public void runCrawler() throws MalformedURLException, InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {
        TaskFactory factory = TaskFactory.getInstance();
        ThreadPool pool = ThreadPool.getInstance(nThreads);
        VisitedPageList visited = VisitedPageList.getInstance();
        Page newPage = new Page();
        newPage.addURL(new URL("https://mta.ro/"));
        Pages.add(newPage);
        for (Page page : Pages) {
            Runnable task = null;
            if(visited.pageAlreadyVisited(page))
                continue;

            if (option.equals("crawl")) {
                task = factory.makeCrawlTask(page, 0, 2, log_file, root_dir, robots);
            }
            if (option.equals("sitemap")) {
                task = factory.makeSitemapTask();
            }
            if (option.equals("filter_type")) {
                task = factory.makeFilterTask();
            }
            if (option.equals("filer_size")) {
                task = factory.makeLimitDimensionTask();
            }
            if (option.equals("search")) {
                task = factory.makeSearchWordsTask(searchWords,root_dir);
            }
            pool.incrementActiveTasks();
            pool.runTask(task);
        }
        while (pool.getActiveTasks()!=0)
        {
            Thread.sleep(10);
        }
        pool.shutdownThreadPool();
        SearchWordsList map = SearchWordsList.getInstance();
        map.print();
    }
}
