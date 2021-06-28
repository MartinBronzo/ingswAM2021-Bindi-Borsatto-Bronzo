package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.fromClient.Message;

public class Command {
    private String cmd;
    private String parameters;

    /**
     * @param cmd the command type to be send, check network documentation to see all available command types
     * @param message the message command content
     */
    public Command(String cmd, Message message) {
        this.cmd = cmd;
        Gson gson = new Gson();
        this.parameters = gson.toJson(message);
    }


    /**
     * this constructor is used when cmd doesn't need any parameter
     *
     * @param cmdWithoutParameters can be "quit" or "endTurn" or "pingResponse"
     */
    public Command(String cmdWithoutParameters) {
        this.cmd = cmdWithoutParameters;
        this.parameters = "";
    }

    public String getCmd() {
        return cmd;
    }

    public String getParameters() {
        return parameters;
    }
}
