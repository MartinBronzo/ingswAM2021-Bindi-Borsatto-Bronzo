package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.model.ResourceType;

import javax.swing.*;
import java.util.function.Consumer;

public class RegisterBaseProdDrop implements Consumer<Icon> {
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
