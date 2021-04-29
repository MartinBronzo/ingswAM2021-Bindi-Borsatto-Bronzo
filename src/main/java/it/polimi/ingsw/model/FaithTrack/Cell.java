package it.polimi.ingsw.model.FaithTrack;

/**
 * Cell class is used to represent the cells on the FaithTrack
 */
public class Cell {

    private int victoryPoints;
    private ReportNum reportNum;

    /**
     * Constructs a cell
     *
     * @param victoryPoints the points of the cell
     * @param reportNum     the vatican report the cell belongs to
     */
    public Cell(int victoryPoints, ReportNum reportNum) {
        this.victoryPoints = victoryPoints;
        this.reportNum = reportNum;
    }

    /**
     * Gets the victory points of the cells in order to calculate the final points of the player
     *
     * @return the victory points of the cells
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Gets the ReportNum of this cell
     *
     * @return the ReportNum of the cell
     */
    public ReportNum getReportNum() {
        return reportNum;
    }

    /**
     * Activates the effect of the cell. Normal cells have no effect
     *
     * @return false
     */
    //We have this method returning a boolean because we need the boolean for testing purposes
    public boolean effect() {
        return false;
    }

    /**
     * States whether this cell lets the player turn the PopeTile for the activated Vatican Report
     *
     * @param currentReportNum the activated Vatican Report
     * @return true if the Tile needs to be turned, false otherwise
     */
    public boolean activatePopeTile(ReportNum currentReportNum) {
        return false;
    }

    public Cell(Cell original) {
        this(original.victoryPoints, original.reportNum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Cell))
            return false;
        if (obj instanceof ReportCell)
            return false;
/*        if(obj instanceof PopeCell)
            return false;*/
        Cell tmp = (Cell) obj;
        return this.victoryPoints == tmp.victoryPoints && this.reportNum.equals(tmp.reportNum);
    }
}
