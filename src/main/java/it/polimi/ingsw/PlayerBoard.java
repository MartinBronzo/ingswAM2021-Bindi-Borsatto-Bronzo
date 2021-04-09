package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevSlot;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.FaithTrack.FaithLevel;

import java.util.*;

public class PlayerBoard {
    private FaithLevel playerFaithLevel;
    private Depot depot;
    private Strongbox strongbox;
    private DevSlots devSlots;
    //private LeaderCards leaderCards;

    public PlayerBoard() {
        //this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        //InactiveLeaderCards = inactiveLeaderCards;
        //ActiveLeaderCards = activeLeaderCards;
    }

    public FaithLevel getPlayerFaithLevel() {
        return playerFaithLevel;
    }


    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(Strongbox strongbox) {
        this.strongbox = strongbox;
    }

    public DevSlots getDevSlots() {
        return devSlots;
    }

    public HashMap<ResourceType,Integer> getAllResources(){
        HashMap<ResourceType,Integer> depotMap = depot.getAllResources();
        HashMap<ResourceType,Integer> strongBoxMap = strongbox.getAllResources();
        Set<ResourceType> depotKeys = depotMap.keySet();
        Set<ResourceType> strongboxKeys = depotMap.keySet();
        Set<ResourceType> keys = new TreeSet<>();
        keys.addAll(depotKeys);
        keys.addAll(strongboxKeys);
        HashMap<ResourceType,Integer> allResourcesMap =new HashMap<>();
        keys.stream()
                .distinct()
                .forEach(key -> allResourcesMap.put(key,depotMap.getOrDefault(key,0) + strongBoxMap.getOrDefault(key,0)));
        return allResourcesMap;
    }

    public Boolean isCardBuyable(DevCard devCard){
        if (devCard==null) throw new NullPointerException("isCardBuyable: DevCardCan't be null");
        HashMap<ResourceType,Integer> cost = devCard.getCost();
        Collection<ResourceType> costKeys = cost.keySet();
        HashMap<ResourceType,Integer> allResources = getAllResources();
        return costKeys.stream().allMatch(key -> cost.getOrDefault(key,0) <= allResources.getOrDefault(key,0));
    }

}
