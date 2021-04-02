package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * PopeCells are a subset of the ReportCells because they do belong to a Vatican Report Section but they also are responsible for the activation of the corresponding Vatican Report
 */

public class PopeCell extends ReportCell implements Subject {
    private boolean activated;
    private List<Observer> observersList;


    /**
     * Constructs a not-yet-activated PopeCell. This type cell is said to be activated when a Player crosses for the first time this cell and, therefore, the corresponding Vatican Report is set into action.
     * It also creates the list to take note of all the Observers which are interested in the Subject
     * @param vicoryPoints the victoryPoints of the cell
     * @param reportNum the Vatican Report this cell activates
     */
    public PopeCell(int vicoryPoints, ReportNum reportNum) {
        super(vicoryPoints, reportNum);
        this.activated = false;
        this.observersList = new ArrayList<Observer>();
    }

    /**
     * Sets the activated attribute to true because the Vatican Report has been called
     */
    public void setActivatedTrue() {
        this.activated = true;
    }

    /**
     * Tells whether the PopeCell has been activated previously
     * @return the value in the activated attribute
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Activates the effect of the cell which in this case is to activate the Vatican Report (among the Observers in the observersList there should be the Controller which will activate the Report).
     * It also changes the value in the activated attribute
     * @return true
     */
    @Override
    public boolean effect() {
        this.setActivatedTrue();
        return this.notifyObservers();
    }

    @Override
    public boolean attach(Observer observer) {
        return observersList.add(observer);
    }

    @Override
    public boolean detach(Observer observer) {
        return observersList.remove(observer);
    }

    @Override
    public boolean notifyObservers() {
        int tmp = 0;
        for(Observer o: observersList)
            if(o.update(true) == true)
                tmp++;
        if(tmp == observersList.size())
            return true;
        return false;
    }

    //This method is only used for testing purposes
    public String fakeEffect(Observer o){
        this.setActivatedTrue();
        return this.notifySingleObserver(o);
    }

    //This method is only used for testing purposes
    public String notifySingleObserver(Observer o){
        if(observersList.contains(o))
            return o.update();
        else
            return "Error!";
    }

    //This method is only used for testing puposes
    public List<Observer> getObserversList() {
        return observersList;
    }
}
