package it.polimi.ingsw.FaithTrackTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Observer;
import org.junit.jupiter.api.Test;

public class CellTest {

    @Test
    //Tests whether the creation of the single Cell of all types is correct
    public void ctrlCellCreation(){
        Cell cell1 = new Cell(1, ReportNum.REPORT1);
        Cell cell2 = new Cell(2, ReportNum.REPORT2);
        Cell cell3 = new Cell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVicoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertFalse(cell1.effect());
        assertEquals(cell2.getVicoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertFalse(cell2.effect());
        assertEquals(cell3.getVicoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(cell3.effect());

        cell1 = new ReportCell(1, ReportNum.REPORT1);
        cell2 = new ReportCell(2, ReportNum.REPORT2);
        cell3 = new ReportCell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVicoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertFalse(cell1.effect());
        assertEquals(cell2.getVicoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertFalse(cell2.effect());
        assertEquals(cell3.getVicoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(cell3.effect());

        cell1 = new PopeCell(1, ReportNum.REPORT1);
        cell2 = new PopeCell(2, ReportNum.REPORT2);
        cell3 = new PopeCell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVicoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertEquals(cell2.getVicoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertEquals(cell3.getVicoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(((PopeCell) cell1).isActivated());
        assertFalse(((PopeCell) cell2).isActivated());
        assertFalse(((PopeCell) cell3).isActivated());
        assertTrue(((PopeCell) cell1).getObserversList().isEmpty());
        assertTrue(((PopeCell) cell2).getObserversList().isEmpty());
        assertTrue(((PopeCell) cell3).getObserversList().isEmpty());
    }

    @Test
    //Tests whether the Cells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationCell(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);

        Cell cell1 = new Cell(0, ReportNum.REPORT1);
        Cell cell2 = new Cell(0, ReportNum.REPORT2);
        Cell cell3 = new Cell(0, ReportNum.REPORT3);

        assertFalse(cell1.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell2.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell3.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell3.activatePopeTile(ReportNum.REPORT3, reportNumOrder));
    }

    @Test
    //Tests whether the ReportCells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationReportCell(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);

        Cell cell1 = new ReportCell(0, ReportNum.REPORT1);
        Cell cell2 = new ReportCell(0, ReportNum.REPORT2);
        Cell cell3 = new ReportCell(0, ReportNum.REPORT3);

        assertTrue(cell1.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell2.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertTrue(cell2.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell3.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT3, reportNumOrder));
    }

    @Test
    //Tests whether the PopeCells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationPopeCell(){
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);

        Cell cell1 = new PopeCell(0, ReportNum.REPORT1);
        Cell cell2 = new PopeCell(0, ReportNum.REPORT2);
        Cell cell3 = new PopeCell(0, ReportNum.REPORT3);

        assertTrue(cell1.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell2.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertTrue(cell2.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3, reportNumOrder));

        assertTrue(cell3.activatePopeTile(ReportNum.REPORT1, reportNumOrder));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT2, reportNumOrder));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT3, reportNumOrder));
    }


    @Test
    //Tests that the pope Cell activates correctly a observer by testing the connection with that observer
    public void ctrlSingleObserverNotification(){
        PopeCell popeCell1 = new PopeCell(0, ReportNum.REPORT1);
        Observer observer = new ControllerStub(popeCell1);
        Observer fakeObserver = new ControllerStub(popeCell1);

        assertTrue(popeCell1.attach(observer));
        assertEquals(popeCell1.fakeEffect(observer), "Activate Vatican Report: REPORT1");
        assertEquals(popeCell1.fakeEffect(fakeObserver),"Error!");
        assertTrue(popeCell1.isActivated());
        assertTrue(popeCell1.detach(observer));

        PopeCell popeCell2 = new PopeCell(0, ReportNum.REPORT2);
        ((ControllerStub)observer).setSubject(popeCell2);
        assertTrue(popeCell2.attach(observer));
        assertEquals(popeCell2.fakeEffect(observer), "Activate Vatican Report: REPORT2");
        assertEquals(popeCell2.fakeEffect(fakeObserver),"Error!");
        assertTrue(popeCell2.isActivated());
        assertTrue(popeCell2.detach(observer));

        PopeCell popeCell3 = new PopeCell(0, ReportNum.REPORT3);
        ((ControllerStub)observer).setSubject(popeCell3);
        assertTrue(popeCell3.attach(observer));
        assertEquals(popeCell3.fakeEffect(observer), "Activate Vatican Report: REPORT3");
        assertEquals(popeCell3.fakeEffect(fakeObserver),"Error!");
        assertTrue(popeCell3.isActivated());
        assertTrue(popeCell3.detach(observer));
    }

    @Test
    //Tests that the Pope Cell notifies all the observers correctly
    public void ctrlObserversNotification(){
        PopeCell popeCell = new PopeCell(0, ReportNum.REPORT1);
        Observer observer1 = new ControllerStub(popeCell);
        Observer observer2 = new ControllerStub(popeCell);
        Observer observer3 = new ControllerStub(popeCell);
        Observer observer4 = new ControllerStub(popeCell);

        assertTrue(popeCell.attach(observer1));
        assertTrue(popeCell.attach(observer2));
        assertTrue(popeCell.attach(observer3));
        assertTrue(popeCell.attach(observer4));
        assertTrue(popeCell.effect());
        assertTrue(popeCell.isActivated());
        assertTrue(popeCell.detach(observer1));
        assertTrue(popeCell.detach(observer2));
        assertTrue(popeCell.detach(observer3));
        assertTrue(popeCell.detach(observer4));
    }
}
