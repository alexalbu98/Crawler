package mta.Singletons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This singleton class uses a thread pool to execute tasks
 * */
public class ThreadPool {

    private ExecutorService pool;
    private static ThreadPool instance=null;

    private ThreadPool(int nThreads)
    {
        pool = Executors.newFixedThreadPool(nThreads);
    }
    private ThreadPool(){}

    public static ThreadPool getInstance()
    {
        if(instance==null)
        {
            instance = new ThreadPool();
        }
        return instance;
    }

    public static ThreadPool getInstance(int nThreads)
    {
        if(instance==null)
        {
            instance = new ThreadPool(nThreads);
        }
        return instance;
    }

    /**
     * Runs a task and awaits for it to finish
     * @param task the task that must be executed.
     */
    public void runTask(Runnable task) throws InterruptedException {
       Future<?>result = pool.submit(task);
       //awaits task to finish
       while(!result.isDone())
       {
           Thread.sleep(10);
       }
    }

    public void shutdownThreadPool(){
        pool.shutdown();
    }
}
