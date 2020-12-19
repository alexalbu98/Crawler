package mta.Classes;

import java.io.*;
import mta.Singletons.AllowedFilesList;

public class FilterTask implements Runnable {

private String root_dir;

public FilterTask(String dir)
{
    root_dir=dir;
}


    @Override

    public void run() 
    {
        AllowedFilesList allowed_files=AllowedFilesList.getInstance();
        String[] pathnames;
        File f = new File(root_dir);
        pathnames = f.list();

         for (String pathname : pathnames) {
               if(allowed_files.isTypeAllowed(pathname)==0)
                  {
                    File filter = new File(pathname);
                    filter.delete();
                    //filter.close();
                  } 
          }
     }
}


