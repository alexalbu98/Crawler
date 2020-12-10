package mta.Singletons;
import java.util.ArrayList;
import mta.Classes.Page;

/** This class stores all the pages visited by the crawler. */
public class VisitedPageList {

    private static VisitedPageList instance = null;
    private ArrayList<Page> sites;

    private VisitedPageList()
    {
        sites = new ArrayList<Page>();
    }

    static public VisitedPageList getInstance()
    {
        if(instance == null){
            instance = new VisitedPageList();
        }
        return instance;
    }

    public boolean pageAlreadyVisited(Page page)
    {
       boolean found = false;
       for(Page it: sites)
       {
           if (it.getURL().toString().equals(page.getURL().toString())) {
               found = true;
               break;
           }
       }
       return found;
    }

    public void addSites(Page page)
    {
        sites.add(page);
    }

}
