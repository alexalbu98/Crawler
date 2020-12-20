package mta.Classes;

import mta.Singletons.AllowedFilesList;
import java.io.*;
import java.util.Scanner;



public class FilterTask implements Runnable {
    private String root_dir;

    public FilterTask(String dir) {
        root_dir = dir;
    }

    public void run(String dir) {
        AllowedFilesList allowed_files = AllowedFilesList.getInstance();
        try {
            File folder = new File(dir);
            boolean chk;
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {

                    if (allowed_files.isTypeAllowed(file) == 0) {
                        System.out.print("Found file to delete");
                        chk = file.delete();
                        if (chk) {
                            System.out.print("File succesfully deleted");
                        }
                    }
                }
                if (file.isDirectory()) {
                    run(file.getPath());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}