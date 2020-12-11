package mta.Singletons;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import mta.Classes.Page;

/** This class stores all the pages visited by the crawler. */
public class VisitedPageList {

    private static VisitedPageList instance = null;
    private ArrayList<Page> sites;
    private Semaphore mutex;

    private VisitedPageList()
    {
        sites = new ArrayList<Page>();
        mutex = new Semaphore(1);
    }

    static public VisitedPageList getInstance()
    {
        if(instance == null){
            instance = new VisitedPageList();
        }
        return instance;
    }

    public boolean pageAlreadyVisited(Page page) throws InterruptedException {
       mutex.acquire();
       boolean found = false;
       for(Page it: sites)
       {
           String itURL = it.getURL().toString();
           String pageURL = page.getURL().toString();
           if(itURL.equals(pageURL))
           {
               found = true;
               break;
           }
           if((itURL+"/").equals(pageURL))
           {
               found = true;
               break;
           }
           if((pageURL+"/").equals(itURL))
           {
               found = true;
               break;
           }
       }
       mutex.release();
       return found;
    }

    public void addSites(Page page) throws InterruptedException {
        mutex.acquire();
        sites.add(page);
        mutex.release();
    }

}
