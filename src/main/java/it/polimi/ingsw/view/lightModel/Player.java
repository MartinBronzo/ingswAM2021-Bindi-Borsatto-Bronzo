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

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public void setBaseProductionInput(HashMap<ResourceType, Integer> baseProductionInput) {
        this.baseProductionInput = baseProductionInput;
    }

    public void setBaseProductionOutput(HashMap<ResourceType, Integer> baseProductionOutput) {
        this.baseProductionOutput = baseProductionOutput;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setLeaderSlots(HashMap<ResourceType, Integer> leaderSlots) {
        this.leaderSlots = leaderSlots;
    }

    public void setUsedLeaders(List<LeaderCard> usedLeaders) {
        this.usedLeaders = usedLeaders;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void addDepotShelf(DepotShelf depotShelf) {
        if (depotShelves == null)
            depotShelves = new ArrayList<>();
        depotShelves.add(depotShelf);
    }

    public void setPopeTiles(List<PopeTile> popeTiles) {
        this.popeTiles = popeTiles;
    }

    public void setFaithPosition(int faithPosition) {
        this.faithPosition = faithPosition;
    }

    public void setUnUsedLeaders(List<LeaderCard> unUsedLeaders) {
        this.unUsedLeaders = unUsedLeaders;
    }

    public String getNickName() {
        return nickName;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public DevSlots getDevSlots() {
        return devSlots;
    }

    public void setDevSlots(DevSlots devSlots) {
        this.devSlots = devSlots;
    }

    public List<LeaderCard> getUnUsedLeaders() {
        return unUsedLeaders;
    }

    public List<LeaderCard> getUsedLeaders() {
        return usedLeaders;
    }

    public Integer getFaithPosition() {
        return faithPosition;
    }

    public HashMap<ResourceType, Integer> getBaseProductionInput() {
        return baseProductionInput;
    }

    public HashMap<ResourceType, Integer> getBaseProductionOutput() {
        return baseProductionOutput;
    }

    public List<DepotShelf> getDepotShelves() {
        return depotShelves;
    }

    public HashMap<ResourceType, Integer> getStrongBox() {
        return strongBox;
    }

    public void setStrongBox(HashMap strongBoxMap) {
        this.strongBox = strongBoxMap;
    }

    public HashMap<ResourceType, Integer> getLeaderSlots() {
        return leaderSlots;
    }

    public Integer getVictoryPoints() {
        return victoryPoints;
    }

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
