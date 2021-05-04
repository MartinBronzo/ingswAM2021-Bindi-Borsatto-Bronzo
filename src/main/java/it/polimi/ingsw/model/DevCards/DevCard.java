package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.ResourceType;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class implements DevelopmentsCards, it needs to be immutable to not change the DevCard Attributes
 */
public class DevCard implements Comparable<DevCard> {
    private final int level;
    private final DevCardColour colour;
    private final int victoryPoints;
    private final HashMap<ResourceType, Integer> productionInput;
    private final HashMap<ResourceType, Integer> productionOutput;
    private final HashMap<ResourceType, Integer> cost;
    private final String url;

    /**
     * @param colour           is one of the 4 possible Card Colours currently in the game
     * @param level            can be 1,2 or 3
     * @param url              refers to image url which can be used to implements a GUI
     * @param victoryPoints    is a non negative int which refers to the victory points given by the Dev Card at the end of the GameController
     * @param cost             is the reference to the Resources, that can be used to buy the DevCard
     * @param productionInput  is the reference to the Resources, that can be used to produce other resources
     * @param productionOutput is the reference to the Resources produced after consuming the resources in productionInput
     * @throws IllegalArgumentException  when the Card Level is not 1 or 2 or 3, or when Victory points are negative
     * @throws NegativeQuantityException when one or more of the resources maps contain a negative quantity, which is impossible.
     */
    public DevCard(int level, DevCardColour colour, int victoryPoints, HashMap<ResourceType, Integer> productionInput, HashMap<ResourceType, Integer> productionOutput, HashMap<ResourceType, Integer> cost, String url) throws IllegalArgumentException, NegativeQuantityException {
        if (level < 1 || level > 3 || victoryPoints < 0)
            throw new IllegalArgumentException("DevCard Builder: Illegal int parameter");

        Collection<Integer> hashMapValues;

        hashMapValues = productionInput.values();
        if (hashMapValues.stream().anyMatch(i -> i < 0))
            throw new NegativeQuantityException("DevCard Builder: Illegal value in InputHashMap");
        if (productionInput.getOrDefault(ResourceType.FAITHPOINT, 0) != 0)
            throw new IllegalArgumentException("DevCard Builder: production input cannot contains FAITHPOINTS");

        hashMapValues = productionOutput.values();
        if (hashMapValues.stream().anyMatch(i -> i < 0))
            throw new NegativeQuantityException("DevCard Builder: Illegal value in OutputHashMap");

        hashMapValues = cost.values();
        if (hashMapValues.stream().anyMatch(i -> i < 0))
            throw new NegativeQuantityException("DevCard Builder: Illegal value in CostHashMap");
        if (cost.getOrDefault(ResourceType.FAITHPOINT, 0) != 0)
            throw new IllegalArgumentException("DevCard Builder: Cost cannot contains FAITHPOINTS");


        this.level = level;
        this.colour = colour;
        this.victoryPoints = victoryPoints;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
        this.cost = cost;
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public DevCardColour getColour() {
        return colour;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @return is a copy of production input, because this class and its references must be immutable
     */
    public HashMap<ResourceType, Integer> getProductionInput() {
        return new HashMap<>(productionInput);
    }

    /**
     * @return is a copy of production output, because this class and its references must be immutable
     */
    public HashMap<ResourceType, Integer> getProductionOutput() {
        return new HashMap<>(productionOutput);
    }

    /**
     * @return is a copy of cost, because this class and its references must be immutable
     */
    public HashMap<ResourceType, Integer> getCost() {
        return new HashMap<>(cost);
    }

    /**
     * @return the image url of the devCard
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "DevCard{" +
                "level=" + level +
                ", colour=" + colour +
                ", victoryPoints=" + victoryPoints +
                ", productionInput=" + productionInput +
                ", productionOutput=" + productionOutput +
                ", cost=" + cost +
                ", url='" + url + '\'' +
                '}';
    }

    /**
     * Compares this devCard with the specified devCard for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param devCard the devCard to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(DevCard devCard) throws NullPointerException {
        if (devCard == null) throw new NullPointerException("devCard can't be null");
        int compareLevel = Integer.compare(this.level, devCard.level);
        return compareLevel == 0 ? Integer.compare(this.colour.ordinal(), devCard.colour.ordinal()) : compareLevel;
    }
}
