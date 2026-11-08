package ismail.exercise04.database;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import ismail.exercise04.database.protocol.AddRecordResponse;
import ismail.exercise04.database.protocol.DatabaseServiceGrpc;
import ismail.exercise04.database.protocol.GetRecordResponse;
import ismail.exercise04.database.protocol.GetSizeResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseClientServerTest {
    @Test
    void clientAndServerExchangeAllOperations() throws Exception {
        final Server server = DatabaseServer.start(0, new InMemoryDatabase());
        final ManagedChannel channel = DatabaseClient.channel(
                "localhost", server.getPort());
        try {
            final DatabaseServiceGrpc.DatabaseServiceBlockingStub stub =
                    DatabaseServiceGrpc.newBlockingStub(channel);
            final AddRecordResponse addResponse =
                    DatabaseClient.addRecord(stub, 4103, "Wedel");
            final GetRecordResponse presentResponse =
                    DatabaseClient.getRecord(stub, 4103);
            final GetRecordResponse missingResponse =
                    DatabaseClient.getRecord(stub, 4107);
            final GetSizeResponse sizeResponse = DatabaseClient.getSize(stub);

            assertTrue(addResponse.getAdded());
            assertTrue(presentResponse.getFound());
            assertEquals("Wedel", presentResponse.getRecord());
            assertFalse(missingResponse.getFound());
            assertEquals(1, sizeResponse.getSize());
        } finally {
            channel.shutdownNow();
            server.shutdownNow();
        }
    }
}
