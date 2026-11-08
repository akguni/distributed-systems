package flynn.HA3.aufgabe1.code;

import flynn.HA3.aufgabe1.code.generated.LogMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;

public class LogServer {

    private static final int PORT = 9090;
    private static final String LOG_FILE = "flynn/HA3/aufgabe1/log.txt";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("server läuft auf port " + PORT);

        while (true) {
            Socket client = serverSocket.accept(); // wartet, bis sich ein client verbindet
            new Thread(() -> handle(client)).start(); // jeder client bekommt einen eigenen thread
        }
    }

    private static void handle(Socket client) {
        try {
            InputStream in = client.getInputStream();
            LogMessage message;
            while ((message = LogMessage.parseDelimitedFrom(in)) != null) { // wartet auf die nächste nachricht, null = client hat geschlossen
                write(message);
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void write(LogMessage message) throws IOException {
        String origin = message.hasOrigin() ? message.getOrigin() : "-";
        String line = Instant.ofEpochMilli(message.getTimestamp()) + " | " + message.getSeverity() + " | " + origin + " | " + message.getText();
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) { // true = anhängen
            writer.write(line + "\n");
        }
        System.out.println(line);
    }
}
