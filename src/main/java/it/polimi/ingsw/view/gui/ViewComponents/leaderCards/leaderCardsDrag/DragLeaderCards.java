package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel contains all the LeaderCardDrag panels which represents the ExtraSlot LeaderCard the player has. From this panels
 * the stored resources onto the player's cards can be dragged away.
 */
public class DragLeaderCards extends JPanel implements Resettable, DragUpdatable {
    private List<LeaderCardDrag> cards;

    /**
     * Constructs a DragLeaderCards already filled with the LeaderCardDrag representing the player's ExtraSlot LeaderCard
     */
    public DragLeaderCards(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.cards = null;

        //Finds the player's cards with an ExtraSlotEffect
        List<LeaderCard> usefulCards = PanelManager.getInstance().getPlayer().getUsedLeaders().stream().filter(x -> x.getEffect() instanceof ExtraSlotLeaderEffect).collect(Collectors.toList());

        if(!usefulCards.isEmpty()) {
            this.fillLeaderCards(usefulCards);
        }

    }

    private void fillLeaderCards(List<LeaderCard> usefulCards) {
        this.cards = new ArrayList<>();
        HashMap<ResourceType, Integer> leaderSlots = PanelManager.getInstance().getLeaderSlots();
        Integer quantity;
        LeaderCardDrag tmp;
        for (LeaderCard lD : usefulCards) {
            quantity = leaderSlots.get(lD.getEffect().extraSlotGetType());
            if (quantity == null)
                tmp = new LeaderCardDrag(lD, 0);
            else
                tmp = new LeaderCardDrag(lD, quantity);
            this.cards.add(tmp);
            this.add(tmp);
        }

    }

    /**
     * Checks whether this panel has LeaderCardDrag panel onto it, that is whether the player has actually ExtraSlot LeaderCard active
     * @return true if there is at least one LeaderCardDrag panel displayed, false otherwise
     */
    public boolean isEmpty(){
        if(this.cards == null)
            return true;
        return this.cards.isEmpty();
    }


    @Override
    public void resetState() {
        for(LeaderCardDrag lDD : this.cards)
            lDD.resetState();
    }

    @Override
    public void updateAfterDrop(String info) {
        ResourceType type = ResourceType.valueOf(info);

        for(LeaderCardDrag lDD : this.cards)
            if(lDD.getResStored().equals(type)){
                lDD.updateAfterDrop(info);
                return;
            }
    }
}
