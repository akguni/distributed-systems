package andreas.ueb03;

import andreas.ueb03.proto.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// Blatt 3, Teil B, Aufgaben 3-5: Client für den DbServer (läuft als eigenes Programm)
public class DbClient {
    static OutputStream out;
    static InputStream in;

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 9091;

        try (Socket socket = new Socket(host, port)) {
            out = socket.getOutputStream();
            in = socket.getInputStream();

            // Aufgabe 4: Tabelle vom Blatt eintragen
            addRecord(4101, "Appen");
            addRecord(4102, "Ahrensburg");
            addRecord(4103, "Wedel");
            addRecord(4104, "Aumühle");
            addRecord(4105, "Seevetal");
            addRecord(4106, "Quickborn");

            // 4103 lesen, 4107 gibt es nicht
            System.out.println("getRecord(4103) = " + getRecord(4103));
            System.out.println("getRecord(4107) = " + getRecord(4107));

            // Aufgabe 5: sollte 6 sein
            System.out.println("getSize()       = " + getSize());
        }
    }

    static void addRecord(int index, String record) throws Exception {
        call(RPC_Request.newBuilder()
                .setOperation(RPC_Request.Operation.ADD_RECORD)
                .setAddRecordArg(AddRecordRequest.newBuilder().setIndex(index).setRecord(record))
                .build());
    }

    static String getRecord(int index) throws Exception {
        RPC_Response response = call(RPC_Request.newBuilder()
                .setOperation(RPC_Request.Operation.GET_RECORD)
                .setGetRecordArg(GetRecordRequest.newBuilder().setIndex(index))
                .build());
        GetRecordResponse result = response.getGetRecordResult();
        return result.getFound() ? result.getRecord() : "(no record)";
    }

    static int getSize() throws Exception {
        RPC_Response response = call(RPC_Request.newBuilder()
                .setOperation(RPC_Request.Operation.GET_SIZE)
                .setGetSizeArg(GetSizeRequest.getDefaultInstance())
                .build());
        return response.getGetSizeResult().getSize();
    }

    // Request schicken und auf die Antwort warten
    static RPC_Response call(RPC_Request request) throws Exception {
        request.writeDelimitedTo(out);
        out.flush();
        RPC_Response response = RPC_Response.parseDelimitedFrom(in);
        if (response.hasError()) {
            throw new RuntimeException(response.getError());
        }
        return response;
    }
}
