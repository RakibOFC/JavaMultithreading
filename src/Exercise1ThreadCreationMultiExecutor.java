import java.util.ArrayList;
import java.util.List;

/*
My Answer
class MultiExecutor {

    // Add any necessary member variables here
    List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks) {
        // Complete your code here
        this.tasks = tasks;
    }
    // Starts and executes all the tasks concurrently

    public void executeAll() {
        // complete your code here
        for (Runnable task : tasks) {

            task.run();
        }
    }
}*/

// Provided Answer
class MultiExecutor {

    private final List<Runnable> tasks;

    /*
     * @param tasks to executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    /**
     * Executes all the tasks concurrently
     */
    public void executeAll() {

        List<Thread> threads = new ArrayList<>(tasks.size());

        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            threads.add(thread);
        }

        for(Thread thread : threads) {
            thread.start();
        }
    }
}

public class Exercise1ThreadCreationMultiExecutor {

    public static void main(String[] args) {

    }
}