package mta.Singletons;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * This singleton class uses a thread pool to execute tasks
 * */
public class ThreadPool {

    private ExecutorService pool;
    private static ThreadPool instance=null;
    private Semaphore mutex;
    private int activeTasks;

    private ThreadPool(int nThreads)
    {
        pool = Executors.newFixedThreadPool(nThreads);
        mutex = new Semaphore(1);
        activeTasks = 0;
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
        pool.execute(task);
    }

    public void incrementActiveTasks() throws InterruptedException {
        mutex.acquire();
        activeTasks++;
        mutex.release();

    }

    public void decrementActiveTasks() throws InterruptedException {
        mutex.acquire();
        activeTasks--;
        mutex.release();
    }

    public int getActiveTasks() throws InterruptedException {
        int value;
        mutex.acquire();
        value = activeTasks;
        mutex.release();
        return  value;
    }

    public void shutdownThreadPool(){
        pool.shutdown();
    }
}
