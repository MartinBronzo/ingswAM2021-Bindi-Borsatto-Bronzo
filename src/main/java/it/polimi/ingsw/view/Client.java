package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Command;

import java.io.IOException;

/**
 * This interface is implemented by the class that enables the user to communicate to the Server.
 */
public interface Client {

    /**
     * Starts the connection used to communicate to the Server
     * @throws IOException if an IO operations fails
     */
    void startConnection() throws IOException;

    /**
     * Receives and deals with the messages that come from the Server
     */
     void doConnection();

    /**
     * Ends the connection to the Server
     */
    void endConnection();

    /**
     * Sends the specified message to the Server
     * @param message the message to be sent
     * @throws NullPointerException if there is no element to send the specified message to
     */
     void sendMessage(Command message) throws NullPointerException;

}

