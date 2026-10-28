package andreas.ueb02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServer {
    static final int PORT = 8080;
    static final String ROOT = "andreas/ueb02/www";   // relativ zum Repo-Ordner

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is running on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handle(clientSocket)).start();
        }
    }

    static void handle(Socket clientSocket) {
        try (clientSocket) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            String requestLine = reader.readLine();

            if (requestLine == null) {
                return;
            }

            String path = requestLine.split(" ")[1];
            path = path.equals("/") ? "/index.html" : path;
            System.out.println("Path: " + path);

            // Abwehr gegen Path Traversal (GET /../../etc/passwd)
            Path root = Path.of(ROOT).toAbsolutePath().normalize();
            Path file = root.resolve(path.substring(1)).normalize();

            if (!file.startsWith(root)) {
                send(out, "403 Forbidden", "403 - Zugriff verweigert: " + path);
                return;
            }

            if (!Files.exists(file)) {
                send(out, "404 Not Found", "404 - Datei nicht gefunden: " + path);
                return;
            }

            send(out, "200 OK", Files.readAllBytes(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void send(OutputStream out, String status, String text) throws IOException {
        send(out, status, text.getBytes());
    }

    static void send(OutputStream out, String status, byte[] body) throws IOException {
        String header = "HTTP/1.1 " + status + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";
        out.write(header.getBytes());
        out.write(body);
    }
}
