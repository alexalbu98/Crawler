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

   public boolean isTypeAllowed(String type)
   {
       if(types.contains(type)){
           return false;
       }
       return true;
   }

   public void addType(String type)
   {
       types.add(type);
   }
}
