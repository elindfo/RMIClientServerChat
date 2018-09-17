package lab2a.client;

import lab2a.server.ChatServer;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ChatClient{

    //TODO Fixa Remote Exception i start() som kallas på då någon annan klient dcar???

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
        Scanner input = null;
        String message = "";
        try{
            input = new Scanner(System.in);
            server.registerForNotification(this);
            do{
                message = input.nextLine();
                if(!message.isEmpty()){
                    if(message.charAt(0) != '/'){ //Not a command
                        server.broadcast(this, message);
                    }
                    else{ //Command
                        String[] splitMessage = message.split(" ");
                        if(splitMessage.length == 1){
                            switch(splitMessage[0]){
                                case "/who": {
                                    server.who(this);
                                    break;
                                }
                                case "/help": {
                                    server.help(this);
                                    break;
                                }
                                case "/quit": {
                                    server.quit(this);
                                    break;
                                }
                                default:{
                                    System.out.println("Invalid command");
                                }
                            }
                        }
                        else if(splitMessage.length == 2){
                            switch(splitMessage[0]){
                                case "/nick": {
                                    server.nick(this, splitMessage[1]);
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid command");
                                }
                            }
                        }
                        else{
                            System.out.println("Invalid command");
                        }
                    }
                }
                System.out.println("Message:" + message);
            }while(!message.equals("/quit"));
            System.out.println("Loop ended");
            //server.deRegisterForNotification(this);
        } catch (ConnectException e) {
            System.err.println("Server not responding");
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally{
            if(input != null){
                input.close();
            }
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
            client.start();
            System.out.println("Exiting...");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
