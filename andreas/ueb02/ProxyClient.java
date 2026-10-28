package andreas.ueb02;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

// Exercise 02, Aufgabe 1c: Wie berücksichtigt der Client einen Proxy-Server?
// Aufruf: ProxyClient <proxyHost> <proxyPort> [url]
public class ProxyClient {

    public static void main(String[] args) throws Exception {
        String proxyHost = args.length > 0 ? args[0] : "localhost";
        int proxyPort = args.length > 1 ? Integer.parseInt(args[1]) : 8888;
        String address = args.length > 2 ? args[2] : "http://example.com/";

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        URL url = URI.create(address).toURL();
        URLConnection urlConnection = url.openConnection(proxy);
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
