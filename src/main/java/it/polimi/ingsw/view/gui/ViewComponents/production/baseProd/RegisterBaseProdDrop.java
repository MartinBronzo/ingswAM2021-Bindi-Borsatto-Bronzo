package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

/**
 * This RegisterDropInterface function is used to take tracks of the drop that have been made onto the BaseProduction panel.
 */
public class RegisterBaseProdDrop implements RegisterDropInterface {
    private final BaseProdPanel baseProdPanel;

    /**
     * Constructs a RegisterBaseProdDrop function used to register the drops that occur onto the specified BaseProdPanel
     * @param dropTarget the BaseProdPanel whose drops this function takes track of
     */
    public RegisterBaseProdDrop(BaseProdPanel dropTarget){
        this.baseProdPanel = dropTarget;
    }

    @Override
    public void accept(Icon icon) {
        ResourceType imageType = ResourceType.valueOf(((ImageIcon) icon).getDescription().split(" ")[1]);

        //Updates the DropTarget about what drop has been made
        baseProdPanel.addDecision(imageType);


        /*switch (icon.toString()){
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
        }*/
    }


}
