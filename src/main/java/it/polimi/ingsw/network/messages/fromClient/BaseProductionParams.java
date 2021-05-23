package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.List;

public class BaseProductionParams {
    private boolean activated;
    private List<ResourceType> baseInput;
    private List<ResourceType> baseOutput;

    /*public BaseProductionParams(boolean activated, List<ResourceType> baseInput){
        this.activated = activated;
        this.baseInput = baseInput;
        this.baseOutput = null;
    }*/

    public BaseProductionParams(boolean activated, List<ResourceType> baseInput, List<ResourceType> baseOutput) {
        this.activated = activated;
        this.baseInput = baseInput;
        this.baseOutput = baseOutput;
    }

    public boolean isActivated() {
        return activated;
    }

    public List<ResourceType> getBaseInput() {
        return baseInput;
    }

    public List<ResourceType> getBaseOutput() {
        return baseOutput;
    }
}
