package it.polimi.ingsw;

/**
 * This interfaces is used to implement the Observer Pattern. It is used by the objects others wants to observe and which notify their Observers of their change
 */
public interface Subject {
    /**
     * Lets know the Subject that this Observer wants to be notified of its change
     * @param observer the Observer
     */
    public boolean attach(Observer observer);

    /**
     * Lets know the Subject that this Observer doesn't want to be notified of its change anymore
     * @param observer the Observer
     */
    public boolean detach(Observer observer);

    /**
     * Notifies all the interested Observers of the change in the state of the Cell
     */
    public boolean notifyObservers();

}
