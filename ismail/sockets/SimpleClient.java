package ismail.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleClient {
    private static final String HOST = "fh-wedel.com";
    private static final String PATH = "/index.html";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String HEADER_HOST = "Host: ";
    private static final String HEADER_CONNECTION_CLOSE = "Connection: close";
    private static final String CRLF = "\r\n";
    private static final int HTTP_PORT = 80;

    public static void main(String[] args) throws Exception {
        try (final Socket socket = new Socket(HOST, HTTP_PORT)) {
            final OutputStream out = socket.getOutputStream();

            final String request =
                    "GET " + PATH + " " + HTTP_VERSION + CRLF +
                            HEADER_HOST + HOST + CRLF +
                            HEADER_CONNECTION_CLOSE + CRLF +
                            CRLF;

            out.write(request.getBytes(StandardCharsets.US_ASCII));
            out.flush();

            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}