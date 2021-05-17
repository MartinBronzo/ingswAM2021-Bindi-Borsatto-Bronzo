package it.polimi.ingsw.view.readOnlyModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Game {
    private Collection<Player> players;
    private Board mainBoard;
    private Integer lorenzosPosition;

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

    public Integer getLorenzosPosition() {
        return lorenzosPosition;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        if(players == null)
            players = new ArrayList<>();
        players.add(player);
    }

    public void setMainBoard(Board mainBoard) {
        this.mainBoard = mainBoard;
    }

    public void setLorenzosPosition(int lorenzosPosition) {
        this.lorenzosPosition = lorenzosPosition;
    }

    public boolean merge(Game update) throws NullPointerException {
        //TODO: come si comporta tojson con i primitivi che non possono essere null
        if (update==null) throw new NullPointerException("update is Null");
        int updateLorenzenzo = update.getLorenzosPosition();
        return true;
    }

    /* merge using class methods
    public boolean merge(Game update) throws NullPointerException {
        //TODO: no idea how works game update
        if (update==null) throw new NullPointerException("update is Null");
        Class updateClass = update.getClass();
        Arrays.stream(updateClass.getDeclaredFields()).filter(field -> {
            try {
                return field.get(update) != null;
            } catch (IllegalAccessException e) {
                return false;
            }
        }).forEach(field -> {
            try {
                field.set(this, field.get(update));
            } catch (IllegalAccessException e) {
                System.err.println("Error happened merging model with update");
                System.exit(1);
            }
        });
        return true;
    }
     */

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", mainBoard=" + mainBoard +
                ", lorenzosPosition=" + lorenzosPosition +
                '}';
    }
}
