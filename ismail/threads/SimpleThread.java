package ismail.threads;

public class SimpleThread {

    private final static int NUMBER_OF_THREADS = 4;
    private final static Thread[] threads = new Thread[NUMBER_OF_THREADS];


    public static void main(String[] args) {

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(new Runner(i));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

}
