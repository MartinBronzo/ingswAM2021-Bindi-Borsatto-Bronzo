package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

public class RegisterBaseProdDrop implements RegisterDropInterface {
    private final BaseProdPanel baseProdPanel;

    public RegisterBaseProdDrop(BaseProdPanel dropTarget){
        this.baseProdPanel = dropTarget;
    }

    @Override
    public void accept(Icon icon) {
        switch (icon.toString()){
            case "src/main/resources/coins small.png":
                baseProdPanel.addDecision(ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                baseProdPanel.addDecision(ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                baseProdPanel.addDecision(ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                baseProdPanel.addDecision(ResourceType.STONE);
                break;
        }
    }


}
