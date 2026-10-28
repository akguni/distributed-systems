package flynn.HA2.aufgabe1.a; // wie namespace in c#

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class SocketClient {

    private static final String HOST = "localhost"; //httpbin.org //stud.fh-wedel.de
    private static final String PATH = "/index.html"; ///stream/5
    private static final int PORT = 8080; //80

    public static void main(String[] args) throws IOException {
        String request = "GET " + PATH + " HTTP/1.1\r\n"
                + "Host: " + HOST + "\r\n"
                + "Connection: close\r\n"
                + "\r\n"; // leere zeile = ende der anfrage

        Socket socket = new Socket(HOST, PORT); // wartet, bis die verbindung steht
        System.out.println("verbunden");
        System.out.println(request);

        OutputStream out = socket.getOutputStream(); // mit dieser instanz kann man informationen zum server schicken
        byte[] requestBytes = request.getBytes(StandardCharsets.US_ASCII);
        out.write(requestBytes);
        out.flush(); // alles senden, was vorher geschrieben wurde

        InputStream rawIn = socket.getInputStream(); // mit dieser instanz kann man informationen vom server empfangen
        InputStreamReader reader = new InputStreamReader(rawIn, StandardCharsets.UTF_8); // bytes -> zeichen
        BufferedReader in = new BufferedReader(reader); // zeichen -> ganze zeilen    (BufferedReader kann zeile für zeile lesen mit in.readLine())
        String line;
        while ((line = in.readLine()) != null) { // wartet auf die nächste zeile, null = server hat geschlossen
            System.out.println(line);
        }

        socket.close(); // verbindung schließen
    }
}
