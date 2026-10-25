package flynn.threads.aufgabe2;

public class SynchronizedCounter extends Counter { // erbt von Counter (wie ": Counter" in c#)

    @Override // überschreibt wie bei virtual in c#, nur muss die basisfunktion in java nicht als virtual markiert sein muss
    public synchronized void increment() { // synchronized == lock (this) {...} in c#
        super.increment();
    }

    @Override
    public synchronized void decrement() {
        super.decrement();
    }

    @Override
    public synchronized int value() {
        return super.value();
    }
}
