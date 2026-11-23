package ismail.exercise06.javarmi.database;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DataBaseImpl implements DataBase {
    private static final String DEFAULT_STUB_FILE =
            "database-stub.bin";

    private final Map<Integer, String> records = new HashMap<>();

    public DataBaseImpl() {
    }

    @Override
    public synchronized String getRecord(int index) throws RemoteException {
        return records.get(index);
    }

    @Override
    public synchronized void addRecord(int index, String record)
            throws RemoteException {
        records.put(index, record);
    }

    @Override
    public synchronized int getSize() throws RemoteException {
        return records.size();
    }

    public static void main(String[] args) throws Exception {
        final String stubFile = args.length == 1
                ? args[0]
                : DEFAULT_STUB_FILE;
        final DataBaseImpl database = new DataBaseImpl();
        final Remote stub =
                UnicastRemoteObject.exportObject(database, 0);
        writeStubToFile(stubFile, stub);
        System.out.println("Database server is running.");
        System.out.println("Stub written to " + stubFile);

        Thread.currentThread().join();
    }

    private static void writeStubToFile(String fileName, Remote stub)
            throws FileNotFoundException , IOException {
        FileOutputStream fos = new FileOutputStream (fileName);
        ObjectOutputStream out = new ObjectOutputStream (fos);
        out.writeObject (stub);
        out.close();
    }
}
