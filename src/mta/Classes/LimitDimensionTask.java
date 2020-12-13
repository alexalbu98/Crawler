package mta.Classes;
import java.io.*;
import java.util.*;

public class LimitDimensionTask implements Runnable {

    private String root_dir;
    public LimitDimensionTask(String dir)
    {
        root_dir=dir;
    }
    public void run() {
        File dir = new File(root_dir);

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
        }
    }
}
