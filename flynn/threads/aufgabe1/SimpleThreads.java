package flynn.threads.aufgabe1; // erstellt das package, wie namespace in c#

public class SimpleThreads implements Runnable { // wie interface in c#

    private static final int COUNT = 10; // static final == const in c#
    private final String name; // final == readonly in c#

    public SimpleThreads(String name) {
        this.name = name;
    }

    @Override // run() ist abstrakt (interface), und MUSS implementiert werden.
    public void run() { // code, der im thread läuft
        for (int i = 1; i <= COUNT; i++) {
            System.out.println(name + " : " + i);
        }
    }

    public static void main(String[] args) { // einstieg, wie Main in c#
        Thread a = new Thread(new SimpleThreads("A")); // parameter muss vom typ runnable sein, deshalb implementtiert simplethreads runnable
        Thread b = new Thread(new SimpleThreads("B"));
        Thread c = new Thread(new SimpleThreads("C"));
        a.start(); // ruft run() in einem neuen thread auf
        b.start();
        c.start();
    }

    // antwort: die zahlen vermischen sich, weil alle threads
    // gleichzeitig laufen. die reihenfolge ist nicht vorhersehbar.
    // mal kommt ein thread mehrmals hintereinander dran,
    // mal wechseln sie sich schneller ab.
}
