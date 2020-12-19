package mta.Classes;

import mta.Singletons.AllowedFilesList;
import java.io.*;
import java.util.Scanner;

public class FilterTask implements Runnable {
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

                  } 
          }
     }

}

