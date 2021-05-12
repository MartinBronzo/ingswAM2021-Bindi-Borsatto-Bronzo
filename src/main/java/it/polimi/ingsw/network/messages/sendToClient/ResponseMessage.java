package it.polimi.ingsw.network.messages.sendToClient;

public class ResponseMessage {
    private ResponseType responseType;
    private String responseContent;

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getResponseContent() {
        return responseContent;
    }
}
