package ismail.threads;

public class Runner implements Runnable {

    private int id;

    public Runner(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 9; i++) {
            try {
                System.out.printf("Hello from Thread %d, run %d\n", id, i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.printf("Someone interrrupted Thread %d!\n", id);
            }
        }
    }
}
