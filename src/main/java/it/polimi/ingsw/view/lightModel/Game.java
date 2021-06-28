package it.polimi.ingsw.view.lightModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * This is LightModel object represents a lighter version of the Model that is stored in the server. In particular, this class is a
 * container for all the LightModel parts, that is, the Game class can be considered at the whole LightModel.
 */
public class Game {
    private Collection<Player> players;
    private Board mainBoard;
    private Integer lorenzosPosition;

    /**
     * Constructs an empty Game object
     * @param numberOfPlayers the number of players in the game
     */
    public Game(int numberOfPlayers) {
        this.players = new ArrayList<>(numberOfPlayers);
        IntStream.range(0, numberOfPlayers).forEach(i -> players.add(new Player()));
        this.mainBoard = new Board();
        if (numberOfPlayers == 1)
            this.lorenzosPosition = 0;
        else
            this.lorenzosPosition = -1;
    }

    /**
     * Constructs an empty Game object without the number of players in the game set
     */
    public Game() {
        this.players = new ArrayList<>();
        this.mainBoard = new Board();
        this.lorenzosPosition = -1;
    }

    /**
     * Returns the Player objects representing the players in this game
     * @return the Player objects representing the players in this game
     */
    public Collection<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the copy of the MainBoard of this game
     * @return the copy of the MainBoard of this game
     */
    public Board getMainBoard() {
        return mainBoard;
    }

    /**
     * Returns Lorenzo's position in this game
     * @return Lorenzo's position in this game
     */
    public Integer getLorenzosPosition() {
        return lorenzosPosition;
    }

    /**
     * Sets the Player objects, which represent the players in this game, in this Game
     * @param players the Player objects which represent the players in this game
     */
    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    /**
     * Adds a Player object, which represent a player in this game, in thi Game
     * @param player a Player object which represent a player in this game
     */
    public void addPlayer(Player player) {
        if (players == null)
            players = new ArrayList<>();
        players.add(player);
    }

    /**
     * Sets the copy of the MainBoard of this game
     * @param mainBoard the copy of the MainBoard of this game
     */
    public void setMainBoard(Board mainBoard) {
        this.mainBoard = mainBoard;
    }

    /**
     * Sets Lorenzo's position in this game
     * @param lorenzosPosition Lorenzo's position in this game
     */
    public void setLorenzosPosition(Integer lorenzosPosition) {
        this.lorenzosPosition = lorenzosPosition;
    }

    /**
     * Returns the Player object corresponding to the specified player's name
     * @param nick a nickname
     * @return the Player object corresponding to the specified player's name if the nickname does belong to a player in this game
     * @throws NoSuchElementException if there is no player in this game with the specified nickname
     */
    public Player findByNick(String nick) throws NoSuchElementException {
        return players.stream().filter(player -> player.getNickName().equals(nick)).findAny().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Merge this Game object with the new information contained in the specified Game object
     * @param update the Game object containing new information
     * @return true if the merge succeeded
     * @throws NullPointerException if there specified update is a null pointer
     */
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
                    System.out.println("ERRORE NEL MERGE DEL PLAYER");
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
