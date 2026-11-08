package flynn.HA4.code;

import flynn.HA4.code.generated.AddRecordRequest;
import flynn.HA4.code.generated.AddRecordResponse;
import flynn.HA4.code.generated.DatabaseGrpc;
import flynn.HA4.code.generated.GetRecordRequest;
import flynn.HA4.code.generated.GetRecordResponse;
import flynn.HA4.code.generated.GetSizeRequest;
import flynn.HA4.code.generated.GetSizeResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseServer {

    private static final int PORT = 9092;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new DatabaseService())
                .build()
                .start();
        System.out.println("server läuft auf port " + PORT);

        server.awaitTermination(); // wartet, bis der server beendet wird
    }

    private static class DatabaseService extends DatabaseGrpc.DatabaseImplBase {

        private final Map<Integer, String> records = new ConcurrentHashMap<>(); // index -> eintrag

        @Override
        public void getRecord(GetRecordRequest request, StreamObserver<GetRecordResponse> responseObserver) {
            String record = records.get(request.getIndex());
            GetRecordResponse.Builder response = GetRecordResponse.newBuilder();
            if (record != null) {
                response.setFound(true).setRecord(record);
            }
            responseObserver.onNext(response.build()); // schickt die antwort
            responseObserver.onCompleted(); // beendet den aufruf
        }

        @Override
        public void addRecord(AddRecordRequest request, StreamObserver<AddRecordResponse> responseObserver) {
            records.put(request.getIndex(), request.getRecord());
            responseObserver.onNext(AddRecordResponse.getDefaultInstance());
            responseObserver.onCompleted();
        }

        @Override
        public void getSize(GetSizeRequest request, StreamObserver<GetSizeResponse> responseObserver) {
            responseObserver.onNext(GetSizeResponse.newBuilder().setSize(records.size()).build());
            responseObserver.onCompleted();
        }
    }
}
