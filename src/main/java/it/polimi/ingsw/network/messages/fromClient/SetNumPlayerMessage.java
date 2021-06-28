package it.polimi.ingsw.network.messages.fromClient;

/**
 * This message is used to send the number of players the maker of the game wants to play in the game.
 */
public class SetNumPlayerMessage extends Message {
    private int numPlayer;

    /**
     * Constructs a SetNumPlayerMessage
     * @param numPlayer the number of players the maker of the game wants to play in the game
     */
    public SetNumPlayerMessage(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    /**
     * Returns the number of players the maker of the game wants to play in the game
     * @return the number of players the maker of the game wants to play in the game
     */
    public int getNumPlayer() {
        return numPlayer;
    }
}
