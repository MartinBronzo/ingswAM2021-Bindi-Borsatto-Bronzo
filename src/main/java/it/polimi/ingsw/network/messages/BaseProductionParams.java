package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.BaseProduction;
import it.polimi.ingsw.model.ResourceType;

public class BaseProductionParams {
    private boolean activate;
    private ResourceType baseInput;
    private ResourceType baseOutput;

    public BaseProductionParams(boolean activate, ResourceType baseInput){
        this.activate = activate;
        this.baseInput = baseInput;
        this.baseOutput = null;
    }

    public BaseProductionParams(boolean activate, ResourceType baseInput, ResourceType baseOutput){
        this.activate = activate;
        this.baseInput = baseInput;
        this.baseOutput = baseOutput;
    }

    public boolean isActivate() {
        return activate;
    }

    public ResourceType getBaseInput() {
        return baseInput;
    }

    public ResourceType getBaseOutput() {
        return baseOutput;
    }
}
