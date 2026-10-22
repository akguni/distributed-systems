package ismail.threads;

public class Synchronization {
    private final static int NUMBER_OF_THREADS = 4;
    private final static Thread[] threads = new Thread[NUMBER_OF_THREADS];


    static void main() {

        final Counter counter =  new Counter();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(new CounterRunner(i, counter));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

}

class CounterRunner implements Runnable {

    private final int id;
    private final Counter counter;

    public CounterRunner(int id, Counter counter) {
        this.id = id;
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 9; i++) {
            try {
                System.out.printf("THREAD %d, RUN %d: c was %d\n", id, i, counter.value());
                counter.increment();
                System.out.printf("THREAD %d, RUN %d: incremented c by 1\n", id, i);
                System.out.printf("THREAD %d, RUN %d: c is now %d\n", id, i, counter.value());

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.printf("Someone interrrupted Thread %d!\n", id);
            }
        }
    }
}

