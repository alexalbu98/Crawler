package mta.Classes;

import mta.Singletons.ThreadPool;

import java.io.*;
import java.util.ArrayList;

public class SitemapTask {
    private PrintWriter writer;
    public void close_writer(){
        this.writer.close();
    }
    public SitemapTask(String path,int level) throws FileNotFoundException, UnsupportedEncodingException {
        this.writer = new PrintWriter(new FileOutputStream(
                new File("sitemap.txt")));
    }
    private void print_to_file(String name,int level,int state) throws FileNotFoundException, UnsupportedEncodingException {
        String to_write;
        if(level==0){
            writer.println("------------------------");
            to_write="Site-ul "+name+" contine urmatoarele fisiere:";
            writer.println(to_write);
        }
        else {
            for (int i = 0; i < level; i++) {
                writer.print("    ");
            }
            if(state==1){
                to_write="-Folderul "+name+" care contine urmatoarele fisiere:";
                writer.println(to_write);
            }
            else{
                to_write="-Fisierul "+name;
                writer.println(to_write);
            }
        }
    }

    public void run(String path,int level) {
        try {
            int state;
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if(file.isDirectory()) {
                    state=1;
                }
                else{
                    state=0;
                }
                print_to_file(file.getName(),level,state);
                if (file.isDirectory()) {
                    run(file.getPath(),level+1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}