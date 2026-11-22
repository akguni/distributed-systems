package flynn.HA6.code.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class DataBaseServer {

    private static final String STUB_FILE = "flynn/HA6/stub.bin"; // hier legt der server den stub für den client ab

    public static void main(String[] args) throws IOException {
        DataBaseImpl db = new DataBaseImpl(); // die echte datenbank
        Remote stub = UnicastRemoteObject.exportObject(db, 0); // macht db von außen erreichbar, 0 = freier port
        writeStubToFile(STUB_FILE, stub);
        System.out.println("server läuft, stub liegt in " + STUB_FILE);
    }

    private static void writeStubToFile(String fileName, Remote stub)
            throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream out = new ObjectOutputStream(fos); // wandelt das objekt in bytes um
        out.writeObject(stub);
        out.close();
    }
}
