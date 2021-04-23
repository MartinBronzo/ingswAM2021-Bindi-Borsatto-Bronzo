package it.polimi.ingsw.FaithTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class takes track of the specified order of the ReportNum. This class implements the Singleton Patter and it
 * doesn't let change eliminate ReportNums after they are added because throughout the game the ReportNum order shouldn't change.
 * It doesn't contain any null elements.
 */
public class ReportNumOrder {
    private static ReportNumOrder instance;
    private List<ReportNum> order;

    /**
     * Constructs a new ReportNumOrder without any particular predefined oder. It is private because this class implements the Singleton pattern-
     */
    private ReportNumOrder() {
        this.order = new ArrayList<>();
    }

    /**
     * Constructs the only instance of the ReportNumOrder class. It constructs the object the first time this method is called. The other times it simply returns
     * the instance already constructed.
     *
     * @return the only instance of the class
     */
    public static ReportNumOrder instance() {
        if (instance == null)
            instance = new ReportNumOrder();
        return instance;
    }

    /**
     * Appends a ReportNum to the end of this list. It only adds element if they are not already present
     *
     * @param reportNum element to be added to the list
     * @return true if it added the element, false otherwise
     * @throws NullPointerException if the ReportNum is a null pointer
     */
    public boolean addElementInOrder(ReportNum reportNum) throws NullPointerException {
        if (reportNum == null)
            throw new NullPointerException("Get a ReportNum before passing it as an input!");
        if (!this.order.contains(reportNum)) {
            this.order.add(reportNum);
            return true;
        }
        return false;
    }


    /**
     * States which of the ReportNum given in inputs comes first
     *
     * @param x a ReportNum
     * @param y a ReportNum
     * @return true if x comes first, false otherwise (y comes first or the two have the same priority)
     * @throws IllegalArgumentException if one of the ReportNum is not in the list
     */
    @Deprecated
    //This method right now is only used for testing purposes
    public boolean stateOrder(ReportNum x, ReportNum y) throws IllegalArgumentException {
        if (!this.order.contains(x))
            throw new IllegalArgumentException("The first element given as a parameter is not in the list");
        if (!this.order.contains(y))
            throw new IllegalArgumentException("The second element given as a parameter is not in the list");
        return this.order.indexOf(x) < this.order.indexOf(y);
    }

    /**
     * Returns the priority of the ReportNum given as a parameter
     *
     * @param reportNum a ReportNum
     * @return the index of the ReportNum
     */
    public int getOrder(ReportNum reportNum) {
        return this.order.indexOf(reportNum);
    }

    //This method is only used for testing purposes
    public boolean isEmpty() {
        return this.order.isEmpty();
    }

    //This method is only used for testing purposes
    //NOT TO BE USED IN THE GAME
    public static void deleteState() {
        instance = null;
    }

    public ReportNum getReportNum(int index) {
        return this.order.get(index);
    }

    public int getSize(){
        return this.order.size();
    }
}
