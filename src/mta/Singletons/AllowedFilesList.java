package mta.Singletons;
import java.util.ArrayList;

public class AllowedFilesList {
    private static AllowedFilesList instance = null;
    private ArrayList<String> types;

    private AllowedFilesList()
    {
        types = new ArrayList<String>();
    }
    static public AllowedFilesList getInstance()
    {
        if(instance == null){
            instance = new AllowedFilesList();
        }
        return instance;
    }

  public int isTypeAllowed(String pathname)
   {
       int flag=0;
      for(String type : types)
        {
          if(pathname.endsWith(type))
              flag=1;
       }
    return flag;
   }

   public boolean isFileAllowed(String file)
   {
       String extension = "";

       int i = file.lastIndexOf('.');
       int p = Math.max(file.lastIndexOf('/'), file.lastIndexOf('\\'));

       if (i > p) {
           extension = file.substring(i+1);
       }
       if(extension.equals(""))
       {
           return true;
       }
       return types.contains(extension);
   }

   public void addType(String type)
   {
       types.add(type);
   }
}
