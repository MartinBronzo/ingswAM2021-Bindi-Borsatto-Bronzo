package it.polimi.ingsw.model.FaithTrackTest;

import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.FaithTrack.ReportNumOrder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ReportNumOrderTest {
    @Test
    //Tests the correct creation and addition of elements, controlling there is only one instance of the class
    public void ctrlCreationElements() {
        //This first line is needed to make sure that when the whole class of tests is running this method is able to create the only instance of the ReportNumOrder class
        //(this is achieved by deleting whatever instance was created before). The static method should NOT be used outside the testing phase.
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder1 = ReportNumOrder.instance();

        /*//Tests that only instance ever exists
        ReportNumOrder reportNumOrder2 = ReportNumOrder.instance();
        assertSame(reportNumOrder1, reportNumOrder2);
        ReportNumOrder reportNumOrder3 = ReportNumOrder.instance();
        assertSame(reportNumOrder1, reportNumOrder3);*/

        //Tests that elements can only be added once
        assertThrows(NullPointerException.class, () -> reportNumOrder1.addElementInOrder(null));
        assertTrue(reportNumOrder1.addElementInOrder(ReportNum.REPORT1));
        assertTrue(reportNumOrder1.addElementInOrder(ReportNum.REPORT2));
        assertTrue(reportNumOrder1.addElementInOrder(ReportNum.REPORT3));
        assertFalse(reportNumOrder1.addElementInOrder(ReportNum.REPORT1));
        assertFalse(reportNumOrder1.addElementInOrder(ReportNum.REPORT2));
        assertFalse(reportNumOrder1.addElementInOrder(ReportNum.REPORT3));
    }

    @Test
    //Checks that multiple ReportNumOrder can be made
    public void ctrlMultipleInstances(){
        ReportNumOrder reportNumOrder1 = ReportNumOrder.instance();
        ReportNumOrder reportNumOrder2 = ReportNumOrder.instance();

        assertNotSame(reportNumOrder1, reportNumOrder2);
    }

    @Test
    //Tests whether the elements are added in the order as request
    public void ctrlCreationOrder() {
        //This first line is needed to make sure that when the whole class of tests is running this method is able to create the only instance of the ReportNumOrder class
        //(this is achieved by deleting whatever instance was created before). The static method should NOT be used outside the testing phase.
        ReportNumOrder.deleteState();

        List<ReportNum> reportNumList = new ArrayList<>();

        reportNumList.add(ReportNum.REPORT1);
        reportNumList.add(ReportNum.REPORT3);
        reportNumList.add(ReportNum.REPORT2);
        assertEquals(reportNumList.indexOf(ReportNum.REPORT1), 0);
        assertEquals(reportNumList.indexOf(ReportNum.REPORT3), 1);
        assertEquals(reportNumList.indexOf(ReportNum.REPORT2), 2);

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        assertTrue(reportNumOrder.isEmpty());
        for (ReportNum rp : reportNumList)
            reportNumOrder.addElementInOrder(rp);

        //Tests whether the original list and the elements stored in the ReportNumOrder are in the same order
        for (ReportNum rp : reportNumList)
            assertEquals(reportNumList.indexOf(rp), reportNumOrder.getOrder(rp));
    }

    @Test
    //Tests whether it can state the correct order of two ReportNums which do belong to list
    public void ctrlOrderStatingNoException() {
        //This first line is needed to make sure that when the whole class of tests is running this method is able to create the only instance of the ReportNumOrder class
        //(this is achieved by deleting whatever instance was created before). The static method should NOT be used outside the testing phase.
        ReportNumOrder.deleteState();

        ReportNumOrder reportNumOrder = ReportNumOrder.instance();
        List<ReportNum> rNList = new ArrayList<>();

        rNList.add(ReportNum.REPORT1);
        rNList.add(ReportNum.REPORT3);
        rNList.add(ReportNum.REPORT2);
        for (ReportNum rp : rNList)
            reportNumOrder.addElementInOrder(rp);
        //Order: REPORT1, REPORT3, REPORT2

        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT1, ReportNum.REPORT1));
        assertTrue(reportNumOrder.stateOrder(ReportNum.REPORT1, ReportNum.REPORT3));
        assertTrue(reportNumOrder.stateOrder(ReportNum.REPORT1, ReportNum.REPORT2));

        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT3, ReportNum.REPORT1));
        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT3, ReportNum.REPORT3));
        assertTrue(reportNumOrder.stateOrder(ReportNum.REPORT3, ReportNum.REPORT2));

        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT2, ReportNum.REPORT1));
        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT2, ReportNum.REPORT3));
        assertFalse(reportNumOrder.stateOrder(ReportNum.REPORT2, ReportNum.REPORT2));
    }

    @Test
    //Tests whether it throws exception when it is being used to state the order of elements which don't belong to set
    public void ctrlOrderStatingWithException() {
        //This first line is needed to make sure that when the whole class of tests is running this method is able to create the only instance of the ReportNumOrder class
        //(this is achieved by deleting whatever instance was created before). The static method should NOT be used outside the testing phase.
        ReportNumOrder.deleteState();
        ReportNumOrder reportNumOrder = ReportNumOrder.instance();

        //Both elements don't belong
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reportNumOrder.stateOrder(ReportNum.REPORT1, ReportNum.REPORT2));
        assertEquals(exception.getMessage(), "The first element given as a parameter is not in the list");
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        exception = assertThrows(IllegalArgumentException.class, () -> reportNumOrder.stateOrder(ReportNum.REPORT2, ReportNum.REPORT3));
        assertEquals(exception.getMessage(), "The first element given as a parameter is not in the list");

        //The first element doesn't belong
        exception = assertThrows(IllegalArgumentException.class, () -> reportNumOrder.stateOrder(ReportNum.REPORT2, ReportNum.REPORT1));
        assertEquals(exception.getMessage(), "The first element given as a parameter is not in the list");

        //The second element doesn't belong
        exception = assertThrows(IllegalArgumentException.class, () -> reportNumOrder.stateOrder(ReportNum.REPORT1, ReportNum.REPORT3));
        assertEquals(exception.getMessage(), "The second element given as a parameter is not in the list");
    }

    @Test
    //Tests whether the cloning of ReportNumOrders works correctly
    public void ctrlCloning(){
        ReportNumOrder original = ReportNumOrder.instance();
        original.addElementInOrder(ReportNum.REPORT1);
        original.addElementInOrder(ReportNum.REPORT2);
        original.addElementInOrder(ReportNum.REPORT3);

        ReportNumOrder clone = new ReportNumOrder(original);

        for(int i = 0; i < 3; i++)
            assertEquals(clone.getReportNum(i), original.getReportNum(i));

        assertEquals(clone, original);
    }

    @Test
    //Tests that the equals methods works correctly: true
    public void ctrlEqualsTrue(){
        ReportNumOrder original = ReportNumOrder.instance();
        original.addElementInOrder(ReportNum.REPORT1);
        original.addElementInOrder(ReportNum.REPORT2);
        original.addElementInOrder(ReportNum.REPORT3);
        ReportNumOrder other = ReportNumOrder.instance();
        other.addElementInOrder(ReportNum.REPORT1);
        other.addElementInOrder(ReportNum.REPORT2);
        other.addElementInOrder(ReportNum.REPORT3);

        assertTrue(original.equals(other));
        assertEquals(original, other);
    }

    @Test
    //Tests that the equals methods works correctly: false (different orders)
    public void ctrlEqualsFalseDifferentOrders(){
        ReportNumOrder original = ReportNumOrder.instance();
        original.addElementInOrder(ReportNum.REPORT1);
        original.addElementInOrder(ReportNum.REPORT2);
        original.addElementInOrder(ReportNum.REPORT3);
        ReportNumOrder other = ReportNumOrder.instance();
        other.addElementInOrder(ReportNum.REPORT1);
        other.addElementInOrder(ReportNum.REPORT3);
        other.addElementInOrder(ReportNum.REPORT2);

        assertFalse(original.equals(other));
        assertNotEquals(original, other);
    }

    @Test
    //Tests that the equals methods works correctly: false (different sizes)
    public void ctrlEqualsFalseDifferentSizes(){
        ReportNumOrder original = ReportNumOrder.instance();
        original.addElementInOrder(ReportNum.REPORT1);
        original.addElementInOrder(ReportNum.REPORT2);
        original.addElementInOrder(ReportNum.REPORT3);
        ReportNumOrder other = ReportNumOrder.instance();
        other.addElementInOrder(ReportNum.REPORT1);

        assertFalse(original.equals(other));
        assertNotEquals(original, other);
    }



}
