package flynn.HA2.aufgabe1.b;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlClient {

    private static final String URL_TEXT = "http://httpbin.org/stream/5";

    public static void main(String[] args) throws IOException {
        URL url = URI.create(URL_TEXT).toURL();

        InputStream rawIn = url.openStream(); // verbindet, sendet die anfrage und wartet auf die antwort
        InputStreamReader reader = new InputStreamReader(rawIn, StandardCharsets.UTF_8); // bytes -> zeichen
        BufferedReader in = new BufferedReader(reader); // zeichen -> ganze zeilen
        String line;
        while ((line = in.readLine()) != null) { // wartet auf die nächste zeile, null = server hat geschlossen
            System.out.println(line);
        }
        in.close();
    }
}
