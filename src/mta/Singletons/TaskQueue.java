package mta.Singletons;
import java.util.ArrayList;
import mta.AbstractClasses.Task;

/** Queue that stores all the tasks that must be executed by threads. */
public class TaskQueue {
    private static TaskQueue instance = null;
    private ArrayList<Task> tasks;
    public int finishedTasks;
    public int startedTasks;

    private TaskQueue()
    {
        tasks = new ArrayList<Task>();
        finishedTasks = 0;
        startedTasks = 0;
    }

    static public TaskQueue getInstance()
    {
        if(instance == null){
            instance = new TaskQueue();
        }
        return instance;
    }

    public void addTask(Task task)
    {
        tasks.add(task);
    }

    /** Removes the first task from queue
     * @returns void*/
    public void removeTask()
    {
        if(tasks.isEmpty())
        {
            return;
        }
        tasks.remove(0);
    }
    /**
     *This method returns the first task in the task queue
     * @return the first task in the list
     * */
    public Task getTask()
    {
        return tasks.get(0);
    }

    public boolean isQueueEmpty()
    {
        return tasks.isEmpty();
    }

}
