package com.mta;

import java.io.*;

public class Crawler {
    private String site_file;
    private String config_file;
    private String log_file;
    private int nThreads;
    private int depth;
    private int log_level;
    private String root_dir;
    private String args;
    private Boolean robots;
    private int delay;
    public void checkArgs(){

    }
    public void readConfigFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(config_file));
        String st, parts[];
        while ((st = br.readLine()) != null) {
            parts = st.split("=");
            switch (parts[0]) {
                case "n_threads":
                    nThreads = Integer.parseInt(parts[1]);
                    break;
                case "depth":
                    depth = Integer.parseInt(parts[1]);
                    break;
                case "log_level":
                    log_level = Integer.parseInt(parts[1]);
                    break;
                case "root_dir":
                    root_dir = parts[1];
                    break;
                case "delay":
                    delay = Integer.parseInt(parts[1].substring(0, parts[1].length() - 2));
                    break;
            }
        }
        String print="n_threads:"+nThreads+"\ndepth:"+depth+"\nlog_level:"+log_level+"\nroot_dir:"+root_dir+"\ndelay:"+delay;
        System.out.println(print);
    }
    public void readSitesFile(){

    }
    public void createThreadPool() {

    }
    public void runCrawler(){

    }
}
