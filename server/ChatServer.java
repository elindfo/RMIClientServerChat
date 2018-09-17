package lab2a.server;

import lab2a.client.ChatClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

    public void sendMessage(ChatClient client, String message) throws RemoteException;
    public String help(ChatClient client) throws RemoteException;
    public void nick(ChatClient client, String nickname) throws RemoteException;
    public void quit(ChatClient client) throws RemoteException;
    public String who(ChatClient client) throws RemoteException;

    public void registerForNotification(ChatClient client) throws RemoteException;
    public void deRegisterForNotification(ChatClient client) throws RemoteException;
}
