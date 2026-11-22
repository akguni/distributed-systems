package flynn.HA6.code.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDataBase extends Remote { // Remote: methoden dürfen von einem anderen programm aufgerufen werden

    public String getRecord(int index) throws RemoteException; // null, wenn es den eintrag nicht gibt

    public void addRecord(int index, String record) throws RemoteException;

    public int getSize() throws RemoteException;

    public int getIndex(String record) throws RemoteException; // -1, wenn es den eintrag nicht gibt

    public DBResult getRecordObj(int index) throws RemoteException; // null, wenn es den eintrag nicht gibt
}
