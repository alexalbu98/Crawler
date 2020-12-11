package mta.Singletons;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mta.Classes.Page;

/** This class stores a map that contains the key words given by user and the files
 * where these words can be found. */
public class SearchWordsList<Map> {

    private static SearchWordsList instance = null;
    HashMap<String, ArrayList<String>> words_map;


    private SearchWordsList()
    {
        words_map = new HashMap<String,ArrayList<String>>();
    }

    static public SearchWordsList getInstance()
    {
        if(instance == null){
            instance = new SearchWordsList();
        }
        return instance;
    }

    public HashMap<String,ArrayList<String>> getWordsMap()
    {
        return this.words_map;
    }

    /**
     * This function will add a file path(in the map) for the word that was found
     * @param  word,path  for the word and the file path
     * */
    public void addWords(String word,String path)
    {
        ArrayList<String> list;
        if(this.words_map.get(word)==null){
            list=new ArrayList<String>();
        }
        else{
            list=this.words_map.get(word);
        }
        list.add(path);
        this.words_map.put(word,list);
    }
    /**
     * This function will print all the values in the map
     * */
    public void print() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("searched_words.txt", "UTF-8");
        for (java.util.Map.Entry<String,ArrayList<String>> entry : this.words_map.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            writer.println("-----------------------");
            String output="Key word \""+key+"\" was found in the next files:";
            writer.println(output);
            for (String i:value){
                output="     -"+i;
                writer.println(output);
            }
        }
        writer.close();
    }

}