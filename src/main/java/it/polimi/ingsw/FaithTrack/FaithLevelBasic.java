package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represents the generic position of a player in the Faith track. It is mainly used in the solo mode to represent Lorenzo's position in the Faith track.
 * The position stored in this class is always in the range of the corresponding FaithTrack (from 0 to the size of the track minus 1).
 */
public class FaithLevelBasic {
    protected int position;
    protected FaithTrack faithTrack;

    /**
     * Constructs a FaithLevelBasic. Since it represents the position of a player at the beginning of the game, its position is set to 0 (the first Cell of the track).
     * @param faithTrack the FaithTrack the player plays in
     */
    public FaithLevelBasic(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
        this.position = 0;
    }

    /**
     * Returns the position the player is at
     * @return the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * Changes the position of the player according to the number of steps the player takes, given as a input of the method
     * @param step the number of the steps the player takes
     */
    //For extendability purposes, we are assuming the step can be any relative integer
    //This method was used for testing purposes, the final version is the method called moveFaithMarker()
    public void moveFaithMarkerBasicVersion(int step){
        int tmp;
        tmp = this.position + step;
        if(tmp < 0)
            this.position = 0;
        else if(tmp > this.faithTrack.getTrackSize() - 1)
            this.position = this.faithTrack.getTrackSize() - 1;
        else
            this.position = tmp;
    }

    //This method is only used for testing purposes
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Changes the position of the player according to the number of steps the player takes, given as a input of the method
     * @param step the number of the steps the player takes
     * @return true
     * @throws LastVaticanReportException if the last Vatican Report was reached by this player
     */
    //For extendability purposes, we are assuming the step can be any relative integer
    //We have this method returning a boolean for testing purposes
    public boolean moveFaithMarker(int step) throws LastVaticanReportException {
        int start = this.position;
        int end;
        end = this.position + step;
        if(end < 0)
            this.position = 0;
        else if(end > this.faithTrack.getTrackSize() - 1)
            this.position = this.faithTrack.getTrackSize() - 1;
        else
            this.position = end;

        if(end == start)
            return true;
        if(end > start) {
            for (int i = start + 1; i <= position - 1; i++)
                this.faithTrack.callCellEffect(i);
        }
        //TODO: controllare casi con end == start e cosa fare con end < start
        return this.faithTrack.callCellEffect(position);
    }
}
