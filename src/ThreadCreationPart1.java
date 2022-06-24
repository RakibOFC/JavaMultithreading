/**
* Section_2
* 4. Threads Creation - Part 1, Thread Capabilities & Debugging
* */

public class ThreadCreationPart1 {

    public static void main(String[] args)  {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                // Code that will run in the thread
                System.out.println("We are now in the thread \"" + Thread.currentThread().getName() + "\"");
                System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
            }
        });

        thread.setName("New Worker Thread");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread \"" + Thread.currentThread().getName() + "\" before starting a new thread");
        thread.start();
        System.out.println("We are in thread \"" + Thread.currentThread().getName() + "\" after starting a new thread");
    }
}
