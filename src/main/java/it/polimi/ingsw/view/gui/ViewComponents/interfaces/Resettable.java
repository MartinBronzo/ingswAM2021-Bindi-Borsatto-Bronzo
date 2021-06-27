package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

/**
 * The objects that implement this interface can have their inner state brought back at the original state, before any drop have taken place.
 */
public interface Resettable {

    /**
     * Resets the state of this object to what the object looked like when it was just constructed
     */
    void resetState();

}
