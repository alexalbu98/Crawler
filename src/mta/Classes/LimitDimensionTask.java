package mta.Classes;

import mta.Singletons.ThreadPool;

import java.io.*;
import java.util.*;

public class LimitDimensionTask implements Runnable {

    private String root_dir;
    private int file_size;

    public LimitDimensionTask(String dir, int size)
    {
        root_dir=dir;
        file_size=size;
    }

    @Override
    public void run()
    {
        ThreadPool pool=ThreadPool.getInstance();
        File folder = new File(root_dir);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                //length() returneaza marimea in octeti, impartim la 1.048.576 pentru a primi marimea in MB
                if ((file.length() / 1024) > file_size)
                {
                    file.delete();
                }
            }
        }

        /* File dir = new File(root_dir);

        File[] files = dir.listFiles();

        Arrays.sort(files, (f1, f2) -> {
            return new Long(f1.length()).compareTo(new Long(f2.length()));
        });

        for (File file : files) {
            if (!file.isHidden()) {
                if (!file.isDirectory()) {
                    System.out.println("FILE\t" + " " + file.length() +
                            " bytes\t\t" + file.getName());
                }
            }
        }*/
    }
}
