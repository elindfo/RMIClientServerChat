package lab2a.server;

import lab2a.client.ChatClient;

public class ClientRepresentation {

    private ChatClient client;
    private String nickname;
    private int clientNo;

    public ClientRepresentation(ChatClient client, int clientNo){
        this.client = client;
        nickname = "";
        this.clientNo = clientNo;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public ChatClient getClient() {
        return client;
    }

    public int getClientNo(){
        return clientNo;
    }
}
