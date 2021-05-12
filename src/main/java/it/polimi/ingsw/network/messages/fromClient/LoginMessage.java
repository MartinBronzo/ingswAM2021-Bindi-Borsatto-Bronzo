package it.polimi.ingsw.network.messages.fromClient;

public class LoginMessage extends Message {
    private String nickName;

    public LoginMessage(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
