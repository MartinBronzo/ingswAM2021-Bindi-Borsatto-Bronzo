package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

/**
 * This message is used to send to the player the output from the Market row or column they sent a request manifesting their interest in buying from.
 */
public class HashMapResFromMarketMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    /**
     * Constructs an HashMapResFromMarketMessage
     * @param resources the output of the Market row or column the player is interested in buying from
     */
    public HashMapResFromMarketMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    /**
     * Returns the output of the Market row or column the player is interested in buying from
     * @return the output of the Market row or column the player is interested in buying from
     */
    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMMARKET;
    }
}
