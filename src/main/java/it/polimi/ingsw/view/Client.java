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

    protected abstract void getResourcesFromMarket() throws IOException;

    protected abstract void getProductionCost() throws InterruptedException, IOException;

    protected abstract void activateProduction() throws IOException;

    protected abstract void moveBetweenShelves() throws IOException;

    protected abstract void moveLeaderToShelf() throws IOException;

    protected abstract void moveShelfToLeader() throws IOException;

    protected abstract void discardLeader() throws IOException;

    protected abstract void activateLeader() throws IOException;
}

