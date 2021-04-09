package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevSlot;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.FaithTrack.FaithLevel;

import java.util.List;

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
}
