package andreas.ueb01;


public class SimpleThreads {
    static final int RUNS = 1000;

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            for (int i = 0; i < RUNS; i++) {
                System.out.print("A");
            }
        });

        Thread b = new Thread(() -> {
            for (int i = 0; i < RUNS; i++) {
                System.out.print("B");
            }
        });

        a.start();
        b.start();

        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        System.out.println("\nfertig");
    }
}
