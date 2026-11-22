package flynn.HA5.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServer {

    private static final int PORT = 9091;
    private static final Map<Integer, String> records = new ConcurrentHashMap<>(); // index -> eintrag

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("server läuft auf port " + PORT);

        while (true) {
            Socket client = serverSocket.accept(); // wartet, bis sich ein client verbindet
            new Thread(() -> handle(client)).start(); // jeder client bekommt einen eigenen thread
        }
    }

    private static void handle(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true); // true = nach jeder zeile senden
            String request;
            while ((request = in.readLine()) != null) { // wartet auf die nächste anfrage (eine zeile), null = client hat geschlossen
                out.println(process(request));
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String process(String request) {
        String id = "null"; // bei fehlerhafter anfrage kennt der server die id nicht
        try {
            String idField = Json.getValue(request, "id");
            if (idField != null) {
                id = idField;
            }
            String methodField = Json.getValue(request, "method");
            if (methodField == null) {
                return JsonRpc.error(-32600, "Invalid Request", id);
            }
            String params = params(request);
            switch (Json.unquote(methodField)) {
                case "getRecord": {
                    String record = records.get(Integer.parseInt(params.trim()));
                    return JsonRpc.result(record == null ? "null" : Json.quote(record), id);
                }
                case "addRecord": {
                    int comma = params.indexOf(','); // trennt index und eintrag
                    records.put(Integer.parseInt(params.substring(0, comma).trim()), Json.unquote(params.substring(comma + 1).trim()));
                    return JsonRpc.result("true", id);
                }
                case "getSize":
                    return JsonRpc.result(String.valueOf(records.size()), id);
                default:
                    return JsonRpc.error(-32601, "Method not found", id);
            }
        } catch (NumberFormatException e) {
            return JsonRpc.error(-32602, "Invalid params", id);
        } catch (RuntimeException e) {
            return JsonRpc.error(-32700, "Parse error", "null");
        }
    }

    // holt alles zwischen den eckigen klammern von params
    private static String params(String request) {
        int start = request.indexOf('[', request.indexOf("\"params\""));
        int end = request.lastIndexOf(']');
        return request.substring(start + 1, end);
    }
}
