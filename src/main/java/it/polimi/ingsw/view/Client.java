package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Command;

import java.io.IOException;

public abstract class Client {
    public abstract void startConnection() throws IOException;
    public abstract void doConnection();

    public abstract void sendMessage(Command message) throws NullPointerException;

    protected abstract void manageLogin();
    protected abstract void manageGameStarting();

    protected abstract void buyFromMarket() throws InterruptedException, IOException;

    protected abstract void buyDevCard() throws InterruptedException, IOException;

    protected abstract void getDevCardCost() throws InterruptedException, IOException;
}

