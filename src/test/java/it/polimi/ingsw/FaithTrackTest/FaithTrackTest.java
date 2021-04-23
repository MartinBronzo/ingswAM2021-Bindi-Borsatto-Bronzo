package it.polimi.ingsw.FaithTrackTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class FaithTrackTest {

    @Test
    //Tests that only one instance of the FaithTrack ever exists
    public void onlyOneInstance(){
        //This first line is needed to make sure that when the whole class of tests is running this method is able to create the only instance of the FaithTrack class
        //(this is achieved by deleting whatever instance was created before). The static method should NOT be used outside the testing phase.
        FaithTrack.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack faithTrack1 = FaithTrack.instance(reportNumOrder);
        //assertTrue(faithTrack1.initTrack());

        FaithTrack faithTrack2 = FaithTrack.instance(reportNumOrder);
        assertSame(faithTrack1, faithTrack2);
        //assertFalse(faithTrack2.initTrack());

        FaithTrack faithTrack3 = FaithTrack.instance(reportNumOrder);
        assertSame(faithTrack1, faithTrack3);
        //assertFalse(faithTrack3.initTrack());
    }

    @Test
    //Tests creation of Faith Track
    public void ctrlTrackCreation(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);

        FaithTrack faithTrack = FaithTrack.instance(reportNumOrder);
        //faithTrack.initTrack();

        //Control the saved ReportNumOrder
        ReportNumOrder tmp = faithTrack.getReportNumOrder();
        assertEquals(reportNumOrder.getOrder(ReportNum.REPORT1), tmp.getOrder(ReportNum.REPORT1));
        assertEquals(reportNumOrder.getOrder(ReportNum.REPORT2), tmp.getOrder(ReportNum.REPORT2));
        assertEquals(reportNumOrder.getOrder(ReportNum.REPORT3), tmp.getOrder(ReportNum.REPORT3));
        assertEquals(tmp.getOrder(ReportNum.REPORT1), 0);
        assertEquals(tmp.getOrder(ReportNum.REPORT2), 1);
        assertEquals(tmp.getOrder(ReportNum.REPORT3), 2);

        //REPORT1
        //Cells 0-4: Normal Cells
        for(int i = 0; i < 5; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 3) ? 0 : 1);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT1);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT1));
        }

        //Cells 5-7: Report Cells
        for(int i = 5; i < 8; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 6) ? 0 : 2);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT1);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT1));
        }

        //Cell 8: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(8));
        assertEquals(faithTrack.getCellVictoryPoints(8), 0);
        assertEquals(faithTrack.getCellReportNum(8), ReportNum.REPORT1);
        assertTrue(faithTrack.callCellActivateTile(8, ReportNum.REPORT1));

        //REPORT2
        //Cells 9-11: Normal Cells
        for(int i = 9; i < 12; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 9) ? 0 : 4);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT2);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT2));
        }

        //Cells 12-15: Report Cells
        for(int i = 12; i < 16; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 12 && i != 15) ? 0 : ((i == 12) ? 6 : 9));
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT2);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT2));
        }

        //Cell 16: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(16));
        assertEquals(faithTrack.getCellVictoryPoints(16), 0);
        assertEquals(faithTrack.getCellReportNum(16), ReportNum.REPORT2);
        assertTrue(faithTrack.callCellActivateTile(16, ReportNum.REPORT2));

        //REPORT3
        //Cells 17-18: Normal Cells
        for(int i = 17; i < 19; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 18) ? 0 : 12);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT3);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT3));
        }

        //Cells 19-23: Report Cells
        for(int i = 19; i < 24; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 21) ? 0 : 16);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT3);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT3));
        }

        //Cell 24: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(24));
        assertEquals(faithTrack.getCellVictoryPoints(24), 20);
        assertEquals(faithTrack.getCellReportNum(24), ReportNum.REPORT3);
        assertTrue(faithTrack.callCellActivateTile(24, ReportNum.REPORT3));

        assertEquals(faithTrack.getTrackSize(), 25);

    }

    @Test
    //Tests creation of Faith Track with the configuration file
    //Right now, this method tests that the right methods are called thanks to overloading. In the near future, I'll
    //implement the actual configuration via file
    public void ctrlTrackCreationWithConfigFile() throws IOException, SAXException, ParserConfigurationException {
        FaithTrack.deleteState();
        File config = new File("FaithTrackConfig.xml");
        FaithTrack faithTrack = FaithTrack.instance(config);

        //REPORT1
        //Cells 0-4: Normal Cells
        for(int i = 0; i < 5; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 3) ? 0 : 1);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT1);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT1));
        }

        //Cells 5-7: Report Cells
        for(int i = 5; i < 8; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 6) ? 0 : 2);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT1);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT1));
        }

        //Cell 8: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(8));
        assertEquals(faithTrack.getCellVictoryPoints(8), 0);
        assertEquals(faithTrack.getCellReportNum(8), ReportNum.REPORT1);
        assertTrue(faithTrack.callCellActivateTile(8, ReportNum.REPORT1));

        //REPORT2
        //Cells 9-11: Normal Cells
        for(int i = 9; i < 12; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 9) ? 0 : 4);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT2);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT2));
        }

        //Cells 12-15: Report Cells
        for(int i = 12; i < 16; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 12 && i != 15) ? 0 : ((i == 12) ? 6 : 9));
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT2);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT2));
        }

        //Cell 16: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(16));
        assertEquals(faithTrack.getCellVictoryPoints(16), 0);
        assertEquals(faithTrack.getCellReportNum(16), ReportNum.REPORT2);
        assertTrue(faithTrack.callCellActivateTile(16, ReportNum.REPORT2));

        //REPORT3
        //Cells 17-18: Normal Cells
        for(int i = 17; i < 19; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 18) ? 0 : 12);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT3);
            assertFalse(faithTrack.callCellActivateTile(i, ReportNum.REPORT3));
        }

        //Cells 19-23: Report Cells
        for(int i = 19; i < 24; i++) {
            assertFalse(faithTrack.callCellEffectBasic(i));
            assertEquals(faithTrack.getCellVictoryPoints(i), (i != 21) ? 0 : 16);
            assertEquals(faithTrack.getCellReportNum(i), ReportNum.REPORT3);
            assertTrue(faithTrack.callCellActivateTile(i, ReportNum.REPORT3));
        }

        //Cell 24: PopeCell
        assertTrue(faithTrack.callCellEffectBasic(24));
        assertEquals(faithTrack.getCellVictoryPoints(24), 20);
        assertEquals(faithTrack.getCellReportNum(24), ReportNum.REPORT3);
        assertTrue(faithTrack.callCellActivateTile(24, ReportNum.REPORT3));

        assertEquals(faithTrack.getTrackSize(), 25);
    }

    @Test
    //Tests that there can only be once instance of the FaithTrack class, even when we create the FaithTrack with the configuration file
    public void onlyOneInstanceWithConfigFile() throws IOException, SAXException, ParserConfigurationException {
        FaithTrack.deleteState();
        File config = new File("FaithTrackConfig.xml");
        FaithTrack faithTrack = FaithTrack.instance(config);

        FaithTrack faithTrack2 = FaithTrack.instance(config);
        assertSame(faithTrack, faithTrack2);

        FaithTrack faithTrack3 = FaithTrack.instance(config);
        assertSame(faithTrack, faithTrack3);
    }

    @Test
    //Tests that the ReportNumOrder is correctly set via configuration file
    public void ctrlReportNumOrderViaFile() throws IOException, SAXException, ParserConfigurationException {
        FaithTrack.deleteState();
        File config = new File("FaithTrackConfig.xml");
        FaithTrack faithTrack = FaithTrack.instance(config);
        ReportNumOrder reportNumOrder = faithTrack.getReportNumOrder();

        assertEquals(reportNumOrder.getReportNum(0), ReportNum.REPORT1);
        assertEquals(reportNumOrder.getReportNum(1), ReportNum.REPORT2);
        assertEquals(reportNumOrder.getReportNum(2), ReportNum.REPORT3);
        assertEquals(reportNumOrder.getSize(), 3);


    }


    @Test
    //Tests that, once set, the ReportNumOrder can't be changed: FaithTrack created without the configuration file
    public void ctrlReportNumOrderNotSetting(){
        FaithTrack.deleteState();
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        FaithTrack faithTrack = FaithTrack.instance(reportNumOrder);

        assertTrue(faithTrack.isReportNumOrderSet());

        ReportNumOrder copy = faithTrack.getReportNumOrder();
        assertSame(copy, reportNumOrder);

        assertFalse(faithTrack.setReportNumOrder(ReportNumOrder.instance()));

        assertTrue(faithTrack.isReportNumOrderSet());

        copy = faithTrack.getReportNumOrder();
        assertSame(copy, reportNumOrder);
    }

    @Test
    //Tests that, once set, the ReportNumOrder can't be changed: FaithTrack created with the configuration file
    public void ctrlReportNumOrderNotSettingWithConfigFile() throws IOException, SAXException, ParserConfigurationException {
        FaithTrack.deleteState();
        File config = new File("FaithTrackConfig.xml");
        FaithTrack faithTrack = FaithTrack.instance(config);

        assertTrue(faithTrack.isReportNumOrderSet());

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        assertFalse(faithTrack.setReportNumOrder(reportNumOrder));
        assertTrue(faithTrack.isReportNumOrderSet());


        assertFalse(faithTrack.setReportNumOrder(reportNumOrder));
        assertTrue(faithTrack.isReportNumOrderSet());

    }

    @Test
    //Tests the forwarding of request to Cells with different position given as inputs
   public void ctrlPositionCell(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        FaithTrack faithTrack = FaithTrack.instance(reportNumOrder);
        //faithTrack.initTrack();

        //Normal Cell
        try {
            assertFalse(faithTrack.callCellEffect(4));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        assertFalse(faithTrack.callCellActivateTile(4, ReportNum.REPORT1));
        assertFalse(faithTrack.callCellActivateTile(9, ReportNum.REPORT1));
        //ReportCell
        try {
            assertFalse(faithTrack.callCellEffect(5));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        assertTrue(faithTrack.callCellActivateTile(5, ReportNum.REPORT1));
        assertFalse(faithTrack.callCellActivateTile(5, ReportNum.REPORT2));
        //PopeCell but not the last one
        try {
            assertTrue(faithTrack.callCellEffect(8));
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }
        assertTrue(faithTrack.callCellActivateTile(8, ReportNum.REPORT1));
        assertFalse(faithTrack.callCellActivateTile(8, ReportNum.REPORT2));
        //The last PopeCell
        assertThrows(LastVaticanReportException.class, () -> faithTrack.callCellEffect(24));

    }

}
