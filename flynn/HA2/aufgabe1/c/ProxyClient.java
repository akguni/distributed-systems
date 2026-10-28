package flynn.HA2.aufgabe1.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ProxyClient {

    private static final String URL_TEXT = "http://httpbin.org/stream/5";
    private static final String PROXY_HOST = "localhost"; // proxy.fh-wedel.de
    private static final int PROXY_PORT = 3128; // üblicher proxy-port

    public static void main(String[] args) throws IOException {
        URL url = URI.create(URL_TEXT).toURL();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)); // adresse und port des proxys
        URLConnection connection = url.openConnection(proxy); // noch nichts gesendet, nur vorbereitet

        InputStream rawIn = connection.getInputStream(); // verbindet zum proxy, sendet die anfrage und wartet auf die antwort
        InputStreamReader reader = new InputStreamReader(rawIn, StandardCharsets.UTF_8); // bytes -> zeichen
        BufferedReader in = new BufferedReader(reader); // zeichen -> ganze zeilen
        String line;
        while ((line = in.readLine()) != null) { // wartet auf die nächste zeile, null = server hat geschlossen
            System.out.println(line);
        }
        in.close();
    }
}
