package flynn.HA5.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RpcClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9091;

    private static BufferedReader in;
    private static PrintWriter out;
    private static int nextId = 1; //  anfrage id

    private static boolean showJson = false;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT); // wartet, bis die verbindung steht
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true); // true = nach jeder zeile senden

        addRecord(4101, "Appen");
        addRecord(4102, "Ahrensburg");
        addRecord(4103, "Wedel");
        addRecord(4104, "Aumühle");
        addRecord(4105, "Seevetal");
        addRecord(4106, "Quickborn");

        System.out.println("4103: " + getRecord(4103));
        System.out.println("4107: " + getRecord(4107));
        System.out.println("größe: " + getSize());

        socket.close();
    }

    private static void addRecord(int index, String record) throws IOException {
        call("addRecord", index + "," + Json.quote(record));
    }

    private static String getRecord(int index) throws IOException {
        String result = call("getRecord", String.valueOf(index));
        return result.equals("null") ? "nicht gefunden" : Json.unquote(result);
    }

    private static int getSize() throws IOException {
        return Integer.parseInt(call("getSize", ""));
    }

    // schickt eine anfrage und gibt den rohen wert von result zurück
    private static String call(String method, String params) throws IOException {
        String request = JsonRpc.request(method, params, nextId++);
        if(showJson){
            System.out.println("-> " + request);
        }
        out.println(request);
        String response = in.readLine(); // wartet auf die antwort
        if(showJson){
            System.out.println("<- " + response);
        }
        String result = Json.getValue(response, "result");
        if (result == null) { // keine result, also steht ein error in der antwort
            throw new IOException("fehler vom server: " + response);
        }
        return result;
    }
}
