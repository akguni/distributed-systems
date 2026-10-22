package ismail.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ProxyClient {
    private static final String SCHEME = "http://";
    private static final String HOST = "fh-wedel.de";
    private static final String PATH = "/index.html";
    private static final String REQUEST_URL = SCHEME + HOST + PATH;

    private static final String PROXY_HOST = "proxy.example.com";
    private static final int PROXY_PORT = 8080;

    public static void main(String[] args) throws Exception {
        final Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(PROXY_HOST, PROXY_PORT));

        final URL url = new URL(REQUEST_URL);
        final URLConnection connection = url.openConnection(proxy);
        connection.setConnectTimeout(5_000);
        connection.setReadTimeout(5_000);

        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}