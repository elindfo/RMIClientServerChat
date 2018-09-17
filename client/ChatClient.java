package lab2a.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    public void message(String message) throws RemoteException;
    public void poke() throws RemoteException;
}
