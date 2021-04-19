package it.polimi.ingsw.PlayerBoardTest;

import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FaithLevelPlayerBoardMethodsTestLB {
    PlayerBoard playerBoard;
    List<PopeTile> popeTileList;
    ReportNumOrder reportNumOrder;
    FaithTrack ft;

    @BeforeEach
    public void setup(){
        playerBoard = new PlayerBoard();
        popeTileList = new ArrayList<>();
        popeTileList.add(new PopeTile(1, ReportNum.REPORT1));
        popeTileList.add(new PopeTile(10, ReportNum.REPORT2));
        popeTileList.add(new PopeTile(3, ReportNum.REPORT3));
        ReportNumOrder.deleteState();
        FaithTrack.deleteState();
        reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        ft = FaithTrack.instance(reportNumOrder);
        playerBoard.setPlayerFaithLevelFaithTrack(ft);
    }

    @Test
    //Tests whether the PopeTiles are set correctly in the player's FaithLevel
    public void ctrlPopeTileSetting(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);
        FaithLevel fT = playerBoard.getFaithLevel();
        List<PopeTile> faithLevelTiles = fT.getPopeTilesSafe();

        assertEquals(popeTileList.size(), faithLevelTiles.size());
        //The order of the tiles must be the same
        for(int i = 0; i < popeTileList.size(); i++){
            assertEquals(popeTileList.get(i), faithLevelTiles.get(i));
            assertNotSame(popeTileList.get(i), faithLevelTiles.get(i));
        }
    }

    @Test
    //Tests that we can only set the PopeTiles once
    public void ctrlAnotherPopeTileSetting(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);

        List<PopeTile> fake = new ArrayList<>();
        fake.add(new PopeTile(55, ReportNum.REPORT1));
        fake.add(new PopeTile(66, ReportNum.REPORT2));
        fake.add(new PopeTile(77, ReportNum.REPORT3));

        playerBoard.setPlayerFaithLevelPopeTiles(fake);

        FaithLevel fL = playerBoard.getFaithLevel();

        assertNotSame(fL.getPopeTiles(), popeTileList); //The list of PopeTiles stored in the FaithLevel are cloned from the one given
        assertEquals(fL.getPopeTiles(), popeTileList);
        assertNotSame(fL.getPopeTilesSafe(), popeTileList);
        assertEquals(fL.getPopeTilesSafe(), popeTileList);
        assertNotEquals(fL.getPopeTilesSafe(), fake);
    }

    @Test
    //Tests whether the FaithLevel Marker is moved properly: one simple move
    public void ctrlMarkerPositionOneMove(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);
        FaithLevel tmp = playerBoard.getFaithLevel();

        assertEquals(playerBoard.getPositionOnFaithTrack(), tmp.getPosition());
        assertEquals(tmp.getPosition(), 0);

        try {
            assertFalse(playerBoard.moveForwardOnFaithTrack(5));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        FaithLevel tmp2 = playerBoard.getFaithLevel();

        assertNotSame(tmp2, tmp);
        assertNotEquals(tmp2, tmp);
        assertEquals(tmp2.getPosition(), playerBoard.getPositionOnFaithTrack());
        assertEquals(playerBoard.getPositionOnFaithTrack(), 5);
    }

    @Test
    //Tests whether the FaithLevel Marker is moved properly: all Vatican Reports are activated
    public void ctrlMarkerPositionManyMoves(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);

        PopeCell pC = (PopeCell) ft.getCell(8);
        ControllerStub cS = new ControllerStub(playerBoard);
        pC.detach(pC.getObserversList().get(0));
        pC.attach(cS);
        pC = (PopeCell) ft.getCell(16);
        pC.detach(pC.getObserversList().get(0));
        pC.attach(cS);
        pC = (PopeCell) ft.getCell(24);
        pC.detach(pC.getObserversList().get(0));
        pC.attach(cS);

        FaithLevel fL = playerBoard.getFaithLevel();
        List<PopeTile> pT = fL.getPopeTilesSafe();
        assertFalse(pT.get(0).isChanged());
        assertFalse(pT.get(1).isChanged());
        assertFalse(pT.get(2).isChanged());

        try {
            assertFalse(playerBoard.moveForwardOnFaithTrack(10));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        fL = playerBoard.getFaithLevel();
        pT = fL.getPopeTilesSafe();

        assertTrue(pT.get(0).isActivated());
        assertFalse(pT.get(1).isChanged());
        assertFalse(pT.get(2).isChanged());

        assertEquals(playerBoard.getPositionOnFaithTrack(), 10);

        try {
            assertFalse(playerBoard.moveForwardOnFaithTrack(10));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        fL = playerBoard.getFaithLevel();
        pT = fL.getPopeTilesSafe();
        assertTrue(pT.get(0).isActivated());
        assertTrue(pT.get(1).isActivated());
        assertFalse(pT.get(2).isChanged());
        assertEquals(playerBoard.getPositionOnFaithTrack(), 20);

        LastVaticanReportException exception = assertThrows(LastVaticanReportException.class, () -> playerBoard.moveForwardOnFaithTrack(+ft.getTrackSize() + 10));
        assertEquals(playerBoard.getPositionOnFaithTrack(), +ft.getTrackSize() - 1);
        assertEquals(playerBoard.getPositionOnFaithTrack(), 24);

        fL = playerBoard.getFaithLevel();
        pT = fL.getPopeTilesSafe();
        assertTrue(pT.get(0).isActivated());
        assertTrue(pT.get(1).isActivated());
        assertTrue(pT.get(2).isActivated());
    }

    @Test
    //Tests that a player gets the correct points due to their position on the FaithTrack
    public void ctrlCellPoints(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);

        try {
            assertFalse(playerBoard.moveForwardOnFaithTrack(6));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertEquals(playerBoard.getCellPoints(), 2);
    }

    @Test
    //Tests that a player gets the correct points from their PopeTiles
    public void ctrlPopeTilePoints(){
        playerBoard.setPlayerFaithLevelPopeTiles(popeTileList);
        PopeCell pC = (PopeCell) ft.getCell(8);
        ControllerStub cS = new ControllerStub(playerBoard);
        pC.detach(pC.getObserversList().get(0));
        pC.attach(cS);
        pC = (PopeCell) ft.getCell(16);
        pC.detach(pC.getObserversList().get(0));
        pC.attach(cS);

        try {
            playerBoard.moveForwardOnFaithTrack(17);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertEquals(playerBoard.getPopeTilesPoints(), 11); //The first two tiles are active, the last one is not changed
    }


}
