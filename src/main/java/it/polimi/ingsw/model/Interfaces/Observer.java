package it.polimi.ingsw.model.Interfaces;

/**
 * This interfaces is used to implement the Observer Pattern. It is used by the objects which want to observe the Subject in order to be notified of their change
 */
public interface Observer {
    /**
     * Updates the Observer that the state of the Subject has changed
     */
    //String è solo momentaneo, serve per controllare che l'Observer venga chiamato correttamente, normalmente dovrebbe essere void o al più un booleano
    String update();

    //Usato solo a fine di testing
    //boolean update(boolean tmp, ReportNum reportNum);
    //TODO: l'update dovrebbe non avere parametri, semmai è il concrete observer che chiede lo stato
    /**
     * Updates the Observer that the state of the Subject has changed
     * @param object an object which can contain useful information for the Observer on the changed state of the Subject
     */
    boolean update(Object object);
}
