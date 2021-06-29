package it.polimi.ingsw.model.faithTrack;

/**
 * ReportCell class represents a particular subsets of cells: they belong to the Vatican Report Sections. They
 * differ from normal Cells because of the different value the method activatePopeTile returns.
 */
public class ReportCell extends Cell {

    /**
     * Constructs a cell
     *
     * @param victoryPoints the points of the cell
     * @param reportNum     the vatican report the cell belongs to
     */
    public ReportCell(int victoryPoints, ReportNum reportNum) {
        super(victoryPoints, reportNum);
    }

    /**
     * Activates the effect of the cell. ReportCells have no effect
     *
     * @return false
     */
    @Override
    public boolean effect() {
        return super.effect();
    }

    /**
     * States whether this cell lets the player turn the PopeTile for the activated Vatican Report
     *
     * @param currentReportNum the activated Vatican Report
     * @return true if this cell belongs to the activated Vatican Report, false otherwise
     */

    @Override
    public boolean activatePopeTile(ReportNum currentReportNum) {
        return currentReportNum.equals(this.getReportNum());
    }

    /**
     * Constructs a ReportCell which is the copy of the specified ReportCell
     * @param original a ReportCell to be cloned
     */
    public ReportCell(ReportCell original) {
        this(original.getVictoryPoints(), original.getReportNum());
    }

    @Override
    public Cell getClone() {
        return new ReportCell(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ReportCell))
            return false;
        if (obj instanceof PopeCell)
            return false;
        ReportCell tmp = (ReportCell) obj;
        return this.getVictoryPoints() == tmp.getVictoryPoints() && this.getReportNum().equals(tmp.getReportNum());
    }

    @Override
    public boolean lighterEquals(Object obj) {
        return this.equals(obj);
    }
}
