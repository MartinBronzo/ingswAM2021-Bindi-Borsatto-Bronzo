package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class RegisterLimitedDropInPlainPanel implements RegisterDropInterface {
    private CheckLimitedDrop checkLimitedDrop;
    private PanelDrop panelTarget;
    private StrongBoxDrag strongBoxDrag;
    private DepotDrag depotDrag;



    public RegisterLimitedDropInPlainPanel(CheckLimitedDrop checkLimitedDrop, StrongBoxDrag strongBoxDrag, DepotDrag depotDrag, PanelDrop panelTarget) {
        this.checkLimitedDrop = checkLimitedDrop;
        this.panelTarget = panelTarget;
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
                panelTarget.updateStrongBox(infos[1]);
                return;
            case "depot":
                depotDrag.updateAfterDrop(infos[1] + " " + infos[2]);
                panelTarget.updateDepot(infos[1], infos[2]);
            //TODO: ci sarà anche da fare un case "leaderCard" e fromLeaderCard quando metteremo le leaderCard che possono contenere le risorse
            default:
                return;
        }
    }


}
