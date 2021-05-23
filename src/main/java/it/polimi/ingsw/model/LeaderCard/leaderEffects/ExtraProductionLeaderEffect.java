package it.polimi.ingsw.model.LeaderCard.leaderEffects;

import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;

/**
 * This class aims to implement the effect of adding a new Production Power to the ones available to the player. If the player pays the required resources,
 * they can get back whatever resource they desire and some extra output
 */
//
public class ExtraProductionLeaderEffect extends Effect {
    private final ResourceType requiredInputType;
    private final int requiredInputNumber;
    private final ResourceType extraOutputType;
    private final int extraOutputQuantity;
    private final int normalOutputQuantity;

    public ExtraProductionLeaderEffect() {
        this.requiredInputType = null;
        this.requiredInputNumber = 0;
        this.extraOutputType = ResourceType.FAITHPOINT;
        this.extraOutputQuantity = 1;
        this.normalOutputQuantity = 1;
    }

    /**
     * Constructs an ExtraProductionLeaderEffect which will require the specified type and amount of resources to be activated and
     * by default it will give one normal output and one FaithPoint as an extra output
     *
     * @param requiredInputType   the needed type of the resources
     * @param requiredInputNumber the needed number of resources
     */

    public ExtraProductionLeaderEffect(ResourceType requiredInputType, int requiredInputNumber) {
        if (requiredInputNumber <= 0)
            throw new IllegalArgumentException("The required amount of resources can't be a number less than or equal to 0!");
        if (requiredInputType.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("FaithPoints can't be required in order to activate the extra production!");
        this.requiredInputType = requiredInputType;
        this.requiredInputNumber = requiredInputNumber;
        this.extraOutputType = ResourceType.FAITHPOINT;
        this.extraOutputQuantity = 1;
        this.normalOutputQuantity = 1;
    }

    /**
     * Constructs a clone of the specified ExtraProductionLeaderEffect
     *
     * @param original the effect to be cloned
     */
    public ExtraProductionLeaderEffect(ExtraProductionLeaderEffect original) {
        this(original.requiredInputType, original.requiredInputNumber);
    }

    @Override
    public Effect getClone() {
        return new ExtraProductionLeaderEffect(this);
    }

    //With extra constructs (which enable to set the other attributes of the object as needed), we are able to implement a more custom effect

    @Override
    public HashMap<ResourceType, Integer> extraProductionEffect(ResourceType desiredOutput) throws IllegalArgumentException {
        HashMap<ResourceType, Integer> output = new HashMap<>();

        if (desiredOutput.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("This effect can't produce a a FaithPoint!");

        if (desiredOutput.equals(this.extraOutputType))
            output.put(desiredOutput, this.normalOutputQuantity + this.extraOutputQuantity);
        else {
            output.put(desiredOutput, this.normalOutputQuantity);
            output.put(this.extraOutputType, this.extraOutputQuantity);
        }
        return output;
    }

    /**
     * Returns the resources required in order to produce using this effect
     *
     * @return the required resources
     */
    @Override
    public HashMap<ResourceType, Integer> getRequiredInput() {
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(this.requiredInputType, this.requiredInputNumber);
        return input;
    }

    public ResourceType getRequiredInputType() {
        return requiredInputType;
    }

    public int getRequiredInputNumber() {
        return requiredInputNumber;
    }

    public ResourceType getExtraOutputType() {
        return extraOutputType;
    }

    public int getExtraOutputQuantity() {
        return extraOutputQuantity;
    }

    public int getNormalOutputQuantity() {
        return normalOutputQuantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ExtraProductionLeaderEffect))
            return false;
        ExtraProductionLeaderEffect tmp = (ExtraProductionLeaderEffect) obj;
        return this.requiredInputNumber == tmp.requiredInputNumber && this.requiredInputType.equals(tmp.requiredInputType) &&
                this.extraOutputType.equals(tmp.extraOutputType) && this.extraOutputQuantity == tmp.extraOutputQuantity &&
                this.normalOutputQuantity == tmp.normalOutputQuantity;
    }

    @Override
    public boolean isOneShotCard() {
        return false;
    }
}


//TODO: controllare che per tutti i metodi i cui input ricevo dall'utente ci siano gli opportuni controlli per lanciare eccezioni se gli input sono sbagliati