package flynn.HA3.aufgabe2.code;

import flynn.HA3.aufgabe2.code.generated.RpcRequest;
import flynn.HA3.aufgabe2.code.generated.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RpcClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9091;

    private static OutputStream out;
    private static InputStream in;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT); // wartet, bis die verbindung steht
        out = socket.getOutputStream();
        in = socket.getInputStream();

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
        call(RpcRequest.newBuilder()
                .setOperation(RpcRequest.Operation.ADD_RECORD)
                .setIndex(index)
                .setRecord(record)
                .build());
    }

    private static String getRecord(int index) throws IOException {
        RpcResponse response = call(RpcRequest.newBuilder()
                .setOperation(RpcRequest.Operation.GET_RECORD)
                .setIndex(index)
                .build());
        return response.getOk() ? response.getRecord() : "nicht gefunden";
    }

    private static int getSize() throws IOException {
        return call(RpcRequest.newBuilder()
                .setOperation(RpcRequest.Operation.GET_SIZE)
                .build()).getSize();
    }

    private static RpcResponse call(RpcRequest request) throws IOException {
        request.writeDelimitedTo(out);
        out.flush();
        return RpcResponse.parseDelimitedFrom(in); // wartet auf die antwort
    }
}
