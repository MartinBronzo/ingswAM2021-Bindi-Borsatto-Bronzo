package it.polimi.ingsw.Interfaces;

import it.polimi.ingsw.FaithTrack.ReportNum;

/**
 * This interfaces is used to implement the Observer Pattern. It is used by the objects which want to observe the Subject in order to be notified of their change
 */
public interface Observer {
    /**
     * Updates the Observer that the state of the Subject has changed
     */
    //String è solo momentaneo, serve per controllare che l'Observer venga chiamato correttamente, normalmente dovrebbe essere void o al più un booleano
    public String update();

    //Usato solo a fine di testing
    public boolean update(boolean tmp, ReportNum reportNum);
}
