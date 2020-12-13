package mta.Classes;

public class FilterTask implements Runnable {
    @Override
    public void run() 
    {
        AllowedFilesList allowed_files=AllowedFilesList.getInstance();
        String[] pathnames;
        File f = new File(root_dir);
        pathnames = f.list();

         for (String pathname : pathnames) {
               if(AllowedFilesList.isTypeAllowed(pathname)==0)
                  {
                    File filter = new File(pathname);
                    filter.delete();
                    filter.close();
                  } 
          }
     }
}


