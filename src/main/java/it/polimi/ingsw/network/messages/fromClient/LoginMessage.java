package it.polimi.ingsw.network.messages.fromClient;

/**
 * This message is used to login into the Server.
 */
public class LoginMessage extends Message {
    private String nickName;

    /**
     * Constructs a LoginMessage
     * @param nickName the nickname the player wants to be called by
     */
    public LoginMessage(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Returns the nickname the player wants to be called by
     * @return the nickname the player wants to be called by
     */
    public String getNickName() {
        return nickName;
    }
}
