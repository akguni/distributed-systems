package ismail.exercise06.javarmi.database;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DataBaseImpl extends UnicastRemoteObject implements DataBase {
    private final Map<Integer, String> records = new HashMap<>();

    public DataBaseImpl() throws RemoteException {
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
}
