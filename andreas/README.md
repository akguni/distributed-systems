# Distributed Systems: Blatt 3 und 4 (Andreas)

Alle Befehle werden vom **Repo-Root** (`ds-gruppe/`) aus gestartet, außer bei Blatt 4.

## Voraussetzungen

- Java (getestet mit OpenJDK 27)
- protoc 36.2 (`brew install protobuf`), nur nötig, wenn man die `.proto`-Dateien ändert
- protobuf-java 4.36.2 als Jar. Jars sind per `.gitignore` nicht im Repo, also einmal herunterladen:

```bash
mkdir -p andreas/ueb03/lib
curl -L -o andreas/ueb03/lib/protobuf-java-4.36.2.jar https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/4.36.2/protobuf-java-4.36.2.jar
```

- Für Blatt 4: Python **3.10 oder neuer** (mit dem Python 3.9 von macOS lässt sich grpcio 1.85 nicht installieren)

## Blatt 3: Protocol Buffers

### Teil A: Log-Server

`LogMessage.proto` enthält Zeitstempel, Herkunft, Level und Text. Laut Blatt ist die Herkunft optional. Das
heißt, das Feld darf in einer Nachricht fehlen, deshalb steht `optional Origin origin` in der proto-Datei und der
Server prüft mit `hasOrigin()`. Der Client schickt vier Nachrichten mit Herkunft und eine ohne, im Log steht dann
`-` statt Host/Programm.

Kompilieren:

```bash
javac -cp andreas/ueb03/lib/protobuf-java-4.36.2.jar -d out andreas/ueb03/*.java andreas/ueb03/proto/*.java
```

Server starten (Terminal 1):

```bash
java -cp andreas/ueb03/lib/protobuf-java-4.36.2.jar:out andreas.ueb03.LogServer
```

Client starten (Terminal 2):

```bash
java -cp andreas/ueb03/lib/protobuf-java-4.36.2.jar:out andreas.ueb03.LogClient
```

Ergebnis steht in `andreas/ueb03/server.log`, eine Zeile pro Nachricht:

```
2026-11-10T15:32:16.994665Z | INFO  | Mac.fritz.box/LogClient | Client started
...
2026-11-10T15:32:17.002018Z | FATAL | - | Shutting down
```

### Teil B: Datenbank-Server (RPC von Hand)

`Database.proto`: `RPC_Request` hat ein Enum `operation` und die Parameter der Operationen in einem `oneof`, weil
pro Aufruf immer nur einer gesetzt ist. `RPC_Response` enthält das Ergebnis oder eine Fehlermeldung.

Server (Terminal 1, Port 9091):

```bash
java -cp andreas/ueb03/lib/protobuf-java-4.36.2.jar:out andreas.ueb03.DbServer
```

Client (Terminal 2):

```bash
java -cp andreas/ueb03/lib/protobuf-java-4.36.2.jar:out andreas.ueb03.DbClient
```

Erwartete Ausgabe:

```
getRecord(4103) = Wedel
getRecord(4107) = (no record)
getSize()       = 6
```

**Aufgabe 2, Socket oder HTTP:** Direkt über den Socket schicken wir nur die Protobuf-Bytes mit Längenpräfix, das
ist schlank, aber wir müssen Framing, Verbindung und Fehler selbst regeln. Mit HTTP kommt ein Header dazu
(Content-Type, Content-Length, Statuscodes), dafür geht es durch Proxys und Firewalls und man kann vorhandene
Server und Bibliotheken nutzen. Die Länge steht dann im Header, das Framing erledigt HTTP.

### Code neu erzeugen (nur nach Änderungen an den .proto-Dateien)

```bash
protoc --proto_path=andreas/ueb03 --java_out=. andreas/ueb03/LogMessage.proto andreas/ueb03/Database.proto
```

## Blatt 4: gRPC (Python)

Gleicher Datenbank-Server wie in Blatt 3, aber die Schnittstelle ist ein `service` in `database.proto`. Wird ein
Index nicht gefunden, antwortet der Server mit dem Statuscode `NOT_FOUND`.

Einmalig die Umgebung anlegen. Vorher mit `python3 --version` prüfen, ob es mindestens 3.10 ist, sonst z. B.
`python3.12` statt `python3` nehmen:

```bash
cd andreas/ueb04
python3 -m venv .venv
.venv/bin/pip install -r requirements.txt
```

Server (Terminal 1, Port 50051):

```bash
cd andreas/ueb04
.venv/bin/python server.py
```

Client (Terminal 2):

```bash
cd andreas/ueb04
.venv/bin/python client.py
```

Optional mit Host und Port: `.venv/bin/python client.py localhost 50051`. Die Ausgabe ist dieselbe wie bei Blatt 3.

Code neu erzeugen (nur nach Änderungen an `database.proto`):

```bash
cd andreas/ueb04
.venv/bin/python -m grpc_tools.protoc -I. --python_out=. --pyi_out=. --grpc_python_out=. database.proto
```

## Ports

| Programm | Port |
|---|---|
| LogServer | 9090 |
| DbServer | 9091 |
| gRPC-Server | 50051 |

Läuft schon etwas auf dem Port, bricht der Server mit `Address already in use` ab. Dann den alten Prozess beenden
(`lsof -i :9091` zeigt ihn).
