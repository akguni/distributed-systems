# Blatt 4: Client für den gRPC-Server (eigenes Programm)
# Start (im Ordner andreas/ueb04): .venv/bin/python client.py [host] [port]
import sys

import grpc

import database_pb2
import database_pb2_grpc

TABLE = {
    4101: "Appen",
    4102: "Ahrensburg",
    4103: "Wedel",
    4104: "Aumühle",
    4105: "Seevetal",
    4106: "Quickborn",
}


def get_record(stub, index):
    try:
        return stub.GetRecord(database_pb2.GetRecordRequest(index=index)).record
    except grpc.RpcError as e:
        if e.code() == grpc.StatusCode.NOT_FOUND:
            return "(no record)"
        raise


def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "localhost"
    port = sys.argv[2] if len(sys.argv) > 2 else "50051"

    with grpc.insecure_channel(f"{host}:{port}") as channel:
        stub = database_pb2_grpc.DatabaseStub(channel)   # über den Stub ruft man die Methoden wie lokal auf

        for index, record in TABLE.items():
            stub.AddRecord(database_pb2.AddRecordRequest(index=index, record=record))

        print("GetRecord(4103) =", get_record(stub, 4103))
        print("GetRecord(4107) =", get_record(stub, 4107))
        print("GetSize()       =", stub.GetSize(database_pb2.GetSizeRequest()).size)


if __name__ == "__main__":
    main()
