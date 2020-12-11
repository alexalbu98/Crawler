package mta.Singletons;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;


public class LogFile {

    static private LogFile instance=null;
    private final String file;
    private final Semaphore mutex;
    private int logLevel;
    private LogFile(String file, int logLevel) throws IOException {
        this.file = file;
        this.mutex = new Semaphore(1);
        this.logLevel = logLevel;
        FileWriter fileWriter =  new FileWriter(file, false);
        fileWriter.close();
    }

    public String getFileName()
    {
        return this.file;
    }

    public int getLogLevel()
    {
        return this.logLevel;
    }

    public void writToFile(String content, String type){
        try {
            mutex.acquire();
            if(logLevel == 0)
            {
                mutex.release();
                return;
            }
            if(logLevel == 1 && !type.equals("INFO"))
            {
                mutex.release();
                return;
            }
            if(logLevel == 2 && !type.equals("INFO") && !type.equals("ERROR"))
            {
                mutex.release();
                return;
            }
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(this.file, true));
            writer.write(type + ": " + content+"\n");
            writer.close();
            mutex.release();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    static public LogFile getInstance(String file, int logLevel) {
        if(instance == null)
        {
            try {
                instance = new LogFile(file, logLevel);
            }catch (Exception e)
            {
                System.exit(-1);
            }
        }
        return instance;
    }

    static public LogFile getInstance()
    {
        return instance;
    }

}
