package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;

import javax.swing.*;
import java.util.function.Consumer;

public class RegisterLimitedDropInPlainPanel implements RegisterDropInterface {
    CheckLimitedDrop checkLimitedDrop;
    StrongBoxDrag strongBoxDrag;
    DepotDrag depotDrag;

    public RegisterLimitedDropInPlainPanel(CheckLimitedDrop checkLimitedDrop, StrongBoxDrag strongBoxDrag, DepotDrag depotDrag) {
        this.checkLimitedDrop = checkLimitedDrop;
        this.strongBoxDrag = strongBoxDrag;
        this.depotDrag = depotDrag;
    }

    @Override
    public void accept(Icon icon) throws IllegalActionException {
        String info = ((ImageIcon) icon).getDescription();
        String infos[] = info.split(" ");
        checkLimitedDrop.updateResCounter(ResourceType.valueOf(infos[1]));
        switch (info.split(" ")[0]){
            case "strongbox":
                strongBoxDrag.updateAfterDrop(infos[1]);
                return;
            case "depot":
                depotDrag.updateAfterDrop(infos[1] + " " + infos[2]);
            default:
                return;
        }
    }
}
