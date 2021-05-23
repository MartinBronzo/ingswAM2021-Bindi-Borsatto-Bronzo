package it.polimi.ingsw.network.messages.sendToClient;

public class LoginConfirmationMessage implements ResponseInterface {
    private String confirmedNickname;

    public LoginConfirmationMessage(String confirmedNickname) {
        this.confirmedNickname = confirmedNickname;
    }

    public String getConfirmedNickname() {
        return confirmedNickname;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETNICK;
    }
}
