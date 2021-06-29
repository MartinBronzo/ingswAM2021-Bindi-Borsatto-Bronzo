package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represents the generic position of a player in the Faith track. It is mainly used in the solo mode to represent Lorenzo's position in the Faith track.
 * The position stored in this class is always in the range of the corresponding FaithTrack (from 0 to the size of the track minus 1).
 */
public class FaithLevelBasic {
    protected int position;
    protected FaithTrack faithTrack;
    protected boolean isFaithTrackSet;

    /**
     * Constructs a FaithLevelBasic. Since it represents the position of a player at the beginning of the game, its position is set to 0 (the first Cell of the track).
     *
     * @param faithTrack the FaithTrack the player plays in
     */
    public FaithLevelBasic(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
        this.position = 0;
        this.isFaithTrackSet = true;
    }

    /**
     * Constructs a FaithLevelBasic without the FaithTrack set
     */
    public FaithLevelBasic() {
        this.position = 0;
        this.faithTrack = null;
        this.isFaithTrackSet = false;
    }

    /**
     * Constructs a clone of a FaithLevelBasic which is already set (it has a corresponding FaithTrack)
     *
     * @param original the FaithLevelBasic to be cloned
     */
    public FaithLevelBasic(FaithLevelBasic original) {
        this.faithTrack = original.faithTrack;
        this.position = original.position;
        this.isFaithTrackSet = original.isFaithTrackSet;
    }

    /**
     * Sets the FaithTrack this FaithLevelBasic belongs to if it hasn't been set, yet
     *
     * @param faithTrack a FaithTrack
     */
    public void setFaithTrack(FaithTrack faithTrack) {
        if (!isFaithTrackSet) {
            this.faithTrack = faithTrack;
            this.isFaithTrackSet = true;
        }
    }

    /**
     * Returns the position the player is at
     *
     * @return the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * Changes the position of the player according to the number of steps the player takes, given as a input of the method
     *
     * @param step the number of the steps the player takes
     */
    //For extendability purposes, we are assuming the step can be any relative integer
    //This method was used for testing purposes, the final version is the method called moveFaithMarker()
    //It was used to make sure that the final position of the Markers was right
    public void moveFaithMarkerBasicVersion(int step) {
        int tmp;
        tmp = this.position + step;
        if (tmp < 0)
            this.position = 0;
        else this.position = Math.min(tmp, this.faithTrack.getTrackSize() - 1);
    }

    /**
     * Returns the FaithTrack this FaithLevelBasic belongs to
     *
     * @return the FaithTrack of the FaithLevelBasic
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Changes the position of the player according to the number of steps the player takes, given as a input of the method
     *
     * @param step the number of the steps the player takes
     * @return the boolean given return by the effect method of the final cell the player lands on
     * @throws LastVaticanReportException if the last Vatican Report was reached by this player
     */
    //For extendability purposes, we are assuming the step can be any relative integer
    //We have this method returning a boolean for testing purposes
    public boolean moveFaithMarker(int step) throws LastVaticanReportException {
        int start = this.position;
        int end = this.position + step;
        if (end < 0)
            end = 0;
        else if (end > this.faithTrack.getTrackSize() - 1)
            end = this.faithTrack.getTrackSize() - 1;

        if (end == start) {
            //The player hasn't changed their position, therefore the effect of the cell isn't called
            this.position = end;
            return true;
        } else if (end > start) {
            for (int i = start + 1; i <= end - 1; i++) {
                this.position = i;
                this.faithTrack.callCellEffect(i);
            }
            this.position = end;
            return this.faithTrack.callCellEffect(end);
        }
        //If the player moves back no effect is called
        this.position = end;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof FaithLevelBasic))
            return false;
        if (obj instanceof FaithLevel)
            return false;
        FaithLevelBasic tmp = (FaithLevelBasic) obj;
        if (!tmp.isFaithTrackSet)
            if (!this.isFaithTrackSet)
                return (tmp.position == this.position);
            else
                return false;
        return (tmp.position == this.position) && tmp.faithTrack == this.faithTrack;
    }
}
