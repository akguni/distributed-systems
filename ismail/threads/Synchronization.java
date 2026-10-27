package ismail.threads;

public class Synchronization {

    private static final int NUMBER_OF_THREADS = 20;
    private static final int INCREMENTS_PER_THREAD = 50;

    public static void main(String[] args) throws InterruptedException {
        test("OrdinaryCounter", new OrdinaryCounter());
        test("SynchronizedCounter", new SynchronizedCounter());
    }

    static void test(String name, CounterInterface counter)
            throws InterruptedException {
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        int expected = NUMBER_OF_THREADS * INCREMENTS_PER_THREAD;

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(
                    new CounterRunner(counter, INCREMENTS_PER_THREAD),
                    "counter-worker-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int actual = counter.value();
        if (actual != expected) {
            System.out.printf("%s: INTERFERENCE DETECTED; expected %d, got %d%n",
                    name, expected, actual);
        } else {
            System.out.printf("%s: no interference; value is %d%n", name, actual);
        }
    }
}

final class CounterRunner implements Runnable {

    private final CounterInterface counter;
    private final int repetitions;

    CounterRunner(CounterInterface counter, int repetitions) {
        this.counter = counter;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        for (int i = 0; i < repetitions; i++) {
            counter.increment();
        }
    }
}
