package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

public class RegisterLimitedDropInPlainPanel implements RegisterDropInterface {
    private CheckLimitedDrop checkLimitedDrop;
    private PanelDrop panelTarget;
    private StrongBoxDrag strongBoxDrag;
    private DepotDrag depotDrag;
    private DragLeaderCards leaders;



    @Deprecated
    public RegisterLimitedDropInPlainPanel(CheckLimitedDrop checkLimitedDrop, StrongBoxDrag strongBoxDrag, DepotDrag depotDrag, PanelDrop panelTarget) {
        this.checkLimitedDrop = checkLimitedDrop;
        this.panelTarget = panelTarget;
        this.strongBoxDrag = strongBoxDrag;
        this.depotDrag = depotDrag;
    }

    public RegisterLimitedDropInPlainPanel(CheckLimitedDrop checkLimitedDrop, StrongBoxDrag strongBoxDrag, DepotDrag depotDrag,
                                           DragLeaderCards leaders, PanelDrop panelTarget) {
        this.checkLimitedDrop = checkLimitedDrop;
        this.panelTarget = panelTarget;
        this.strongBoxDrag = strongBoxDrag;
        this.depotDrag = depotDrag;
        this.leaders = leaders;
    }


    @Override
    public void accept(Icon icon) throws IllegalActionException {
        String info = ((ImageIcon) icon).getDescription();
        String infos[] = info.split(" ");
        checkLimitedDrop.updateResCounter(ResourceType.valueOf(infos[1]));
        switch (info.split(" ")[0]){
            case "strongbox":
                strongBoxDrag.updateAfterDrop(infos[1]);
                panelTarget.updateStrongBoxCounter(infos[1]);
                return;
            case "depot":
                depotDrag.updateAfterDrop(infos[1] + " " + infos[2]);
                panelTarget.updateDepotCounter(infos[1], infos[2]);
                break;
            case "leaderCard":
                leaders.updateAfterDrop(infos[1]);
                panelTarget.updateLeaderCardsCounter(infos[1]);
                break;
            default:
                return;
        }
    }


}
