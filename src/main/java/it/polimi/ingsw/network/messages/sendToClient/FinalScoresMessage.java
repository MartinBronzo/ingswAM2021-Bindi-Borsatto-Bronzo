package it.polimi.ingsw.network.messages.sendToClient;

import java.util.HashMap;
import java.util.Map;

/**
 * This message is used to communicate the final scores of a multi-player game.
 */
public class FinalScoresMessage implements ResponseInterface {
    private Map<String, Integer> results;

    /**
     * Constructs an empty FinalScoresMessage
     */
    public FinalScoresMessage() {
        results = new HashMap<>();
    }

    /**
     * Adds the specified player's result to the message
     * @param nickName the nickname of a player in the game
     * @param result the specified player's results
     */
    public void addScore(String nickName, Integer result) {
        results.put(nickName, result);
    }

    /**
     * Returns the results of the game
     * @return the results of the game
     */
    public Map<String, Integer> getResults() {
        return results;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.FINALSCORES;
    }
}
