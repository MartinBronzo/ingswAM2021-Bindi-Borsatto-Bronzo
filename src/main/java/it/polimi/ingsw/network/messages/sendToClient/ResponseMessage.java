package it.polimi.ingsw.network.messages.sendToClient;

public class ResponseMessage {
    private ResponseType responseType;
    private String responseContent;

    public ResponseMessage(ResponseType responseType, String responseContent) {
        this.responseType = responseType;
        this.responseContent = responseContent;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getResponseContent() {
        return responseContent;
    }
}
