package andreas.ueb03;

import andreas.ueb03.proto.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Blatt 3, Teil B, Aufgabe 2: Datenbank-Server, nimmt RPC_Requests an und antwortet mit RPC_Response
public class DbServer {
    static final int PORT = 9091;

    // ConcurrentHashMap, weil mehrere Client-Threads gleichzeitig zugreifen können
    static final Map<Integer, String> database = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Database server is running on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handle(clientSocket)).start();
        }
    }

    static void handle(Socket clientSocket) {
        try (clientSocket) {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            RPC_Request request;
            // Verbindung bleibt offen, Client kann mehrere Aufrufe hintereinander schicken
            while ((request = RPC_Request.parseDelimitedFrom(in)) != null) {
                RPC_Response response = execute(request);
                response.writeDelimitedTo(out);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // anhand von operation entscheiden, was ausgeführt wird
    static RPC_Response execute(RPC_Request request) {
        System.out.println("Call: " + request.getOperation());
        switch (request.getOperation()) {
            case GET_RECORD: {
                int index = request.getGetRecordArg().getIndex();
                String record = database.get(index);
                GetRecordResponse result = record == null
                        ? GetRecordResponse.newBuilder().setFound(false).build()
                        : GetRecordResponse.newBuilder().setFound(true).setRecord(record).build();
                return RPC_Response.newBuilder().setGetRecordResult(result).build();
            }
            case ADD_RECORD: {
                AddRecordRequest arg = request.getAddRecordArg();
                String old = database.put(arg.getIndex(), arg.getRecord());
                return RPC_Response.newBuilder()
                        .setAddRecordResult(AddRecordResponse.newBuilder().setReplaced(old != null))
                        .build();
            }
            case GET_SIZE:
                return RPC_Response.newBuilder()
                        .setGetSizeResult(GetSizeResponse.newBuilder().setSize(database.size()))
                        .build();
            default:
                return RPC_Response.newBuilder().setError("Unknown operation: " + request.getOperation()).build();
        }
    }
}
