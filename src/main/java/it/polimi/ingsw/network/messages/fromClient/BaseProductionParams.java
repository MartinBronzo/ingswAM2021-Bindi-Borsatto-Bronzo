package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.List;

/**
 * This class contains all the information needed in order to activate the BaseProduction.
 */
public class BaseProductionParams {
    private boolean activated;
    private List<ResourceType> baseInput;
    private List<ResourceType> baseOutput;

    /*public BaseProductionParams(boolean activated, List<ResourceType> baseInput){
        this.activated = activated;
        this.baseInput = baseInput;
        this.baseOutput = null;
    }*/

    /**
     * Constructs a BaseProductionParams object
     * @param activated true if the player wants to activate the BaseProduction, false otherwise
     * @param baseInput the resources the player wants to use as input of the BaseProduction
     * @param baseOutput the resources the player wants to use as output of the BaseProduction
     */
    public BaseProductionParams(boolean activated, List<ResourceType> baseInput, List<ResourceType> baseOutput) {
        this.activated = activated;
        this.baseInput = baseInput;
        this.baseOutput = baseOutput;
    }

    /**
     * Returns whether the player wants to use the BaseProduction
     * @return true if the player wants to activate the BaseProduction, false otherwise
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Returns the resources the player wants to use as input of the BaseProduction
     * @return the resources the player wants to use as input of the BaseProduction
     */
    public List<ResourceType> getBaseInput() {
        return baseInput;
    }

    /**
     * Returns the resources the player wants to use as output of the BaseProduction
     * @return the resources the player wants to use as output of the BaseProduction
     */
    public List<ResourceType> getBaseOutput() {
        return baseOutput;
    }
}
