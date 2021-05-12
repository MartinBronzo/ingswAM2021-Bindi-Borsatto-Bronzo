package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.fromClient.Message;

public class Command {
    private String cmd;
    private String parameters;

    public Command(String cmd, Message message) {
        this.cmd = cmd;
        Gson gson = new Gson();
        this.parameters = gson.toJson(message);
    }

    public String getCmd() {
        return cmd;
    }

    public String getParameters() {
        return parameters;
    }
}
