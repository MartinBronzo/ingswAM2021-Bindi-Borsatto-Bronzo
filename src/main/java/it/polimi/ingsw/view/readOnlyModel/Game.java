package it.polimi.ingsw.view.readOnlyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class Game {
    private Collection<Player> players;
    private Board mainBoard;
    private Integer lorenzosPosition;

    public Game(int numberOfPlayers) {
        this.players = new ArrayList<>(numberOfPlayers);
        IntStream.range(0, numberOfPlayers).forEach(i -> players.add(new Player()));
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

    public Integer getLorenzosPosition() {
        return lorenzosPosition;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        if (players == null)
            players = new ArrayList<>();
        players.add(player);
    }

    public void setMainBoard(Board mainBoard) {
        this.mainBoard = mainBoard;
    }

    public void setLorenzosPosition(Integer lorenzosPosition) {
        this.lorenzosPosition = lorenzosPosition;
    }

    private Player findByNick(String nick) throws NoSuchElementException {
        return players.stream().filter(player -> player.getNickName().equals(nick)).findAny().orElseThrow(NoSuchElementException::new);
    }

    public boolean merge(Game update) throws NullPointerException {
        if (update == null) throw new NullPointerException("update is Null");

        Integer updateLorenzo = update.getLorenzosPosition();
        if (updateLorenzo != null) this.lorenzosPosition = updateLorenzo;

        Board updateBoard = update.getMainBoard();
        if (updateBoard != null) this.mainBoard.merge(updateBoard);

        Collection<Player> updatePlayers = update.players;
        if (updatePlayers != null) {
            for (Player updatePlayer : updatePlayers) {
                try {
                    this.findByNick(updatePlayer.getNickName()).merge(updatePlayer);
                } catch (NoSuchElementException e) {
                    players.add(updatePlayer);
                }
            }
        }
        return true;
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
