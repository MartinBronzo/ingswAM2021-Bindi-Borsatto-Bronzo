package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;
import it.polimi.ingsw.model.ResourceType;

import javax.swing.*;
import java.util.function.Consumer;

public class RegisterDrop implements Consumer<Icon> {
    private final MyDepotPanel panel;

    public RegisterDrop(MyDepotPanel panel){
        this.panel = panel;
    }
    @Override
    public void accept(Icon icon) {
        //TODO: controllare che non si possano aggiungere più risorse di quanto sia lo spazio libero (le risorse presenti saranno visualizzate da - e forse salvate nel - MyDepotPanel: guardare lì per capire quante risorse l'utente può mettere)
        switch (icon.toString()){
            case "src/main/resources/coins small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.STONE);
                break;
        }
    }
}