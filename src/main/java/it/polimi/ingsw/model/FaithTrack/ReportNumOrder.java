package it.polimi.ingsw.model.FaithTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class takes track of the specified order of the ReportNum. It doesn't let eliminate ReportNums after they are added because throughout the game the ReportNum order shouldn't change.
 * It doesn't contain any null elements.
 */
public class ReportNumOrder {
    private List<ReportNum> order;

    /**
     * Constructs a new ReportNumOrder without any particular predefined oder.
     */
    private ReportNumOrder() {
        this.order = new ArrayList<>();
    }

    /**
     * Returns a newly-constructed ReportNumOrder object.
     *
     * @return a ReportNumOrder object
     */
    //The reason this class seems to implement the Singleton pattern even if it doesn't because it used to be a singleton.
    public static ReportNumOrder instance() {
        return new ReportNumOrder();
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
    //Once upon a time this method was actually useful
    @Deprecated
    public static void deleteState(){
        ;
    }

    public ReportNum getReportNum(int index) {
        return this.order.get(index);
    }

    public int getSize() {
        return this.order.size();
    }

    /**
     * Constructs a copy of the specified ReportNumOrder
     * @param original the ReportNumOrder to be cloned
     */
    public ReportNumOrder(ReportNumOrder original){
        this();
        for(ReportNum rN: original.order)
            this.addElementInOrder(rN);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ReportNumOrder))
            return false;
        ReportNumOrder tmp = (ReportNumOrder) obj;
        if(this.order.size() != tmp.order.size())
            return false;
        for(int i = 0; i < this.order.size(); i++)
            if(!this.order.get(i).equals(tmp.order.get(i)))
                return false;
        return true;
    }
}
