package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

/**
 * This message is used to send to the player the cost of the DevGrid they sent a request manifesting their interest in buying.
 */
public class HashMapResFromDevGridMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    /**
     * Constructs an HashMapResFromDevGridMessage
     * @param resources the cost of the DevCard the player wants to buy
     */
    public HashMapResFromDevGridMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    /**
     * Returns the cost of the DevCard the player wants to buy
     * @return the cost of the DevCard the player wants to buy
     */
    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMDEVGRID;
    }
}
