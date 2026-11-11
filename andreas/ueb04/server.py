# Blatt 4: Datenbank-Server mit gRPC
# Start (im Ordner andreas/ueb04): .venv/bin/python server.py
from concurrent import futures
import threading

import grpc

import database_pb2
import database_pb2_grpc

PORT = 50051


# die drei Methoden aus database.proto, Sockets und Serialisierung macht gRPC
class DatabaseService(database_pb2_grpc.DatabaseServicer):

    def __init__(self):
        self.records = {}
        self.lock = threading.Lock()   # Server läuft mit Thread-Pool, also Zugriff absichern

    def GetRecord(self, request, context):
        with self.lock:
            record = self.records.get(request.index)
        if record is None:
            # nicht gefunden -> Statuscode NOT_FOUND (in Blatt 3 war das noch das found-Feld)
            context.abort(grpc.StatusCode.NOT_FOUND, f"no record with index {request.index}")
        return database_pb2.GetRecordResponse(record=record)

    def AddRecord(self, request, context):
        with self.lock:
            replaced = request.index in self.records
            self.records[request.index] = request.record
        return database_pb2.AddRecordResponse(replaced=replaced)

    def GetSize(self, request, context):
        with self.lock:
            return database_pb2.GetSizeResponse(size=len(self.records))


def main():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    database_pb2_grpc.add_DatabaseServicer_to_server(DatabaseService(), server)
    server.add_insecure_port(f"[::]:{PORT}")   # ohne TLS
    server.start()
    print(f"gRPC database server is running on port {PORT}", flush=True)
    server.wait_for_termination()


if __name__ == "__main__":
    main()
