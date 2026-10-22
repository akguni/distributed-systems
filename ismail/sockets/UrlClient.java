package ismail.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class UrlClient {
    private static final String SCHEME = "http://";
    private static final String HOST = "fh-wedel.de";
    private static final String PATH = "/index.html";
    private static final String REQUEST_URL = SCHEME + HOST + PATH;

    public static void main(String[] args) throws Exception {
        final URL url = new URL(REQUEST_URL);
        final URLConnection connection = url.openConnection();

        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}