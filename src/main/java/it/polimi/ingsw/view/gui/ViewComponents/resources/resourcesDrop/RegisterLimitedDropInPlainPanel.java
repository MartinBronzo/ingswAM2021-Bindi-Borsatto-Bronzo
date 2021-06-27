package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.CheckLimitedDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.PanelDrop;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxDrag;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag.DepotDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag.DragLeaderCards;

import javax.swing.*;

/**
 * This RegisterDropInterface functions register the drops that have been made to a PanelDrop.
 */
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

    /**
     * Constructs a RegisterLimitedDropInPlainPanel
     * @param checkLimitedDrop the CheckLimitedDrop function used to check the drops onto the specified PanelDrop
     * @param strongBoxDrag the player's StrongBox which stored resources can be dropped onto the specified PanelDrop
     * @param depotDrag the player's Depot which stored resources can be dropped onto the specified PanelDrop
     * @param leaders the player's ExtraSlot LeaderCard which stored resources can be dropped onto the specified PanelDrop
     * @param panelTarget the PanelDrop the players drops resources onto
     */
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
