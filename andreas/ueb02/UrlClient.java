package andreas.ueb02;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class UrlClient {

    public static void main(String[] args) throws Exception {
        String address = args.length > 0 ? args[0] : "http://example.com/";

        URL url = URI.create(address).toURL();

        try {
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
