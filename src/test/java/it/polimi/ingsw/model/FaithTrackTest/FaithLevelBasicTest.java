package it.polimi.ingsw.model.FaithTrackTest;

import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.faithTrack.FaithLevelBasic;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.faithTrack.PopeCell;
import it.polimi.ingsw.model.faithTrack.ReportNumOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaithLevelBasicTest {
    @Test
    //Tests the creation
    public void ctrlCreation() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        assertEquals(faithLevelBasic.getFaithTrack(), ft);
        assertEquals(faithLevelBasic.getPosition(), 0);
    }

    @Test
    //Tests the increasing of the position, controlling it is not going out of bound (here the changing of the position does not call the cells)
    public void ctrlPositionsBasic() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        //the range of the track is [0 ft.getTrackSize()]
        //always in the range
        faithLevelBasic.moveFaithMarkerBasicVersion(5);
        assertEquals(faithLevelBasic.getPosition(), 5);
        faithLevelBasic.moveFaithMarkerBasicVersion(-3);
        assertEquals(faithLevelBasic.getPosition(), 2);
        //going out of the range
        faithLevelBasic.moveFaithMarkerBasicVersion(-ft.getTrackSize() - 10);
        assertEquals(faithLevelBasic.getPosition(), 0);
        faithLevelBasic.moveFaithMarkerBasicVersion(2);
        faithLevelBasic.moveFaithMarkerBasicVersion(ft.getTrackSize() + 10);
        assertEquals(faithLevelBasic.getPosition(), ft.getTrackSize() - 1);
        //finishing exactly on the bounds of the range
        faithLevelBasic.moveFaithMarkerBasicVersion(-2);
        faithLevelBasic.moveFaithMarkerBasicVersion(-(ft.getTrackSize() - 1) + 2);
        assertEquals(faithLevelBasic.getPosition(), 0);
        faithLevelBasic.moveFaithMarkerBasicVersion(+2);
        faithLevelBasic.moveFaithMarkerBasicVersion(+(ft.getTrackSize() - 1) - 2);
        assertEquals(faithLevelBasic.getPosition(), ft.getTrackSize() - 1);
    }

    @Test
    //Tests the increasing of the position, checking what the cell effect returns
    public void ctrlPositionFull() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        assertFalse(((PopeCell) ft.getCellNotSame(8)).isActivated());
        //Landing on a normal Cell
        try {
            assertFalse(faithLevelBasic.moveFaithMarker(4));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertFalse(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Landing on a ReportCell
        try {
            assertFalse(faithLevelBasic.moveFaithMarker(2));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertFalse(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Landing on a PopeCell but not the last one
        try {
            assertTrue(faithLevelBasic.moveFaithMarker(2));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        assertEquals(faithLevelBasic.getPosition(), 8);
        assertTrue(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Landing on the last PopeCell which activates the last Vatican Report
        LastVaticanReportException exception = assertThrows(LastVaticanReportException.class, () -> faithLevelBasic.moveFaithMarker(+ft.getTrackSize() + 10));
        assertTrue(exception.getLastValue());
        assertEquals(exception.getMessage(), "Last Vatican Report was activated!");

    }

    @Test
    //Tests the cloning of FaithLevelBasic
    public void ctrlCloning() throws LastVaticanReportException {
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        FaithLevelBasic original = new FaithLevelBasic(ft);
        original.moveFaithMarker(4);
        FaithLevelBasic clone = new FaithLevelBasic(original);

        assertEquals(clone, original);
        assertEquals(clone.getPosition(), 4);
    }

}
