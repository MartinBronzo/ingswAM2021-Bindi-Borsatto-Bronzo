package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Command;

import java.io.IOException;

public abstract class Client {
    public abstract void startConnection() throws IOException;
    public abstract void doConnection();

    public abstract void sendMessage(Command message) throws NullPointerException;

    public abstract void manageLogin();

    protected abstract void printWelcome();

    protected abstract void manageGameStarting();
}

