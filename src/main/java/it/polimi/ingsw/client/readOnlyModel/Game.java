package it.polimi.ingsw.client.readOnlyModel;

import java.util.ArrayList;
import java.util.Collection;

public class Game {
    private Collection<Player> players;
    private Board mainBoard;
    private int lorenzosPosition;

    public Game(int numberOfPlayers) {
        this.players = new ArrayList<>(numberOfPlayers);
        for (Player p: this.players) {
            p = new Player();
            players.add(p);
        }
        this.mainBoard = new Board();
        if (numberOfPlayers == 1)
            this.lorenzosPosition = 0;
        else
            this.lorenzosPosition = -1;
    }

    public Game() {
        this.players = new ArrayList<>();
        this.mainBoard = new Board();
        this.lorenzosPosition = -1;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Board getMainBoard() {
        return mainBoard;
    }

    public int getLorenzosPosition() {
        return lorenzosPosition;
    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", mainBoard=" + mainBoard +
                ", lorenzosPosition=" + lorenzosPosition +
                '}';
    }
}
