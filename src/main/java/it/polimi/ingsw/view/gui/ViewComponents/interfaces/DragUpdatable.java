package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

/**
 * The objects which implement this interface are updated after an object dragged away from them has been dropped in another place.
 */
public interface DragUpdatable {

    /**
     * Updates this object that an object dragged away from them has been dropped in another place
     * @param info the information about the drop (the name of the resource dragged away eventually followed by the number of the shelf of the depot)
     */
    void updateAfterDrop(String info);

    //void updateAfterDragBegin(String info);

    //void undoDrag(String info);
}
