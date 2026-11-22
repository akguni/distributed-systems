@echo off
cd /d "%~dp0..\..\Gruppe\distributed-systems"
echo Starte: Java-Code aus Rpc.proto erzeugen ...
"%~dp0..\protoc\bin\protoc.exe" --proto_path=flynn/HA3/aufgabe2/proto --java_out=. flynn/HA3/aufgabe2/proto/Rpc.proto
if errorlevel 1 (
    echo FEHLER: Code wurde nicht erzeugt, siehe Meldung oben.
) else (
    echo FERTIG: Code liegt in flynn\HA3\aufgabe2\code\generated
)
pause
