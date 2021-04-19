package it.polimi.ingsw.FaithTrackTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Interfaces.Observer;
import org.junit.jupiter.api.Test;

public class CellTest {

    @Test
    //Tests whether the creation of the single Cell of all types is correct
    public void ctrlCellCreationPope(){
        PopeCell cell1 = new PopeCell(1, ReportNum.REPORT1);
        PopeCell cell2 = new PopeCell(2, ReportNum.REPORT2);
        PopeCell cell3 = new PopeCell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVictoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertEquals(cell2.getVictoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertEquals(cell3.getVictoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(cell1.isActivated());
        assertFalse(cell2.isActivated());
        assertFalse(cell3.isActivated());
        assertTrue(cell1.getObserversList().isEmpty());
        assertTrue(cell2.getObserversList().isEmpty());
        assertTrue(cell3.getObserversList().isEmpty());
    }

    @Test
    public void ctrlCellCreationNormal(){
        Cell cell1 = new Cell(1, ReportNum.REPORT1);
        Cell cell2 = new Cell(2, ReportNum.REPORT2);
        Cell cell3 = new Cell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVictoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertFalse(cell1.effect());
        assertEquals(cell2.getVictoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertFalse(cell2.effect());
        assertEquals(cell3.getVictoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(cell3.effect());
    }

    @Test
    public void ctrlCellCreationReport(){
        Cell cell1 = new ReportCell(1, ReportNum.REPORT1);
        Cell cell2 = new ReportCell(2, ReportNum.REPORT2);
        Cell cell3 = new ReportCell(3, ReportNum.REPORT3);

        assertEquals(cell1.getVictoryPoints(), 1);
        assertEquals(cell1.getReportNum(), ReportNum.REPORT1);
        assertFalse(cell1.effect());
        assertEquals(cell2.getVictoryPoints(), 2);
        assertEquals(cell2.getReportNum(), ReportNum.REPORT2);
        assertFalse(cell2.effect());
        assertEquals(cell3.getVictoryPoints(), 3);
        assertEquals(cell3.getReportNum(), ReportNum.REPORT3);
        assertFalse(cell3.effect());
    }

    @Test
    //Tests whether the Cells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationCell(){

        Cell cell1 = new Cell(0, ReportNum.REPORT1);
        Cell cell2 = new Cell(0, ReportNum.REPORT2);
        Cell cell3 = new Cell(0, ReportNum.REPORT3);

        assertFalse(cell1.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell2.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell3.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell3.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell3.activatePopeTile(ReportNum.REPORT3));
    }

    @Test
    //Tests whether the ReportCells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationReportCell(){

        Cell cell1 = new ReportCell(0, ReportNum.REPORT1);
        Cell cell2 = new ReportCell(0, ReportNum.REPORT2);
        Cell cell3 = new ReportCell(0, ReportNum.REPORT3);

        assertTrue(cell1.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell2.activatePopeTile(ReportNum.REPORT1));
        assertTrue(cell2.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell3.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell3.activatePopeTile(ReportNum.REPORT2));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT3));
    }

    @Test
    //Tests whether the PopeCells communicate to turn and not to turn the PopeTile as needed
    public void ctrlActivationPopeCell(){

        Cell cell1 = new PopeCell(0, ReportNum.REPORT1);
        Cell cell2 = new PopeCell(0, ReportNum.REPORT2);
        Cell cell3 = new PopeCell(0, ReportNum.REPORT3);

        assertTrue(cell1.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell1.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell2.activatePopeTile(ReportNum.REPORT1));
        assertTrue(cell2.activatePopeTile(ReportNum.REPORT2));
        assertFalse(cell2.activatePopeTile(ReportNum.REPORT3));

        assertFalse(cell3.activatePopeTile(ReportNum.REPORT1));
        assertFalse(cell3.activatePopeTile(ReportNum.REPORT2));
        assertTrue(cell3.activatePopeTile(ReportNum.REPORT3));
    }


    @Test
    //Tests that the pope Cell activates correctly a observer by testing the connection with that observer
    public void ctrlSingleObserverNotification(){
        PopeCell popeCell1 = new PopeCell(0, ReportNum.REPORT1);
        ControllerStub observer = new ControllerStub(popeCell1);
        Observer fakeObserver = new ControllerStub(popeCell1);

        assertTrue(popeCell1.attach(observer));
        assertEquals(popeCell1.fakeEffect(observer), "Activate Vatican Report: REPORT1");
        assertEquals(popeCell1.fakeEffect(fakeObserver),"Error!");
        assertTrue(popeCell1.isActivated());
        assertTrue(popeCell1.detach(observer));

        PopeCell popeCell2 = new PopeCell(0, ReportNum.REPORT2);
        observer.setSubject(popeCell2);
        assertTrue(popeCell2.attach(observer));
        assertEquals(popeCell2.fakeEffect(observer), "Activate Vatican Report: REPORT2");
        assertEquals(popeCell2.fakeEffect(fakeObserver),"Error!");
        assertTrue(popeCell2.isActivated());
        assertTrue(popeCell2.detach(observer));

        PopeCell popeCell3 = new PopeCell(0, ReportNum.REPORT3);
        observer.setSubject(popeCell3);
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

    //Cell
    @Test
    public void ctrlCellEqualsTrue(){
        Cell c1 = new Cell(3, ReportNum.REPORT1);
        Cell c2 = new Cell(3, ReportNum.REPORT1);
        assertEquals(c1, c2);
        assertEquals(c1, c1);
        assertEquals(c1.getReportNum(), c2.getReportNum());
        assertEquals(c1.getVictoryPoints(), c2.getVictoryPoints());
    }

    @Test
    public void ctrlCellEqualsFalse(){
        Cell c1 = new Cell(2, ReportNum.REPORT1);
        Cell c2 = new Cell(3, ReportNum.REPORT1);
        Cell c3 = new Cell(2, ReportNum.REPORT3);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, new ReportCell(2, ReportNum.REPORT1));
        assertNotEquals(c1, new PopeCell(2, ReportNum.REPORT1));
    }

    @Test
    public void ctrlCellCloning(){
        Cell c1 = new Cell(3, ReportNum.REPORT1);
        Cell c1bis = new Cell(c1);

        assertNotSame(c1bis, c1);
        assertEquals(c1bis, c1);
    }

    //ReportCell
    @Test
    public void ctrlReportEqualsTrue(){
        ReportCell c1 = new ReportCell(3, ReportNum.REPORT1);
        ReportCell c2 = new ReportCell(3, ReportNum.REPORT1);
        assertEquals(c1, c2);
        assertEquals(c1, c1);
        assertEquals(c1.getReportNum(), c2.getReportNum());
        assertEquals(c1.getVictoryPoints(), c2.getVictoryPoints());
    }

    @Test
    public void ctrlReportEqualsFalse(){
        ReportCell c1 = new ReportCell(2, ReportNum.REPORT1);
        ReportCell c2 = new ReportCell(3, ReportNum.REPORT1);
        ReportCell c3 = new ReportCell(2, ReportNum.REPORT3);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, new PopeCell(2, ReportNum.REPORT1));
    }

    @Test
    public void ctrlReportCloning(){
        ReportCell c1 = new ReportCell(3, ReportNum.REPORT1);
        ReportCell c1bis = new ReportCell(c1);

        assertNotSame(c1bis, c1);
        assertEquals(c1bis, c1);
    }

    //PopeCell
    @Test
    public void ctrlPopeEqualsTrue(){
        PopeCell c1 = new PopeCell(3, ReportNum.REPORT1);
        PopeCell c2 = new PopeCell(3, ReportNum.REPORT1);
        Observer o1 = new ControllerStub();
        Observer o2 = new ControllerStub();
        c1.attach(o1);
        c1.attach(o2);
        c2.attach(o1);
        c2.attach(o2);

        assertEquals(c1, c2);
        assertEquals(c1, c1);
        assertEquals(c1.getReportNum(), c2.getReportNum());
        assertEquals(c1.getVictoryPoints(), c2.getVictoryPoints());
        assertEquals(c1.isActivated(), c2.isActivated());
        assertTrue(c1.getObserversList().containsAll(c2.getObserversList()));
        assertTrue(c2.getObserversList().containsAll(c1.getObserversList()));
    }

    @Test
    public void ctrlPopeEqualsFalse(){
        PopeCell c1 = new PopeCell(2, ReportNum.REPORT1);
        PopeCell c2 = new PopeCell(3, ReportNum.REPORT1);
        PopeCell c3 = new PopeCell(2, ReportNum.REPORT3);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, new ReportCell(2, ReportNum.REPORT1));

        PopeCell c4 = new PopeCell(2, ReportNum.REPORT1);
        Observer o1 = new ControllerStub();
        Observer o2 = new ControllerStub();
        c1.attach(o1);
        c1.attach(o2);
        c4.attach(o1);

        assertNotEquals(c1, c4);
    }

    @Test
    public void ctrlPopeCloning(){
        PopeCell c1 = new PopeCell(3, ReportNum.REPORT1);
        Observer o1 = new ControllerStub();
        Observer o2 = new ControllerStub();
        c1.attach(o1);
        c1.attach(o2);
        PopeCell c1bis = new PopeCell(c1);

        assertNotSame(c1bis, c1);
        assertEquals(c1bis, c1);
    }
}
