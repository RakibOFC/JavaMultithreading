/**
* Quiz 2 - Question 2
* */

public class Quiz2Question2 {

    public static void main(String[] args) {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I'm going for a walk");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I'm going to swim");
            }
        });

        thread1.start();
        while (thread1.isAlive()) {

            System.out.println("I'm going home");
        }
        thread2.start();

    }
}
