package it.polimi.ingsw.network.messages.sendToClient;

import java.util.HashMap;
import java.util.Map;

public class FinalScoresMessage implements ResponseInterface {
    private Map<String, Integer> results;

    public FinalScoresMessage() {
        results = new HashMap<>();
    }

    public void addScore(String nickName, Integer result) {
        results.put(nickName, result);
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.FINALSCORES;
    }
}
