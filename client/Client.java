package lab2a.client;

import lab2a.server.ChatServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ChatClient{

    private ChatServer server;

    public Client(ChatServer server) throws RemoteException {
        super();
        this.server = server;
    }

    @Override
    public void message(String message) throws RemoteException {
        System.out.println(message);
    }

    public void start(){
        Scanner input = new Scanner(System.in);
        int choice;
        try{

            do{
                choice = input.nextInt();
                switch(choice){
                    case 1: {
                        server.who();
                        break;
                    }
                }
            }while(choice != 0);
            server.deRegisterForNotification(this);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        if(args.length != 1){
            System.err.println("Invalid argument. Usage: java Client hostaddress");
            System.exit(0);
        }

        try{
            ChatServer server = (ChatServer) Naming.lookup("rmi://" + args[0] + "/chatServer");
            Client client = new Client(server);
            server.registerForNotification(client);
            client.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
