package it.polimi.ingsw.FaithTrack;

/**
 * ReportCell class represents a particular subsets of cells: they belong to the Vatican Report Sections. They
 * differ from normal Cells because of the different value the method activatePopeTile returns.
 */
public class ReportCell extends Cell{

    /**
     * Constructs a cell
     *
     * @param vicoryPoints the points of the cell
     * @param reportNum    the vatican report the cell belongs to
     */
    public ReportCell(int vicoryPoints, ReportNum reportNum) {
        super(vicoryPoints, reportNum);
    }

    /**
     * Activates the effect of the cell. ReportCells have no effect
     * @return false
     */
    @Override
    public boolean effect() {
        return super.effect();
    }

    /**
     * States whether this cell lets the player turn the PopeTile for the activated Vatican Report
     * @param currentReportNum the activated Vatican Report
     * @return true if this cell belongs to the activated Vatican Report, false otherwise
     */

    @Override
    public boolean activatePopeTile(ReportNum currentReportNum, ReportNumOrder reportNumOrder) {
        if(reportNumOrder.stateOrder(currentReportNum, this.getReportNum()))
            return true;
        if(currentReportNum.equals(this.getReportNum()))
            return true;
        return false;
    }
}
