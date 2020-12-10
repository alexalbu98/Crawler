package mta.Classes;

import mta.Singletons.SearchWordsList;
import mta.Singletons.ThreadPool;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
public class SearchWordsTask implements Runnable {
    private ArrayList<String> key_words;
    String path;
    public SearchWordsTask(ArrayList<String> S,String path){
        this.key_words = (ArrayList<String>)S.clone();
        this.path=path;
    }
    /**
     * This function will read all the content from a file
     * @param  filePath file path
     * */



    private static String readContent(String filePath)
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * This function is called when a word is found in a file.It will put in the map the file path
     * where the word was found.
     * @param  f,word  for filestream and word
     * */

    private void wasFound(File f, String word){
        SearchWordsList map = SearchWordsList.getInstance();
        map.addWords(word,f.getPath());
    }
    private String getExtension(File file) {
        String fileName = file.toString();
        int index = fileName.lastIndexOf('.');
        String extension="";
        if(index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }
    @Override
    public void run() {
        ThreadPool pool = ThreadPool.getInstance();
        try {
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String extension=getExtension(file);
                    if(extension.equals("pdf")){
                       /* PDDocument document = PDDocument.load(file);
                        //Instantiate PDFTextStripper class
                        PDFTextStripper pdfStripper = new PDFTextStripper();
                        //Retrieving text from PDF document
                        String text = pdfStripper.getText(document);
                        System.out.println(text);
                        //Closing the document
                        document.close();*/
                    }
                    String content = readContent(file.getPath());
                    for (String iterator : key_words) {
                        boolean isFound = content.indexOf(iterator) != -1 ? true : false;
                        if (isFound == true) {
                            this.wasFound(file, iterator);
                        }
                    }
                }
                if (file.isDirectory()) {
                    SearchWordsTask newTask = new SearchWordsTask(this.key_words,file.getPath());
                    pool.incrementActiveTasks();
                    pool.runTask(newTask);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                pool.decrementActiveTasks();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}