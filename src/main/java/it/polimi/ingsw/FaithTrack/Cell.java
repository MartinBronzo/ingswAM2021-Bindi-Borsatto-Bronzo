package it.polimi.ingsw.FaithTrack;

/**
 * Cell class is used to represent the cells on the FaithTrack
 */
public class Cell {

    private int vicoryPoints;
    private ReportNum reportNum;

    /**
     * Constructs a cell
     * @param vicoryPoints the points of the cell
     * @param reportNum the vatican report the cell belongs to
     */
    public Cell(int vicoryPoints, ReportNum reportNum) {
        this.vicoryPoints = vicoryPoints;
        this.reportNum = reportNum;
    }

    /**
     * Gets the victory points of the cells in order to calculate the final points of the player
     * @return the victory points of the cells
     */
    public int getVicoryPoints() {
        return vicoryPoints;
    }

    /**
     * Gets the ReportNum of this cell
     * @return the ReportNum of the cell
     */
    public ReportNum getReportNum() {
        return reportNum;
    }

    /**
     * Activates the effect of the cell. Normal cells have no effect
     * @return false
     */
    //We have this method returning a boolean because we need the boolean for testing purposes
    public boolean effect(){
        return false;
    }

    /**
     * States whether this cell lets the player turn the PopeTile for the activated Vatican Report
     * @param currentReportNum the activated Vatican Report
     * @return true if the Tile needs to be turned, false otherwise
     */
    public boolean activatePopeTile(ReportNum currentReportNum, ReportNumOrder reportNumOrder){
        /*if(reportNumOrder.stateOrder(currentReportNum, this.reportNum))
            return true;
        return false;*/
        return false;
    }
}
