package ismail.sockets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleFileServer {
    private static final int PORT = 8080;

    private static final String DOCUMENT_ROOT = "public";
    private static final String SPACE = " ";
    private static final String GET_METHOD = "GET";
    private static final String EMPTY = "";
    private static final String CRLF = "\r\n";
    private static final String STATUS_OK = "HTTP/1.1 200 OK";
    private static final String CONTENT_TYPE_HEADER = "Content-Type: application/octet-stream";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length: ";
    private static final String CONNECTION_HEADER = "Connection: close";

    private static final Path ROOT =
            Path.of(DOCUMENT_ROOT).toAbsolutePath().normalize();

    public static void main(String[] args) throws IOException {
        Files.createDirectories(ROOT);

        try (final ServerSocket server = new ServerSocket(
                PORT, 50, InetAddress.getLoopbackAddress())) {
            while (true) {
                try (Socket client = server.accept()) {
                    handle(client);
                }
            }
        }
    }

    private static void handle(Socket client) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(
                client.getInputStream(), StandardCharsets.US_ASCII));

        final String requestLine = in.readLine();
        if (requestLine == null) return;

        final String[] parts = requestLine.split(SPACE, 3);
        if (parts.length < 2 || !parts[0].equals(GET_METHOD)) return;

        final String requestPath = URI.create(parts[1]).getPath();
        final Path file = ROOT.resolve(requestPath.substring(1)).normalize();

        if (!file.startsWith(ROOT) || !Files.isRegularFile(file)) return;

        String line;
        while ((line = in.readLine()) != null && !line.equals(EMPTY)) {
            // Headers are not relevant.
        }

        final byte[] body = Files.readAllBytes(file);
        final OutputStream out = client.getOutputStream();

        final String headers = STATUS_OK + CRLF
                + CONTENT_TYPE_HEADER + CRLF
                + CONTENT_LENGTH_HEADER + body.length + CRLF
                + CONNECTION_HEADER + CRLF
                + CRLF;

        out.write(headers.getBytes(StandardCharsets.US_ASCII));
        out.write(body);
    }
}