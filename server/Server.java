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
    public synchronized void sendMessage(ChatClient client, String message) throws RemoteException {
        int sendingClient = getClientRepresentationIndexFromChatClient(client);
        String messageToSend = "Client " + sendingClient + ": " + message;
        for(ClientRepresentation cr : clients){
            if(cr.getClient() != client){
                cr.getClient().message(messageToSend);
            }
        }
    }

    @Override
    public synchronized String help(ChatClient client) throws RemoteException {
        return null;
    }

    @Override
    public synchronized void nick(ChatClient client, String nickname) throws RemoteException {

    }

    @Override
    public synchronized void quit(ChatClient client) throws RemoteException {

    }

    @Override
    public synchronized String who(ChatClient client) throws RemoteException {
        System.out.println("Server: who() method called");
        return null;
    }

    @Override
    public synchronized void registerForNotification(ChatClient client) throws RemoteException {
        clients.add(new ClientRepresentation(client, clientNo));
        client.message("Welcome to the server! You are client number " + clientNo);
        System.out.println("Client connected: " + clientNo);
        clientNo++;
    }

    @Override
    public synchronized void deRegisterForNotification(ChatClient client) throws RemoteException {
        int clientIndex = 0;
        int disconnectedClient = 0;
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getClient() == client){
                clientIndex = i;
                disconnectedClient = clients.get(i).getClientNo();
                break;
            }
        }
        clients.remove(clientIndex);
        System.out.println("Client disconnected: " + disconnectedClient);
    }

    private int getClientRepresentationIndexFromChatClient(ChatClient client){
        System.out.println("getClientRepresentationIndexFromChatClient");
        for(int i = 0; i < clients.size(); i++){
            System.out.println("stored:"+clients.get(i).getClient()+" client:"+client);
            if(clients.get(i).getClient().equals(client)){
                return i;
            }
        }
        return -1;
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
