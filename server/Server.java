package lab2a.server;

import lab2a.client.ChatClient;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ChatServer{

    private ArrayList<ClientRepresentation> clients;
    private static int clientNo = 0;

    private static final String HELP_MESSAGE =
            "Commands List:\n" +
                    "/who - List other active users\n" +
                    "/nick <nickname> - Set nickname\n" +
                    "/quit - Exit chat\n" +
                    "/help - Display this message";

    public Server() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void broadcast(ChatClient fromClient, String message) throws RemoteException {
        int sendingClient = getClientRepresentationIndexFromChatClient(fromClient);
        String messageToSend = "";
        if(clients.get(sendingClient).getNickname().equals("")){
            messageToSend = "Client " + sendingClient + ": " + message;
        }
        else{
            messageToSend = clients.get(sendingClient).getNickname() + ": " + message;
        }
        int currentClient = 0;
        do {
            try {
                if (!clients.get(currentClient).getClient().equals(fromClient)) {
                    clients.get(currentClient).getClient().message(messageToSend);
                }
                currentClient++;
            } catch (ConnectException ce) {
                deRegisterForNotification(clients.get(currentClient).getClient());
            }
        }while(currentClient < clients.size());
    }

    @Override
    public synchronized void help(ChatClient fromClient) throws RemoteException {
        fromClient.message(HELP_MESSAGE);
    }

    @Override
    public synchronized void nick(ChatClient fromClient, String nickname) throws RemoteException {
        int clientIndex = getClientRepresentationIndexFromChatClient(fromClient);
        boolean nicknameUnavailable = false;
        for(ClientRepresentation cr : clients){
            if(cr.getNickname().equals(nickname)){
                nicknameUnavailable = true;
            }
        }
        if(nicknameUnavailable){
            fromClient.message("Nickname already taken.");
        }
        else{
            String previousNickname = clients.get(clientIndex).getNickname();
            clients.get(clientIndex).setNickname(nickname);
            fromClient.message("Nickname set to: " + nickname);
            if(previousNickname.equals("")){
                broadcast(fromClient, "Client " + clients.get(clientIndex).getClientNo() + " changed nickname to: " + nickname);
            }
            else{

                broadcast(fromClient, previousNickname + " changed nickname to: " + nickname);
            }
        }
    }

    @Override
    public synchronized void quit(ChatClient fromClient) throws RemoteException {
        broadcast(fromClient, "Disconnected");
        //deRegisterForNotification(fromClient);
    }

    @Override
    public synchronized void who(ChatClient fromClient) throws RemoteException {
        StringBuffer sb = new StringBuffer("*****************");
        sb.append("\n");
        sb.append("CLIENTS ONLINE");
        sb.append("\n");
        for(ClientRepresentation cr : clients){
            if(!cr.getClient().equals(fromClient)){
                if(cr.getNickname().equals("")){
                    sb.append("Client ");
                    sb.append(cr.getClientNo());
                }
                else{
                    sb.append(cr.getNickname());
                }
                sb.append("\n");
            }
        }
        sb.append("*****************");
        sb.append("\n");
        fromClient.message(sb.toString());
    }

    @Override
    public synchronized void registerForNotification(ChatClient client) throws RemoteException {
        clients.add(new ClientRepresentation(client, clientNo));
        client.message("Welcome to the server! You are client number " + clientNo);
        broadcast(client, "Client " + clientNo + " connected");
        System.out.println("Client connected: " + clientNo);
        clientNo++;
    }

    @Override
    public synchronized void deRegisterForNotification(ChatClient client) throws RemoteException {
        int clientIndex = 0;
        int disconnectedClient = 0;
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getClient().equals(client)){
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
