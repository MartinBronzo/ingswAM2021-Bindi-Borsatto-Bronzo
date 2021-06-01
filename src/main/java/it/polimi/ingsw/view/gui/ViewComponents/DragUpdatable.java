package it.polimi.ingsw.view.gui.ViewComponents;

public interface DragUpdatable {

    void updateAfterDragBegin(String info);

    void undoDrag(String info);
}
