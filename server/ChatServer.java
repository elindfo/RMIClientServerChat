package lab2a.server;

import lab2a.client.ChatClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

    public String help() throws RemoteException;
    public void nick(String nickname) throws RemoteException;
    public void quit() throws RemoteException;
    public String who() throws RemoteException;

    public void registerForNotification(ChatClient client) throws RemoteException;
    public void deRegisterForNotification(ChatClient client) throws RemoteException;
}
