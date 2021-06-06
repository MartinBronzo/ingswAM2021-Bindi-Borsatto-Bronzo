package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.RegisterDropInterface;

import javax.swing.*;
import java.util.function.Consumer;

public class RegisterDropFromInfiniteRes implements RegisterDropInterface {
    private final Droppable dropTarget;

    public RegisterDropFromInfiniteRes(Droppable dropTarget){
        this.dropTarget = dropTarget;
    }
    @Override
    public void accept(Icon icon) {
        //TODO: controllare che non si possano aggiungere più risorse di quanto sia lo spazio libero (le risorse presenti saranno visualizzate da - e forse salvate nel - ShelfDrop: guardare lì per capire quante risorse l'utente può mettere)
        switch (icon.toString()){
            case "src/main/resources/coins small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.STONE);
                break;
        }
    }
}