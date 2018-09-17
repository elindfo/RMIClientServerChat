package lab2a.server;

import lab2a.client.ChatClient;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ChatServer{

    private ArrayList<ClientRepresentation> clients;
    private static int clientNo = 0;

    public Server() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized String help() throws RemoteException {
        return null;
    }

    @Override
    public synchronized void nick(String nickname) throws RemoteException {

    }

    @Override
    public synchronized void quit() throws RemoteException {

    }

    @Override
    public synchronized String who() throws RemoteException {
        System.out.println("Server: who() method called");
        return null;
    }

    @Override
    public synchronized void registerForNotification(ChatClient client) throws RemoteException {
        clients.add(new ClientRepresentation(client, clientNo++));
        client.message("Welcome to the server! You are client number " + clientNo);
    }

    @Override
    public synchronized void deRegisterForNotification(ChatClient client) throws RemoteException {
        int clientIndex = 0;
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getClient() == client){
                clientIndex = i;
                break;
            }
        }
        clients.remove(clientIndex);
    }

    public static void main(String[] args) {
        try{
            Server server = new Server();
            Naming.rebind("chatServer", server);
            System.out.println("Chat server started...");
        }catch(RemoteException re){
            System.err.println(re.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
