package andreas.ueb03;

import andreas.ueb03.proto.LogMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;

// Blatt 3, Teil A, Aufgabe 5: Log-Server, schreibt jede Nachricht als eine Zeile in server.log
public class LogServer {
    static final int PORT = 9090;
    static final String LOGFILE = "andreas/ueb03/server.log";   // Pfad gilt, wenn man vom Repo-Root startet

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Log server is running on port " + PORT + ", writing to " + LOGFILE);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handle(clientSocket)).start();   // pro Client ein Thread wie bei Blatt 2
        }
    }

    static void handle(Socket clientSocket) {
        try (clientSocket) {
            InputStream in = clientSocket.getInputStream();
            LogMessage message;
            // Nachrichten kommen mit Längenpräfix (writeDelimitedTo), null = Client hat zugemacht
            while ((message = LogMessage.parseDelimitedFrom(in)) != null) {
                writeLine(format(message));
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Format: Zeit | Level | host/programm | Text
    static String format(LogMessage m) {
        Instant time = Instant.ofEpochSecond(m.getTimestamp().getSeconds(), m.getTimestamp().getNanos());
        String origin = m.hasOrigin() ? m.getOrigin().getHost() + "/" + m.getOrigin().getProgram() : "-";
        String text = m.getText().replace('\n', ' ');   // Zeilenumbrüche raus, sonst wären es mehrere Zeilen
        return time + " | " + String.format("%-5s", m.getSeverity()) + " | " + origin + " | " + text;
    }

    // synchronized, damit sich bei mehreren Clients die Zeilen nicht vermischen
    static synchronized void writeLine(String line) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOGFILE, true))) {
            writer.println(line);
        }
        System.out.println(line);
    }
}
