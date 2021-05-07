package it.polimi.ingsw.model.FaithTrackTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.FaithTrack.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FaithLevelTest {
    @Test
    //Tests the creation, controlling that the given list of PopeTiles matches with the one stored in teh FaithLevel (not only because
    //they both contain the same elements, but also because they are in the same order)
    public void ctrlCreation() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(0, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(0, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(0, ReportNum.REPORT3));

        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);

        assertSame(ft, faithLevel.getFaithTrack());
        assertEquals(faithLevel.getPosition(), 0);

        //Tests that the PopeTile list stored in the FaithLevel is a clone of the original
        List<PopeTile> myPopeTiles = faithLevel.getPopeTiles();
        assertNotSame(popeTiles, myPopeTiles);
        assertTrue(popeTiles.containsAll(myPopeTiles));
        assertTrue(myPopeTiles.containsAll(popeTiles));
        PopeTile tmp;
        int i = 0;
        for (PopeTile pt : popeTiles) {
            tmp = myPopeTiles.get(popeTiles.indexOf(pt));
            assertEquals(popeTiles.indexOf(pt), i);
            assertEquals(pt, tmp);
            assertNotSame(pt, tmp);
            i++;
        }
        assertEquals(popeTiles.size(), myPopeTiles.size());
    }

    @Test
    //Tests FaithLevels give the right victory points when called: the cell does give points
    public void ctrlCellPointsSomePoints() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        List<PopeTile> popeTiles = new ArrayList<>();
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);

        //The Player is on a cell which gives points
        faithLevel.moveFaithMarkerBasicVersion(9);
        assertEquals(faithLevel.getCellPoints(), ft.getCellVictoryPoints(9));
    }

    @Test
    //Tests FaithLevels give the right victory points when called: boundaries edition
    public void ctrlCellPointsBounds() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        List<PopeTile> popeTiles = new ArrayList<>();
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);

        //Checking the bounds
        faithLevel.moveFaithMarkerBasicVersion(+ft.getTrackSize() + 10); //The FaithMarker is on the last cell
        assertEquals(faithLevel.getCellPoints(), ft.getCellVictoryPoints(+ft.getTrackSize() - 1));
        faithLevel.moveFaithMarkerBasicVersion(-ft.getTrackSize() - 10); //The FaithMarker is on the first cell
        assertEquals(faithLevel.getCellPoints(), ft.getCellVictoryPoints(0));
        faithLevel.moveFaithMarkerBasicVersion(2);
        assertEquals(faithLevel.getCellPoints(), ft.getCellVictoryPoints(0));
    }

    @Test
    //Tests FaithLevels give the right victory points when called: the cell doesn't give points
    public void ctrlCellPointsNoPoints() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        List<PopeTile> popeTiles = new ArrayList<>();
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);

        //The Player is in a cell which doesn't give points
        faithLevel.moveFaithMarkerBasicVersion(5);//Cell 5
        assertEquals(faithLevel.getPosition(), 5);
        assertEquals(faithLevel.getCellPoints(), ft.getCellVictoryPoints(3));
    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: three not changed tiles
    public void ctrlPopeTilesPointsNotChangedTiles() throws IllegalActionException {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);

        //Three not changed tiles
        assertEquals(faithLevel.getPopeTilesPoints(), 0);

    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: one discarded, one active, one not changed
    public void ctrlPopeTilesPointsOneForEachType() throws IllegalActionException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);

        //1 discarded, 1 active, 1 not changed
        p1.dealWithVaticanReport(ReportNum.REPORT1, false);
        p2.dealWithVaticanReport(ReportNum.REPORT2, true);
        popeTiles = new ArrayList<>();
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        assertEquals(faithLevel.getPopeTilesPoints(), 3);
    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: three active tiles
    public void ctrlPopeTilesPointsThreeActive() throws IllegalActionException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);

        //Three active tiles
        p1.dealWithVaticanReport(ReportNum.REPORT1, true);
        p2.dealWithVaticanReport(ReportNum.REPORT2, true);
        p3.dealWithVaticanReport(ReportNum.REPORT3, true);
        popeTiles = new ArrayList<>();
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        assertEquals(faithLevel.getPopeTilesPoints(), 12);
    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: one active, two not changed
    public void ctrlPopeTilesPointsOneActiveTwoNotChanged() throws IllegalActionException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        //active, not changed, not changed
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);
        p1.dealWithVaticanReport(ReportNum.REPORT1, true);
        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        assertEquals(faithLevel.getPopeTilesPoints(), 4);

    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: three discarded tiles
    public void ctrlPopeTilesPointsThreeDiscarded() throws IllegalActionException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);

        //Three discarded tiles
        p1.dealWithVaticanReport(ReportNum.REPORT1, false);
        p2.dealWithVaticanReport(ReportNum.REPORT2, false);
        p3.dealWithVaticanReport(ReportNum.REPORT3, false);
        popeTiles = new ArrayList<>();
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        assertEquals(faithLevel.getPopeTilesPoints(), 0);
    }

    @Test
    //Tests the PopeTiles in the FaithLevels give the right victory points when called: One discarded, two not changed
    public void ctrlPopeTilesPointsOneDiscardedTwoNotChanged() throws IllegalActionException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);

        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile p1 = new PopeTile(4, ReportNum.REPORT1);
        PopeTile p2 = new PopeTile(3, ReportNum.REPORT2);
        PopeTile p3 = new PopeTile(5, ReportNum.REPORT3);

        //We manually force the PopeTiles to change their status to try some combinations of discarded, active and not changed tiles

        //Discarded, not changed, not changed
        p1.dealWithVaticanReport(ReportNum.REPORT1, false);
        popeTiles = new ArrayList<>();
        popeTiles.add(p1);
        popeTiles.add(p2);
        popeTiles.add(p3);
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        assertEquals(faithLevel.getPopeTilesPoints(), 0);
    }

    //The three next test test how the tiles respond to a Vatican Report (also checking what happens when we call an already activated Vatican Report)

    @Test
    //Tests how the normal Cell respond to a Vatican Report
    public void ctrlVaticanReportReactionNormalCell() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        List<PopeTile> myPT = faithLevel.getPopeTiles();
        tmp.attach(new ControllerStub(tmp, faithLevel));

        //Controlling the behavior of normal Cell
        try {
            assertFalse(faithLevel.moveFaithMarker(11)); //Player is in a normal Cell whose ReportNum is REPORTNUM2
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //The PopeTile with ReportNum1 is active, the others are still unchanged
        assertTrue(myPT.get(0).isActivated());
        assertFalse(myPT.get(1).isChanged());
        assertFalse(myPT.get(2).isChanged());

        //VaticanReport with the same ReportNum as the one of the Cell
        try {
            faithLevel.dealWithVaticanReport(ReportNum.REPORT2);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is discarded, the last one is unchanged
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isDiscarded());
        assertFalse(myPT.get(2).isChanged());

        //VaticanReport with a higher ReportNum than the one of the Cell
        try {
            faithLevel.dealWithVaticanReport(ReportNum.REPORT3);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is still discarded, the one with ReportNum3 is discarded
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isDiscarded());
        assertTrue(myPT.get(2).isDiscarded());

        Exception exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT1));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT1");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT2));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT2");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT3));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT3");
    }

    @Test
    //Tests how the ReportCell respond to a Vatican Report
    public void ctrlVaticanReportReactionReportCell() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));


        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        List<PopeTile> myPT = faithLevel.getPopeTiles();
        tmp.attach(new ControllerStub(tmp, faithLevel));

        //Controlling the behavior of normal Cell
        try {
            faithLevel.moveFaithMarker(13); //Player is in a ReportCell whose ReportNum is REPORTNUM2
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //The PopeTile with ReportNum1 is active, the others are still unchanged
        assertTrue(myPT.get(0).isActivated());
        assertFalse(myPT.get(1).isChanged());
        assertFalse(myPT.get(2).isChanged());

        //VaticanReport with the same ReportNum as the one of the Cell
        try {
            faithLevel.dealWithVaticanReport(ReportNum.REPORT2);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is active, the last one is unchanged
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isActivated());
        assertFalse(myPT.get(2).isChanged());

        //VaticanReport with a higher ReportNum than the one of the Cell
        try {
            faithLevel.dealWithVaticanReport(ReportNum.REPORT3);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is still active, the one with ReportNum3 is discarded
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isActivated());
        assertTrue(myPT.get(2).isDiscarded());

        Exception exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT1));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT1");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT2));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT2");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT3));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT3");
    }

    @Test
    //Tests how the PopeCell respond to a Vatican Report
    public void ctrlVaticanReportReactionPopeCell() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        List<PopeTile> myPT = faithLevel.getPopeTiles();
        tmp.attach(new ControllerStub(tmp, faithLevel));

        tmp = (PopeCell) ft.getCellNotSame(16);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(new ControllerStub(tmp, faithLevel));

        //Controlling the behavior of normal Cell
        try {
            faithLevel.moveFaithMarker(16); //Player is in a ReportCell whose ReportNum is REPORTNUM2
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is active, the last one is unchanged
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isActivated());
        assertFalse(myPT.get(2).isChanged());

        //VaticanReport with a higher ReportNum than the one of the Cell
        try {
            faithLevel.dealWithVaticanReport(ReportNum.REPORT3);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        //The PopeTile with ReportNum1 is still active, the one with ReportNum2 is still active, the one with ReportNum3 is discarded
        assertTrue(myPT.get(0).isActivated());
        assertTrue(myPT.get(1).isActivated());
        assertTrue(myPT.get(2).isDiscarded());

        Exception exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT1));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT1");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT2));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT2");
        exception = assertThrows(IllegalActionException.class, () -> faithLevel.dealWithVaticanReport(ReportNum.REPORT3));
        assertEquals(exception.getMessage(), "This tile has already been changed. ReportNum: REPORT3");
    }

    @Test
    //Tests a Vatican Report basic version: the player lands on the PopeTiles
    public void ctrlVaticanReportBasic() {
        //The first two lines are needed to make sure that in this method the first and only instances of these two classes are created
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel1 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT1 = faithLevel1.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 2, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 2, ReportNum.REPORT3));
        FaithLevel faithLevel2 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT2 = faithLevel2.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 3, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 3, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 3, ReportNum.REPORT3));
        FaithLevel faithLevel3 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT3 = faithLevel3.getPopeTiles();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));
        ControllerStub controllerStub = new ControllerStub(tmp, faithLevel1, faithLevel2, faithLevel3);
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(16);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(24);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);


        try {
            faithLevel1.moveFaithMarker(2);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel2.moveFaithMarker(5);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel3.moveFaithMarker(8);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //First VaticanReport
        assertTrue(pT1.get(0).isDiscarded());
        assertTrue(pT2.get(0).isActivated());
        assertTrue(pT3.get(0).isActivated());

        try {
            faithLevel1.moveFaithMarker(8);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel2.moveFaithMarker(8);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel3.moveFaithMarker(8);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Second VaticanReport
        assertTrue(pT1.get(1).isDiscarded());
        assertTrue(pT2.get(1).isActivated());
        assertTrue(pT3.get(1).isActivated());

        try {
            faithLevel1.moveFaithMarker(7);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel2.moveFaithMarker(7);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertThrows(LastVaticanReportException.class, () -> faithLevel3.moveFaithMarker(+ft.getTrackSize() + 10));

        //Third VaticanReport
        assertTrue(pT1.get(2).isDiscarded());
        assertTrue(pT2.get(2).isActivated());
        assertTrue(pT3.get(2).isActivated());
    }

    @Test
    //Tests that the Vatican Report full version: it is activated because the player lands at the end of all of their steps after a PopeTile
    //Tests with three "human" players
    public void ctrlVaticanReportFull() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel1 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT1 = faithLevel1.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 2, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 2, ReportNum.REPORT3));
        FaithLevel faithLevel2 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT2 = faithLevel2.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 3, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 3, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 3, ReportNum.REPORT3));
        FaithLevel faithLevel3 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT3 = faithLevel3.getPopeTiles();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));
        ControllerStub controllerStub = new ControllerStub(tmp, faithLevel1, faithLevel2, faithLevel3);
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(16);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(24);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);

        try {
            faithLevel1.moveFaithMarker(2);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel2.moveFaithMarker(5);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel3.moveFaithMarker(9);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //First VaticanReport
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));
        assertTrue(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Let's cross again the PopeTile
        try {
            faithLevel2.moveFaithMarker(8); //Player2 crosses the PopeTile which already activated the Vatican Report
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Controlling all tiles are exactly as they were before
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));

        //Let's land on the PopeTile
        try {
            assertFalse(faithLevel1.moveFaithMarker(6)); //Player1 lands on the PopeTile which already activated the Vatican Report
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Controlling all tiles are exactly as they were before
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));

        //Let's activate the remaining two VaticanReport together
        LastVaticanReportException exception = assertThrows(LastVaticanReportException.class, () -> faithLevel3.moveFaithMarker(+ft.getTrackSize() + 10));
        assertTrue(exception.getLastValue());
        //Controlling the tiles
        assertTrue(pT1.get(0).isDiscarded());
        assertTrue(pT1.get(1).isDiscarded());
        assertTrue((pT1.get(2).isDiscarded()));
        assertTrue(pT2.get(0).isActivated());
        assertTrue(pT2.get(1).isActivated()); //Player 2 is in a ReportCell fot the Second Vatican Report
        assertTrue((pT2.get(2).isDiscarded()));
        assertTrue(pT3.get(0).isActivated());
        assertTrue(pT3.get(1).isActivated());
        assertTrue(pT3.get(2).isActivated());
    }

    @Test
    //Tests that the Vatican Report full version: it is activated because the player lands at the end of all of their steps after a PopeTile
    //Tests with three "human" players and Lorenzo
    public void ctrlVaticanReportFullWithLorenzo() {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        //ft.initTrack();

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel1 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT1 = faithLevel1.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 2, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 2, ReportNum.REPORT3));
        FaithLevel faithLevel2 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT2 = faithLevel2.getPopeTiles();
        popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1 * 3, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2 * 3, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3 * 3, ReportNum.REPORT3));
        FaithLevel faithLevel3 = new FaithLevel(ft, popeTiles);
        List<PopeTile> pT3 = faithLevel3.getPopeTiles();
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));
        ControllerStub controllerStub = new ControllerStub(tmp, faithLevel1, faithLevel2, faithLevel3);
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(16);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);
        tmp = (PopeCell) ft.getCellNotSame(24);
        tmp.detach(tmp.getObserversList().get(0));
        tmp.attach(controllerStub);

        FaithLevelBasic lorenzoFaithLevel = new FaithLevelBasic(ft);

        try {
            faithLevel1.moveFaithMarker(2);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel2.moveFaithMarker(5);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            faithLevel3.moveFaithMarker(9);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //First VaticanReport
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));
        assertTrue(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Let's cross again the PopeTile
        try {
            faithLevel2.moveFaithMarker(8); //Player2 crosses the PopeTile which already activated the Vatican Report
            //"The Vatican Report REPORT1 has already been activated!";
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Controlling all tiles are exactly as they were before
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));
        assertTrue(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Let's land on the PopeTile
        try {
            assertFalse(faithLevel1.moveFaithMarker(6)); //Player1 lands on the PopeTile which already activated the Vatican Report
            //"The Vatican Report REPORT1 has already been activated!";
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        try {
            assertFalse(faithLevel1.moveFaithMarker(1));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Controlling all tiles are exactly as they were before
        assertTrue(pT1.get(0).isDiscarded());
        assertFalse(pT1.get(1).isChanged());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated());
        assertFalse(pT2.get(1).isChanged());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated());
        assertFalse(pT3.get(1).isChanged());
        assertFalse((pT3.get(2).isChanged()));
        assertTrue(((PopeCell) ft.getCellNotSame(8)).isActivated());

        //Let Lorenzo activate the second Vatican Report by landing on it
        try {
            assertTrue(lorenzoFaithLevel.moveFaithMarker(16));
            //"The Vatican Report REPORT1 has already been activated!";
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        //Controlling all tiles
        assertTrue(pT1.get(0).isDiscarded()); //Player1 is on the 8th cell
        assertTrue(pT1.get(1).isDiscarded());
        assertFalse((pT1.get(2).isChanged()));
        assertTrue(pT2.get(0).isActivated()); //Player2 is on the 13rd cell
        assertTrue(pT2.get(1).isActivated());
        assertFalse((pT2.get(2).isChanged()));
        assertTrue(pT3.get(0).isActivated()); //Player3 is on the 9th cell
        assertTrue(pT3.get(1).isDiscarded());
        assertFalse((pT3.get(2).isChanged()));
        assertTrue(((PopeCell) ft.getCellNotSame(16)).isActivated());

        //Let's activate the remaining VaticanReport together by crossing also the second Vatican Report
        LastVaticanReportException exception = assertThrows(LastVaticanReportException.class, () -> faithLevel3.moveFaithMarker(+ft.getTrackSize() + 10));
        //"The Vatican Report REPORT2 has already been activated!";
        assertTrue(exception.getLastValue());
        assertTrue(((PopeCell) ft.getCellNotSame(24)).isActivated());
        //Controlling the tiles
        assertTrue(pT1.get(0).isDiscarded()); //Player1 is on the 8th cell
        assertTrue(pT1.get(1).isDiscarded());
        assertTrue((pT1.get(2).isDiscarded()));
        assertTrue(pT2.get(0).isActivated()); //Player2 is on the 13rd cell
        assertTrue(pT2.get(1).isActivated());
        assertTrue((pT2.get(2).isDiscarded()));
        assertTrue(pT3.get(0).isActivated()); //Player3 is on the 24th cell
        assertTrue(pT3.get(1).isDiscarded());
        assertTrue((pT3.get(2).isActivated()));
    }

    @Test
    //Tests particular values for the steps the Marker takes: zero steps
    public void ctrlMarkerStepsZeroStep() throws LastVaticanReportException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        popeTiles = faithLevel.getPopeTiles();
        tmp.attach(new ControllerStub(tmp, faithLevel));

        assertFalse(popeTiles.get(0).isChanged());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());

        assertFalse(faithLevel.moveFaithMarker(9));

        assertEquals(faithLevel.getPosition(), 9);

        assertTrue(popeTiles.get(0).isActivated());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());

        assertTrue(faithLevel.moveFaithMarker(0));

        assertEquals(faithLevel.getPosition(), 9);

        //The tiles haven't changed
        assertTrue(popeTiles.get(0).isActivated());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());
    }

    @Test
    //Tests particular values for the steps the Marker takes: negative steps
    public void ctrlMarkerStepsNegativeSteps() throws LastVaticanReportException {
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        PopeCell tmp = (PopeCell) ft.getCellNotSame(8);
        tmp.detach(tmp.getObserversList().get(0));

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel faithLevel = new FaithLevel(ft, popeTiles);
        popeTiles = faithLevel.getPopeTiles();
        tmp.attach(new ControllerStub(tmp, faithLevel));

        assertFalse(popeTiles.get(0).isChanged());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());

        assertFalse(faithLevel.moveFaithMarker(9));

        assertEquals(faithLevel.getPosition(), 9);

        assertTrue(popeTiles.get(0).isActivated());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());

        assertTrue(faithLevel.moveFaithMarker(-4));

        assertEquals(faithLevel.getPosition(), 5);

        //The tiles haven't changed
        assertTrue(popeTiles.get(0).isActivated());
        assertFalse(popeTiles.get(1).isChanged());
        assertFalse(popeTiles.get(2).isChanged());
    }

    @Test
    //Tests the cloning of FaithLevels
    public void ctrlCloning() {
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack ft = FaithTrack.instance(reportNumOrder);
        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(1, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(2, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(3, ReportNum.REPORT3));
        FaithLevel original = new FaithLevel(ft, popeTiles);

        FaithLevel clone = new FaithLevel(original);
        assertNotSame(clone, original);
        assertEquals(clone, original);
    }
}
