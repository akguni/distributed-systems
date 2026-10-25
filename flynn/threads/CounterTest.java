package flynn.threads;

public class CounterTest implements Runnable {

    private static final int COUNT = 1000000;
    private final Counter counter;

    public CounterTest(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < COUNT; i++) {
            counter.increment();
            counter.decrement();
        }
    }

    static void test(String name, Counter counter) throws InterruptedException {
        Thread a = new Thread(new CounterTest(counter));
        Thread b = new Thread(new CounterTest(counter));
        a.start();
        b.start();
        a.join(); // wartet bis der thread fertig ist
        b.join();
        System.out.println(name + ": erwartet 0, tatsächlich " + counter.value());
    }

    public static void main(String[] args) throws InterruptedException {
        test("Counter", new Counter());
        test("SynchronizedCounter", new SynchronizedCounter());
    }
    // antwort: erst wird die zahl gelesen, dann inkrementiert, dann geschrieben.
    // währenddessen kann der andere thread die zahl schon verändert haben,
    // dadurch kann es zu einer race condition kommen.
    // mit synchronized wartet der andere thread, bis der erste fertig ist.
    // deswegen kommt dann immer 0 raus.
}
