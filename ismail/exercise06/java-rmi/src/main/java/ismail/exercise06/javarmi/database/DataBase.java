package ismail.exercise06.javarmi.database;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataBase extends Remote {
    public String getRecord ( int index ) throws RemoteException ;
    public void addRecord ( int index , String record ) throws RemoteException;
    public int getSize () throws RemoteException ;
}
