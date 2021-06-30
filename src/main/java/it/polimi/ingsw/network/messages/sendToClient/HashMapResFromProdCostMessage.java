package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

/**
 * This message is used to send to the player the inputs needed for the production methods they sent a request manifesting their interest in activating.
 */
public class HashMapResFromProdCostMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    /**
     * Constructs an HashMapResFromProdCostMessage
     * @param resources the inputs needed for the production methods is interested in activating
     */
    public HashMapResFromProdCostMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    /**
     * Returns the inputs needed for the production methods is interested in activating
     * @return the inputs needed for the production methods is interested in activating
     */
    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMPRODCOST;
    }
}
