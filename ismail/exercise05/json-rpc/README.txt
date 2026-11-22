JSON-RPC DATABASE - EXERCISE 05
================================

This module implements the Exercise 05 database server using JSON-RPC 2.0
over raw TCP sockets. Java API for JSON Processing (JSON-P) is used to parse
and build JSON messages. Records are stored in memory.

REQUIREMENTS

Java 21 or newer
Maven

BUILD

From the exercise05 directory, run:

mvn clean test

PREPARE THE CLASSPATH

From the json-rpc directory, run:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

START THE SERVER

In one terminal:

java -cp "$CP" ismail.exercise05.jsonrpc.database.DatabaseServer 8081

The optional argument is the TCP port. The default is 8081.
The server prints a listening message after startup and reports each RPC it
processes.

The database is stored in memory and is lost when the server stops.

RUN THE CLIENT

With the server running in another terminal, run:

java -cp "$CP" ismail.exercise05.jsonrpc.database.DatabaseClient localhost 8081

The client adds the six exercise records, reads 4103 and 4107, and requests
the database size. The expected output is:

addRecord(4101) succeeded
addRecord(4102) succeeded
addRecord(4103) succeeded
addRecord(4104) succeeded
addRecord(4105) succeeded
addRecord(4106) succeeded
getRecord(4103) -> Wedel
getRecord(4107) reports that no record exists
getSize() -> 6

TESTS

The module contains unit tests for the in-memory database and an integration
test that starts a JSON-RPC server and exchanges JSON-RPC messages over a TCP
socket.

Run the database tests from this directory with:

mvn test

Run the complete JUnit test suite with:

mvn -Dtest=Exercise05TestSuite test

JSON-RPC 2.0 REQUEST AND RESPONSE FORMS
========================================

The Exercise 05 JSON-RPC alternative uses JSON-RPC 2.0 messages. Each request
contains a method name, parameters, and a request id. Responses contain the
same id and either a result or an error.

getRecord
---------

Request:

{"jsonrpc":"2.0","method":"getRecord","params":{"key":4103},"id":1}

Response when the record exists:

{"jsonrpc":"2.0","result":"Wedel","id":1}

Request for a missing key:

{"jsonrpc":"2.0","method":"getRecord","params":{"key":4107},"id":2}

Response:

{"jsonrpc":"2.0","result":null,"id":2}

addRecord
---------

Request:

{"jsonrpc":"2.0","method":"addRecord","params":{"key":4101,"record":"Appen"},"id":3}

Response:

{"jsonrpc":"2.0","result":true,"id":3}

getSize
-------

Request:

{"jsonrpc":"2.0","method":"getSize","params":[],"id":4}

Response:

{"jsonrpc":"2.0","result":6,"id":4}

Invalid parameters or another JSON-RPC failure can be returned as an error:

{"jsonrpc":"2.0","error":{"code":-32602,"message":"Invalid params"},"id":2}

DIRECT JSON-RPC VS. JSON-RPC OVER HTTP
=====================================

Direct JSON-RPC over raw TCP in this exercise sends one JSON-RPC 2.0 object
per line over a Socket connection. The application is responsible for framing
messages, maintaining connections, and closing or flushing the streams.
There is no HTTP request method, status code, URL, or HTTP header. This makes
the protocol smaller and keeps it independent of web infrastructure, but it
does not provide HTTP transport features such as standard proxying, caching,
content negotiation, authentication headers, TLS configuration, or established
HTTP status semantics. It also requires the client and server to agree on
socket framing.

JSON-RPC over HTTP is carried in an HTTP POST request, typically with a JSON
content type, and the JSON-RPC response is sent as the HTTP response body.
HTTP provides familiar transport conventions, intermediaries, and security
mechanisms, while JSON-RPC still defines the JSON message structure and
method semantics. It can be accessed by normal web tools and browser-capable
clients. It also adds HTTP overhead and is usually request-response based,
unless HTTP streaming or persistent connections are used separately.
