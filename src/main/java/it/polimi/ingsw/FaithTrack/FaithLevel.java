package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the position of the players who actively play in the game. Each player in the game is associated with a FaithLevel.
 * The position stored in this class is always in the range of the corresponding FaithTrack (from 0 to the size of the track minus 1).
 * It also stores the PopeTiles each player is given at the beginning of the game.
 */
//The PopeTiles here are stored in the same order as the one given by the list of PopeTiles which is passed as a parameter at the constructor (because we made a clone of that list):
//if we made a list of the ReportNums of the PopeTiles maintaining the same order of the list stored here, we would get the same order of ReportNums
//as the one stored in the ReportNumOrder in the FaithTrack (e.g., if REPORTNUM1 has index 0 in the ReportNumOrder, then the PopeTile at position 0 in the
//list stored here has REPORTNUM1 as its reportNum. This is true for all PopeTiles in the list).
public class FaithLevel extends FaithLevelBasic {
    /**
     * The PopeTiles the player can activate or discard throughout the game
     */
    private List<PopeTile> popeTiles;
    private boolean arePopeTilesSet;

    /**
     * Constructs a FaithLevel. Since it represents the position of a player at the beginning of the game, its position is set to 0 (the first Cell of the track).
     *
     * @param faithTrack the FaithTrack the player plays in
     * @param popeTiles  the PopeTiles each player is given at the beginning of the game
     */
    public FaithLevel(FaithTrack faithTrack, List<PopeTile> popeTiles) {
        super(faithTrack);
        //By doing this, only the FaithLevel is able to change the state of its PopeTiles
        this.popeTiles = this.cloneList(popeTiles);
        this.arePopeTilesSet = true;
    }

    public FaithLevel() {
        super();
        this.popeTiles = null;
        this.arePopeTilesSet = false;
    }

    /**
     * Constructs a clone of a FaithLevel which is already set (it has a corresponding FaithTrack and a list of PopeTiles)
     *
     * @param original the FaithLevel to be cloned
     */
    public FaithLevel(FaithLevel original) {
        this(original.faithTrack, original.popeTiles);
        this.position = original.position;
    }

    /**
     * Sets the PopeTiles this FaithLevel has if they haven't been set, yet
     *
     * @param popeTiles a list of PopeTiles
     */
    public void setPopeTiles(List<PopeTile> popeTiles) {
        if (arePopeTilesSet == false) {
            this.popeTiles = this.cloneList(popeTiles);
            this.arePopeTilesSet = true;
        }

    }

    /**
     * Clones the list of PopeTiles given as input
     *
     * @param toBeCloned the list of PopeTiles to be cloned
     * @return a clone of the input list
     */
    private List<PopeTile> cloneList(List<PopeTile> toBeCloned) {
        List<PopeTile> tmp = new ArrayList<>();

        for (PopeTile pt : toBeCloned) {
            tmp.add(pt.cloneTile());
        }

        return tmp;
    }

    /**
     * Returns the victory points given by the position of the player in the Faith track. If the player has reached or surpassed a
     * space that gives victory points which are different from 0, then the player gains that number of victory points (that is, if the player
     * is in a cell which gives 0 points, the player gains the victory points given by the last cell they surpassed which gives victory
     * points that differ from 0)
     *
     * @return the victory points the player gains
     */
    public int getCellPoints() {
        if (faithTrack.getCellVictoryPoints(this.position) != 0)
            return faithTrack.getCellVictoryPoints(this.position);

        //Go down to the last cell the Player visited which gives victory points different from 0
        int i = this.position - 1;
        while (i >= 0) {
            if (faithTrack.getCellVictoryPoints(i) != 0)
                return faithTrack.getCellVictoryPoints(i);
            i--;
        }
        return 0;

    }

    /**
     * Responds to a Vatican Report by changing the state of the PopeTile whose reportNum equals the one of the Vatican Report taking place, according to the
     * position of the player at the time of the activation of the Vatican Report
     *
     * @param reportNum the Vatican Report which is taking place
     * @throws IllegalActionException if this Vatican Report has already been activated
     */
    //Bisogna prestare attenzione al fatto che se cerchiamo di attivare due volte lo stesso vatican report (con lo stesso ReportNum) le
    //popeCell lanciano eccezioni
    public void dealWithVaticanReport(ReportNum reportNum) throws IllegalActionException {
        //The position in the PopeTiles list of the PopeTile which has the required reportNum is equal to the position of the required reportNum in the ReportNumOrder list stored in the FaithTrack
        popeTiles.get(faithTrack.getPopeTileIndex(reportNum)).dealWithVaticanReport(reportNum, faithTrack.callCellActivateTile(this.position, reportNum));
    }

    /**
     * Returns the points given by all the PopeTiles this player has
     *
     * @return the points of the PopeTiles
     */
    public int getPopeTilesPoints() {
        int points = 0;
        for (PopeTile pt : this.popeTiles)
            points = points + pt.getPoints();
        return points;
    }

    //This method is only used for testing purposes
    public List<PopeTile> getPopeTiles() {
        return popeTiles;
    }

    public List<PopeTile> getPopeTilesSafe() {
        return this.cloneList(this.popeTiles);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof FaithLevel))
            return false;
        FaithLevel tmp = (FaithLevel) obj;

        if (tmp.isFaithTrackSet != this.isFaithTrackSet)
            return false;
        if (tmp.arePopeTilesSet != this.arePopeTilesSet)
            return false;
        if (tmp.isFaithTrackSet == false) {
            if (tmp.arePopeTilesSet == false)
                return this.position == tmp.position;
            else
                return this.position == tmp.position && this.popeTiles.containsAll(tmp.popeTiles) && tmp.popeTiles.containsAll(this.popeTiles);
        }
        if (tmp.arePopeTilesSet == false)
            return (tmp.position == this.position) && tmp.faithTrack == this.faithTrack;
        return (tmp.position == this.position) && tmp.faithTrack == this.faithTrack && this.popeTiles.containsAll(tmp.popeTiles) && tmp.popeTiles.containsAll(this.popeTiles);
    }
}
