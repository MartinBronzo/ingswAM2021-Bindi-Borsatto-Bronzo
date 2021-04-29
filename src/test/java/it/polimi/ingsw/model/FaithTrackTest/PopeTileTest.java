package it.polimi.ingsw.model.FaithTrackTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopeTileTest {

    @Test
    //Tests the correct creation of the PopeTile
    public void ctrlTileCreation() {
        PopeTile popeTile = new PopeTile(-1, ReportNum.REPORT1);

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
    //Tests the activation of the PopeTile
    public void ctrlTileActivation() {
        PopeTile popeTile1 = new PopeTile(0, ReportNum.REPORT1);
        PopeTile popeTile2a = new PopeTile(0, ReportNum.REPORT1);
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

        //popeTile1 needs to be active
        assertTrue(popeTile1.isActivated());
        assertFalse(popeTile1.isDiscarded());
        assertTrue(popeTile1.isChanged());
        //popeTile2a doesn't change its initial inner values because its Vatican Report is not the one taking place
        assertFalse(popeTile2a.isActivated());
        assertFalse(popeTile2a.isDiscarded());
        assertFalse(popeTile2a.isChanged());


        //PopeTile1 can't be changed again
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT1, true));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT1, false));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT2, true));
        assertThrows(IllegalActionException.class, () -> popeTile1.dealWithVaticanReport(ReportNum.REPORT2, false));

        try {
            popeTile2a.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        //popeTile2a now needs to be active
        assertTrue(popeTile2a.isActivated());
        assertFalse(popeTile2a.isDiscarded());
        assertTrue(popeTile2a.isChanged());


    }

    @Test
    //Tests the discard of the PopeTile
    public void ctrlTileDiscard() {
        PopeTile popeTile2b = new PopeTile(0, ReportNum.REPORT1);
        PopeTile popeTile3 = new PopeTile(0, ReportNum.REPORT1);

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
        //popeTile2b doesn't change its initial inner values because its Vatican Report is not the one taking place
        assertFalse(popeTile2b.isActivated());
        assertFalse(popeTile2b.isDiscarded());
        assertFalse(popeTile2b.isChanged());
        //popeTile3 needs to be discarded
        assertFalse(popeTile3.isActivated());
        assertTrue(popeTile3.isDiscarded());
        assertTrue(popeTile3.isChanged());

        //PopeTile3 can't be changed again
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT1, true));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT1, false));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT2, true));
        assertThrows(IllegalActionException.class, () -> popeTile3.dealWithVaticanReport(ReportNum.REPORT2, false));


        try {
            popeTile2b.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        //popeTile2b needs to be discarded
        assertFalse(popeTile2b.isActivated());
        assertTrue(popeTile2b.isDiscarded());
        assertTrue(popeTile2b.isChanged());
    }

    @Test
    //Tests that activated PopeTiles give their points, discarded and unchanged tile give 0 points
    public void ctrlPointsActiveTiles() {
        PopeTile popeTile1 = new PopeTile(2, ReportNum.REPORT1);

        try {
            popeTile1.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        assertEquals(popeTile1.getPoints(), 2);
    }

    @Test
    public void ctrlPointsUnchangedTiles() {
        PopeTile popeTile3 = new PopeTile(2, ReportNum.REPORT1);
        assertEquals(popeTile3.getPoints(), 0);
    }

    @Test
    public void ctrlPointsDiscardedTiles() {
        PopeTile popeTile2 = new PopeTile(2, ReportNum.REPORT1);
        try {
            popeTile2.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertEquals(popeTile2.getPoints(), 0);
    }

    @Test
    public void ctrlEqualsTrue() {
        PopeTile p1 = new PopeTile(3, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT1);

        assertEquals(p1, p2);
        assertEquals(p1.getPoints(), p2.getPoints());
        assertEquals(p1.getReportNum(), p2.getReportNum());
        assertEquals(p1.isActivated(), p2.isActivated());
        assertEquals(p1.isDiscarded(), p2.isDiscarded());
        assertEquals(p1.isChanged(), p2.isChanged());

        try {
            p1.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            p2.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertEquals(p1, p2);
        assertTrue(p2.isDiscarded());
        assertTrue(p1.isDiscarded());
    }

    @Test
    public void ctrlEqualsFalse() {
        PopeTile p1 = new PopeTile(3, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(2, ReportNum.REPORT1);
        PopeTile p3 = new PopeTile(3, ReportNum.REPORT2);

        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);

        PopeTile p4 = new PopeTile(3, ReportNum.REPORT1);
        try {
            p1.dealWithVaticanReport(ReportNum.REPORT1, false);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertNotEquals(p1, p4);
        assertTrue(p1.isDiscarded());
        assertFalse(p4.isDiscarded());
    }

    @Test
    public void ctrlCloningNoActiveTile() {
        PopeTile p1 = new PopeTile(3, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(p1);

        assertNotSame(p1, p2);
        assertEquals(p1, p2);
        assertEquals(p1.isChanged(), p2.isChanged());
        assertFalse(p1.isChanged());
    }

    @Test
    public void ctrlCloningActiveTile() {
        PopeTile p1 = new PopeTile(3, ReportNum.REPORT1);
        try {
            p1.dealWithVaticanReport(ReportNum.REPORT1, true);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        PopeTile p2 = new PopeTile(p1);

        assertNotSame(p1, p2);
        assertEquals(p1, p2);
        assertEquals(p1.isActivated(), p2.isActivated());
        assertTrue(p1.isActivated());
    }

}
