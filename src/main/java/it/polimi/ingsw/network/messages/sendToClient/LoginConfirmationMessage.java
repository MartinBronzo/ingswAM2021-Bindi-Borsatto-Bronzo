package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to confirm the player that their nickname has been accepted by the server.
 */
public class LoginConfirmationMessage implements ResponseInterface {
    private String confirmedNickname;

    /**
     * Constructs a LoginConfirmationMessage
     * @param confirmedNickname the nickname that the player sent and that has been accepted by the Server
     */
    public LoginConfirmationMessage(String confirmedNickname) {
        this.confirmedNickname = confirmedNickname;
    }

    /**
     * Returns the nickname that the player sent and that has been accepted by the Server
     * @return the nickname that the player sent and that has been accepted by the Server
     */
    public String getConfirmedNickname() {
        return confirmedNickname;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETNICK;
    }
}
