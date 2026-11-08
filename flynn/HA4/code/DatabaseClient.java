package flynn.HA4.code;

import flynn.HA4.code.generated.AddRecordRequest;
import flynn.HA4.code.generated.DatabaseGrpc;
import flynn.HA4.code.generated.GetRecordRequest;
import flynn.HA4.code.generated.GetRecordResponse;
import flynn.HA4.code.generated.GetSizeRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DatabaseClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9092;

    private static DatabaseGrpc.DatabaseBlockingStub stub;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext() // ohne verschlüsselung
                .build();
        stub = DatabaseGrpc.newBlockingStub(channel); // jeder aufruf wartet auf die antwort

        addRecord(4101, "Appen");
        addRecord(4102, "Ahrensburg");
        addRecord(4103, "Wedel");
        addRecord(4104, "Aumühle");
        addRecord(4105, "Seevetal");
        addRecord(4106, "Quickborn");

        System.out.println("4103: " + getRecord(4103));
        System.out.println("4107: " + getRecord(4107));
        System.out.println("größe: " + getSize());

        channel.shutdown();
    }

    private static void addRecord(int index, String record) {
        stub.addRecord(AddRecordRequest.newBuilder()
                .setIndex(index)
                .setRecord(record)
                .build());
    }

    private static String getRecord(int index) {
        GetRecordResponse response = stub.getRecord(GetRecordRequest.newBuilder()
                .setIndex(index)
                .build());
        return response.getFound() ? response.getRecord() : "nicht gefunden";
    }

    private static int getSize() {
        return stub.getSize(GetSizeRequest.getDefaultInstance()).getSize();
    }
}
