package it.polimi.ingsw.view.lightModel;

import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.devCards.DevSlots;
import it.polimi.ingsw.model.faithTrack.PopeTile;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This LightModel object contains all the information needed to represent the players' Models in the server.
 */
public class Player {
    private String nickName;
    private PlayerState playerState;
    private DevSlots devSlots;
    private List<LeaderCard> unUsedLeaders;
    private List<LeaderCard> usedLeaders;
    private Integer faithPosition;
    private HashMap<ResourceType, Integer> baseProductionInput;
    private HashMap<ResourceType, Integer> baseProductionOutput;
    private List<DepotShelf> depotShelves;
    private HashMap<ResourceType, Integer> strongBox;
    private HashMap<ResourceType, Integer> leaderSlots;
    private Integer victoryPoints;
    private List<PopeTile> popeTiles;

    /**
     * Sets the victoryPoints the player has so far achieved
     * @param victoryPoints the victoryPoints the player has so far achieved
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * Sets the BaseProduction inputs the player has decided to have
     * @param baseProductionInput the BaseProduction inputs the player has decided to have
     */
    public void setBaseProductionInput(HashMap<ResourceType, Integer> baseProductionInput) {
        this.baseProductionInput = baseProductionInput;
    }

    /**
     * Sets the BaseProduction outputs the player has decided to have
     * @param baseProductionOutput the BaseProduction outputs the player has decided to have
     */
    public void setBaseProductionOutput(HashMap<ResourceType, Integer> baseProductionOutput) {
        this.baseProductionOutput = baseProductionOutput;
    }

    /**
     * Sets the player's state
     * @param playerState the player's state
     */
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * Sets the resources stored in the player's ExtraSlot LeaderCard
     * @param leaderSlots the resources stored in the player's ExtraSlot LeaderCard
     */
    public void setLeaderSlots(HashMap<ResourceType, Integer> leaderSlots) {
        this.leaderSlots = leaderSlots;
    }

    /**
     * Sets the player's Used LeaderCards
     * @param usedLeaders the player's Unused LeaderCards
     */
    public void setUsedLeaders(List<LeaderCard> usedLeaders) {
        this.usedLeaders = usedLeaders;
    }

    /**
     * Sets the player's nickname
     * @param nickName the player's nickname
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Adds a DepotShelf object, which represents a player's Depot shelf
     * @param depotShelf a DepotShelf object which represents a player's Depot shelf
     */
    public void addDepotShelf(DepotShelf depotShelf) {
        if (depotShelves == null)
            depotShelves = new ArrayList<>();
        depotShelves.add(depotShelf);
    }

    /**
     * Sets the player's PopeTiles
     * @param popeTiles the player's PopeTiles
     */
    public void setPopeTiles(List<PopeTile> popeTiles) {
        this.popeTiles = popeTiles;
    }

    /**
     * Sets the player's position onto the FaithTrack
     * @param faithPosition the player's position onto the FaithTrack
     */
    public void setFaithPosition(int faithPosition) {
        this.faithPosition = faithPosition;
    }

    /**
     * Sets the player's Unused LeaderCards
     * @param unUsedLeaders the player's Unused LeaderCards
     */
    public void setUnUsedLeaders(List<LeaderCard> unUsedLeaders) {
        this.unUsedLeaders = unUsedLeaders;
    }

    /**
     * Returns the player's nickname
     * @return the player's nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Returns the player's state
     * @return the player's state
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Returns the player's DevSlots
     * @return the player's DevSlots
     */
    public DevSlots getDevSlots() {
        return devSlots;
    }

    /**
     * Set the player's DevSlots
     * @param devSlots the player's DevSlots
     */
    public void setDevSlots(DevSlots devSlots) {
        this.devSlots = devSlots;
    }

    /**
     * Returns the player's Unused LeaderCards
     * @return the player's Unused LeaderCards
     */
    public List<LeaderCard> getUnUsedLeaders() {
        return unUsedLeaders;
    }

    /**
     * Returns the player's Used LeaderCards
     * @return the player's Used LeaderCards
     */
    public List<LeaderCard> getUsedLeaders() {
        return usedLeaders;
    }

    /**
     * Returns the player's position onto the FaithTrack
     * @return the player's position onto the FaithTrack
     */
    public Integer getFaithPosition() {
        return faithPosition;
    }

    /**
     * Returns the BaseProduction inputs the player has decided to have
     * @return the BaseProduction inputs the player has decided to have
     */
    public HashMap<ResourceType, Integer> getBaseProductionInput() {
        return baseProductionInput;
    }

    /**
     * Returns the BaseProduction outputs the player has decided to have
     * @return the BaseProduction outputs the player has decided to have
     */
    public HashMap<ResourceType, Integer> getBaseProductionOutput() {
        return baseProductionOutput;
    }

    /**
     * Returns the player's DepotShelf objects which all together represent the player's Depot
     * @return the player's DepotShelf objects which all together represent the player's Depot
     */
    public List<DepotShelf> getDepotShelves() {
        return depotShelves;
    }

    /**
     * Returns the player's StrongBox
     * @return the player's StrongBox
     */
    public HashMap<ResourceType, Integer> getStrongBox() {
        return strongBox;
    }

    /**
     * Sets the player's StrongBox
     * @param strongBoxMap the player's StrongBox
     */
    public void setStrongBox(HashMap strongBoxMap) {
        this.strongBox = strongBoxMap;
    }

    /**
     * Returns the resources stored onto the player's ExtraSlot LeaderCards
     * @return the resources stored onto the player's ExtraSlot LeaderCards
     */
    public HashMap<ResourceType, Integer> getLeaderSlots() {
        return leaderSlots;
    }

    /**
     * Returns the player's victory points
     * @return the player's victory points
     */
    public Integer getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Returns the player's PopeTiles
     * @return the player's PopeTiles
     */
    public List<PopeTile> getPopeTiles() {
        return popeTiles;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickName + '\'' +
                ", playerState=" + playerState +
                ", devSlots=" + devSlots +
                ", unUsedLeaders=" + unUsedLeaders +
                ", usedLeaders=" + usedLeaders +
                ", faithPosition=" + faithPosition +
                ", baseProductionInput=" + baseProductionInput +
                ", baseProductionOutput=" + baseProductionOutput +
                ", depotShelves=" + depotShelves +
                ", strongBox=" + strongBox +
                ", leaderSlots=" + leaderSlots +
                ", victoryPoints=" + victoryPoints +
                ", popeTiles" + popeTiles +
                '}';
    }

    /**
     * Merge this Player object with the new information contained in the specified Player object
     * @param updatePlayer the Player object containing new information
     */
    public void merge(Player updatePlayer) {
        PlayerState updateState = updatePlayer.getPlayerState();
        if (updateState != null) this.playerState = updateState;

        DevSlots updateDevslots = updatePlayer.getDevSlots();
        if (updateDevslots != null) this.devSlots = updateDevslots;

        List<LeaderCard> leaders = updatePlayer.getUnUsedLeaders();
        if (leaders != null) this.unUsedLeaders = leaders;

        leaders = updatePlayer.getUsedLeaders();
        if (leaders != null) this.usedLeaders = leaders;

        Integer updateInteger = updatePlayer.getFaithPosition();
        if (updateInteger != null) this.faithPosition = updateInteger;

        updateInteger = updatePlayer.getVictoryPoints();
        if (updateInteger != null) this.victoryPoints = updateInteger;

        HashMap<ResourceType, Integer> updateMap = updatePlayer.getBaseProductionInput();
        if (updateMap != null) this.baseProductionInput = updateMap;

        updateMap = updatePlayer.getBaseProductionOutput();
        if (updateMap != null) this.baseProductionOutput = updateMap;

        updateMap = updatePlayer.getStrongBox();
        if (updateMap != null) this.strongBox = updateMap;

        updateMap = updatePlayer.getLeaderSlots();
        if (updateMap != null) this.leaderSlots = updateMap;

        List<DepotShelf> updateDepotShelves = updatePlayer.getDepotShelves();
        if (updateDepotShelves != null) this.depotShelves = updateDepotShelves;

        List<PopeTile> updatePopeTiles = updatePlayer.getPopeTiles();
        if (updatePopeTiles != null) this.popeTiles = updatePopeTiles;
    }
}
