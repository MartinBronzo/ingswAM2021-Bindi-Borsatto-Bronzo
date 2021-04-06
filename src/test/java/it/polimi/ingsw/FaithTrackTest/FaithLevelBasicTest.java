package it.polimi.ingsw.FaithTrackTest;

import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import org.junit.jupiter.api.Test;

public class FaithLevelBasicTest {
    @Test
    //Tests the creation
    public void ctrlCreation(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        assertEquals(faithLevelBasic.getFaithTrack(), ft);
        assertEquals(faithLevelBasic.getPosition(), 0);
    }

    @Test
    //Tests the increasing of the position, controlling it is not going out of bound (here the changing of the position does not call the cells)
    public void ctrlPositionsBasic(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        //the range of the track is [0 ft.getTrackSize()]
        //always in the range
        faithLevelBasic.moveFaithMarkerBasicVersion(5);
        assertEquals(faithLevelBasic.getPosition(), 5);
        faithLevelBasic.moveFaithMarkerBasicVersion(-3);
        assertEquals(faithLevelBasic.getPosition(), 2);
        //going out of the range
        faithLevelBasic.moveFaithMarkerBasicVersion(-ft.getTrackSize() -10);
        assertEquals(faithLevelBasic.getPosition(), 0);
        faithLevelBasic.moveFaithMarkerBasicVersion(2);
        faithLevelBasic.moveFaithMarkerBasicVersion(ft.getTrackSize() + 10);
        assertEquals(faithLevelBasic.getPosition(), ft.getTrackSize() -1 );
        //finishing exactly on the bounds of the range
        faithLevelBasic.moveFaithMarkerBasicVersion(-2);
        faithLevelBasic.moveFaithMarkerBasicVersion(-(ft.getTrackSize() -1) +2);
        assertEquals(faithLevelBasic.getPosition(), 0);
        faithLevelBasic.moveFaithMarkerBasicVersion(+2);
        faithLevelBasic.moveFaithMarkerBasicVersion(+(ft.getTrackSize() -1) -2);
        assertEquals(faithLevelBasic.getPosition(), ft.getTrackSize() - 1);
    }

    @Test
    //Tests the increasing of the position, checking what the cell
    public void ctrlPositionFull(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        ft.initTrack();
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(ft);

        //Landing on a normal Cell
        try {
            assertFalse(faithLevelBasic.moveFaithMarker(4));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Landing on a ReportCell
        try {
            assertFalse(faithLevelBasic.moveFaithMarker(2));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Landing on a PopeCell but not the last one
        try {
            assertTrue(faithLevelBasic.moveFaithMarker(2));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Landing on the last PopeCell which activates the last Vatican Report
        LastVaticanReportException exception = assertThrows(LastVaticanReportException.class, () -> faithLevelBasic.moveFaithMarker(+ft.getTrackSize() + 10));
        assertEquals(exception.getLastValue(), true);
        assertEquals(exception.getMessage(), "Last Vatican Report was activated!");

    }

}
