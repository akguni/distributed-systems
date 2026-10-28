package flynn.HA2.aufgabe2.a;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileServer {

    private static final int PORT = 8080;
    private static final String ROOT = "flynn/HA2/aufgabe2/a"; // relativ zum projektordner
    private static final String PAGE = "/index.html";
    private static final String HTTP_VERSION = "HTTP/1.1";

    // wie enum in c#, kann in java aber werte speichern
    private enum Status {
        OK("200 OK"),
        NOT_FOUND("404 Not Found");

        private final String text;

        Status(String text) {
            this.text = text;
        }
    }

    private enum ContentType {
        HTML("text/html; charset=utf-8"),
        TEXT("text/plain; charset=utf-8");

        private final String text;

        ContentType(String text) {
            this.text = text;
        }
    }

    // nimmt clients an
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("server läuft auf port " + PORT);
        System.out.println("http://localhost:" + PORT);

        while (true) {
            Socket client = serverSocket.accept(); // wartet, bis sich ein client verbindet
            Thread thread = new Thread(() -> handle(client)); // pro anfrage ein eigener thread
            thread.start();
        }
    }

    // beantwortet einen client, läuft in einem eigenen thread
    private static void handle(Socket client) {
        try {
            //1. header des clients auslesen
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            String requestLine = in.readLine(); // wartet auf die erste zeile, z.b. GET /index.html HTTP/1.1
            if (requestLine == null) { // client hat nichts geschickt
                client.close();
                return;
            }
            System.out.println(Thread.currentThread().getName() + ": " + requestLine);

            // 2. pfad aus der ersten zeile holen
            String[] parts = requestLine.split(" "); // ["GET", "/index.html", "HTTP/1.1"]
            if (parts.length < 2) { // zeile hat kein pfad
                client.close();
                return;
            }
            String path = parts[1];

            // 3. nur wenn der pfad stimmt, die index.html schicken, sonst 404
            OutputStream out = client.getOutputStream();
            if (path.equals(PAGE)) {
                File file = new File(ROOT, PAGE);
                send(out, Status.OK, ContentType.HTML, Files.readAllBytes(file.toPath()));
            } else {
                send(out, Status.NOT_FOUND, ContentType.TEXT, "404 nicht gefunden".getBytes(StandardCharsets.UTF_8));
            }

            //4. verbindung schließen
            client.close();
        } catch (IOException e) {
            System.out.println("fehler: " + e.getMessage());
        }
    }


    private static void send(OutputStream out, Status status, ContentType type, byte[] body) throws IOException {
        String temp = "\r\n";
        String header = HTTP_VERSION + " " + status.text + temp
                + "Content-Type: " + type.text + temp
                + "Content-Length: " + body.length + temp
                + "Connection: close" + temp
                + temp;
        out.write(header.getBytes(StandardCharsets.US_ASCII));
        out.write(body);
        out.flush();
    }
}
