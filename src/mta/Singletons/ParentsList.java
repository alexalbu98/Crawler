package mta.Singletons;
import mta.Templates.Tuple;
import java.util.ArrayList;

public class ParentsList {
    private static ParentsList instance = null;
    private ArrayList<Tuple<String, String>> parentsList;

    private ParentsList()
    {
        parentsList = new ArrayList<Tuple<String, String>>();
    }

    static public ParentsList getInstance()
    {
        if(instance == null){
            instance = new ParentsList();
        }
        return instance;
    }

    public void removeFromList(Tuple<String, String> entry)
    {
        parentsList.remove(entry);
    }

    public  void addToList(Tuple<String, String> entry)
    {
        parentsList.add(entry);
    }

}
