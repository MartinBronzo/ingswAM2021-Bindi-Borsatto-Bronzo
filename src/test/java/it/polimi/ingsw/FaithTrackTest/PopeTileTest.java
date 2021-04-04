package it.polimi.ingsw.FaithTrackTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.FaithTrack.PopeTile;
import it.polimi.ingsw.FaithTrack.ReportNum;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

public class PopeTileTest {

    @Test
    //Tests the correct creation of the PopeTile
    public void ctrlTileCreation(){
        PopeTile popeTile= new PopeTile(-1, ReportNum.REPORT1);

        assertEquals(popeTile.getPoints(), 0);
        assertEquals(popeTile.getReportNum(), ReportNum.REPORT1);
        assertFalse(popeTile.isActivated());
        assertFalse(popeTile.isDiscarded());
        assertFalse(popeTile.isChanged());

        try {
            popeTile.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        assertEquals(popeTile.getPoints(), -1);
    }

    @Test
    //Tests the activation and the discard of the PopeTile
    public void ctrlTileActivationAndDiscard(){
        PopeTile popeTile1 = new PopeTile(0, ReportNum.REPORT1);
        PopeTile popeTile2a = new PopeTile(0, ReportNum.REPORT1);
        PopeTile popeTile2b = new PopeTile(0, ReportNum.REPORT1);
        PopeTile popeTile3 = new PopeTile(0, ReportNum.REPORT1);


        try {
            popeTile1.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            popeTile2a.dealWithVaticanReport(ReportNum.REPORT2, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            popeTile2b.dealWithVaticanReport(ReportNum.REPORT2, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            popeTile3.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //popeTile1 needs to be active
        assertTrue(popeTile1.isActivated());
        assertFalse(popeTile1.isDiscarded());
        assertTrue(popeTile1.isChanged());
        //popeTile2a doesn't change its initial inner values because its Vatican Report is not the one taking place
        assertFalse(popeTile2a.isActivated());
        assertFalse(popeTile2a.isDiscarded());
        assertFalse(popeTile2a.isChanged());
        //popeTile2b doesn't change its initial inner values because its Vatican Report is not the one taking place
        assertFalse(popeTile2b.isActivated());
        assertFalse(popeTile2b.isDiscarded());
        assertFalse(popeTile2b.isChanged());
        //popeTile3 needs to be discarded
        assertFalse(popeTile3.isActivated());
        assertTrue(popeTile3.isDiscarded());
        assertTrue(popeTile3.isChanged());

        //PopeTile1 can't be changed again
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT1, true));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT1, false));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT2, true));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT2, false));
        //PopeTile3 can't be changed again
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT1, true));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT1, false));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT2, true));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT2, false));

        try {
            popeTile2a.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            popeTile2b.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //popeTile2a now needs to be active
        assertTrue(popeTile2a.isActivated());
        assertFalse(popeTile2a.isDiscarded());
        assertTrue(popeTile2a.isChanged());
        //popeTile2b needs to be discarded
        assertFalse(popeTile2b.isActivated());
        assertTrue(popeTile2b.isDiscarded());
        assertTrue(popeTile2b.isChanged());

    }

    @Test
    //Tests that activated PopeTiles give their points, discarded and unchanged tile give 0 points
    public void ctlrPointsActiveTiles(){
        PopeTile popeTile1 = new PopeTile(2, ReportNum.REPORT1);
        PopeTile popeTile2 = new PopeTile(2, ReportNum.REPORT1);
        PopeTile popeTile3 = new PopeTile(2, ReportNum.REPORT1);

        try {
            popeTile1.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            popeTile2.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        assertEquals(popeTile1.getPoints(), 2);
        assertEquals(popeTile2.getPoints(), 0);
        assertEquals(popeTile3.getPoints(), 0);

    }
}
