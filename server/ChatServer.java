package lab2a.server;

import lab2a.client.ChatClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

    public void broadcast(ChatClient fromClient, String message) throws RemoteException;
    public void help(ChatClient fromClient) throws RemoteException;
    public void nick(ChatClient fromClient, String nickname) throws RemoteException;
    public void quit(ChatClient fromClient) throws RemoteException;
    public void who(ChatClient fromClient) throws RemoteException;

    public void registerForNotification(ChatClient client) throws RemoteException;
    public void deRegisterForNotification(ChatClient client) throws RemoteException;
}
