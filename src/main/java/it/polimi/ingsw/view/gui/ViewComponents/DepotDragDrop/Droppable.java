package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;

public interface Droppable {

    void addDecision(Integer shelf, ResourceType res);

    int getShelfNumber();
}