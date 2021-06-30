package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.fromClient.Message;

/**
 * This is the container of the messages that the GameController receives from the player. This is what is actually being sent by the players.
 */
public class Command {
    private String cmd;
    private String parameters;

    /**
     * Constructs a Command object for the specified command and which will contain the specified parameters
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

    /**
     * Returns the command type
     * @return the command type
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Returns the message command content
     * @return the message command content
     */
    public String getParameters() {
        return parameters;
    }
}
