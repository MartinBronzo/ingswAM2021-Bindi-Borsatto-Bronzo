package it.polimi.ingsw.view.readOnlyModel;

import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;
import it.polimi.ingsw.view.readOnlyModel.player.Devslot;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.DevCards.DevSlots;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;

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

    public Player() {
        this.nickName = nickName;
        this.playerState = playerState;
        this.devSlots = devSlots;
        this.unUsedLeaders = unUsedLeaders;
        this.usedLeaders = usedLeaders;
        this.faithPosition = faithPosition;
        this.baseProductionInput = baseProductionInput;
        this.baseProductionOutput = baseProductionOutput;
        this.depotShelves = depotShelves;
        this.strongBox = strongBox;
        this.leaderSlots = leaderSlots;
        this.victoryPoints = victoryPoints;
        this.popeTiles = popeTiles;
    }

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

    public void doSetNickname(String a){
        this.nickName = a;
    }

    public void addDepotShelf(DepotShelf depotShelf){
        if(depotShelves == null)
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

    public void setDevSlots(DevSlots devSlots){this.devSlots = devSlots;}

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

    public void setStrongBox(HashMap strongBoxMap){this.strongBox = strongBoxMap;}

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
    }
}
