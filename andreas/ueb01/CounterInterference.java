package andreas.ueb01;

public class CounterInterference {
    static final int RUNS = 100000;

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new SynchronizedCounter();

        Thread a = new Thread(() -> {
            for (int i = 0; i < RUNS; i++) {
                counter.increment();
            }
        });

        Thread b = new Thread(() -> {
            for (int i = 0; i < RUNS; i++) {
                counter.decrement();
            }
        });

        a.start();
        b.start();

        a.join();
        b.join();

        System.out.println("Endwert: " + counter.value());
    }
}
