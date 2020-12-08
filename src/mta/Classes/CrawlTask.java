package mta.Classes;

import mta.AbstractClasses.Task;
import mta.Exceptions.CannotCrawlException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import java.util.ArrayList;

public class CrawlTask extends Task {
    @Override
    public void run() {
        String content = fetch(true);
        try {
            ArrayList<Page> list = getLinks(content);
            for(Page page:list)
            {
                System.out.println(page.getURL().toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
