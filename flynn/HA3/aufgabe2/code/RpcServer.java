package flynn.HA3.aufgabe2.code;

import flynn.HA3.aufgabe2.code.generated.RpcRequest;
import flynn.HA3.aufgabe2.code.generated.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            RpcRequest request;
            while ((request = RpcRequest.parseDelimitedFrom(in)) != null) { // wartet auf die nächste anfrage, null = client hat geschlossen
                process(request).writeDelimitedTo(out);
                out.flush();
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static RpcResponse process(RpcRequest request) {
        RpcResponse.Builder response = RpcResponse.newBuilder();
        switch (request.getOperation()) {
            case GET_RECORD:
                String record = records.get(request.getIndex());
                if (record != null) {
                    response.setOk(true).setRecord(record);
                }
                break;
            case ADD_RECORD:
                records.put(request.getIndex(), request.getRecord());
                response.setOk(true);
                break;
            case GET_SIZE:
                response.setOk(true).setSize(records.size());
                break;
            default:
                break;
        }
        return response.build();
    }
}
