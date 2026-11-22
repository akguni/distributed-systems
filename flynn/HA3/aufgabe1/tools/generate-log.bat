@echo off
cd /d "%~dp0..\..\Gruppe\distributed-systems"
echo Starte: Java-Code aus LogMessage.proto erzeugen ...
"%~dp0..\protoc\bin\protoc.exe" --proto_path=flynn/HA3/aufgabe1/proto --java_out=. flynn/HA3/aufgabe1/proto/LogMessage.proto
if errorlevel 1 (
    echo FEHLER: Code wurde nicht erzeugt, siehe Meldung oben.
) else (
    echo FERTIG: Code liegt in flynn\HA3\aufgabe1\code\generated
)
pause
