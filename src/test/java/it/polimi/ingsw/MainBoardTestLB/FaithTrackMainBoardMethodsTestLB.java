package it.polimi.ingsw.MainBoardTestLB;

import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.PopeTile;
import it.polimi.ingsw.FaithTrack.PopeCellObserver;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.MainBoard;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
//import sun.tools.jar.Main;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackMainBoardMethodsTestLB {
    static FaithTrack faithTrack;
    static PlayerBoard playerBoard1;
    static PlayerBoard playerBoard2;
    static MainBoard mainBoard;
    static Observer observer;
    static List<PopeTile> popeTileList;

    @BeforeAll
    public static void setUp() throws IOException, SAXException, ParserConfigurationException {
        faithTrack = FaithTrack.instance(new File("FaithTrackConfig.xml"));
        playerBoard1 = new PlayerBoard();
        playerBoard2 = new PlayerBoard();
        playerBoard1.setPlayerFaithLevelFaithTrack(faithTrack);
        playerBoard2.setPlayerFaithLevelFaithTrack(faithTrack);
        mainBoard = new MainBoard();
        mainBoard.addPlayerBoard(playerBoard1);
        mainBoard.addPlayerBoard(playerBoard2);
        observer = new PopeCellObserver(mainBoard);
        faithTrack.attachObserverToPopeTiles(observer);
        popeTileList = new ArrayList<>();
        popeTileList.add(new PopeTile(1, ReportNum.REPORT1));
        popeTileList.add(new PopeTile(2, ReportNum.REPORT2));
        popeTileList.add(new PopeTile(3, ReportNum.REPORT3));
        playerBoard1.setPlayerFaithLevelPopeTiles(popeTileList);
        playerBoard2.setPlayerFaithLevelPopeTiles(popeTileList);
    }

    @Test
    //Tests the Observer PopeTileObserver calls the MainBoard as it is supposed
    public void ctrlObserverJob() throws LastVaticanReportException {
        assertEquals(playerBoard1.getPositionOnFaithTrack(), 0);
        assertEquals(playerBoard2.getPositionOnFaithTrack(), 0);

        playerBoard1.moveForwardOnFaithTrack(10);

        assertTrue(playerBoard1.getPopeTile().get(0).isActivated());
        assertTrue(playerBoard2.getPopeTile().get(0).isDiscarded());

        assertThrows(LastVaticanReportException.class, () -> playerBoard2.moveForwardOnFaithTrack(+faithTrack.getTrackSize() + 10));
        assertTrue(playerBoard1.getPopeTile().get(1).isDiscarded());
        assertTrue(playerBoard1.getPopeTile().get(2).isDiscarded());
        assertTrue(playerBoard2.getPopeTile().get(1).isActivated());
        assertTrue(playerBoard2.getPopeTile().get(2).isActivated());
    }

}
