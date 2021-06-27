package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel contains all the LeaderCardDrop panels which represents the ExtraSlot LeaderCard the player has. Onto this panels
 * resources can be dropped.
 */
public class DropLeaderCards extends JPanel implements Resettable {
    private List<LeaderCardDrop> cards;


    /**
     * Constructs a DropLeaderCards already filled with the LeaderCardDrop representing the player's ExtraSlot LeaderCard
     */
    public DropLeaderCards(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.cards = null;

        //Finds the player's cards with an ExtraSlotEffect
        List<LeaderCard> usefulCards = PanelManager.getInstance().getPlayer().getUsedLeaders().stream().filter(x -> x.getEffect() instanceof ExtraSlotLeaderEffect).collect(Collectors.toList());

        if(!usefulCards.isEmpty()) {
            this.fillLeaderCards(usefulCards);
        }
    }

    /**
     * Checks whether this panel has LeaderCardDrop panel onto it, that is whether the player has actually ExtraSlot LeaderCard active
     * @return true if there is at least one LeaderCardDrop panel displayed, false otherwise
     */
    public boolean isEmpty(){
        if(this.cards == null)
            return true;
        return this.cards.isEmpty();
    }

    private void fillLeaderCards(List<LeaderCard> usefulCards){
        this.cards = new ArrayList<>();
        HashMap<ResourceType, Integer> leaderSlots = PanelManager.getInstance().getLeaderSlots();
        Integer quantity;
        LeaderCardDrop tmp;
        for (LeaderCard lD : usefulCards) {
            quantity = leaderSlots.get(lD.getEffect().extraSlotGetType());
            if(quantity == null)
                tmp = new LeaderCardDrop(lD, 0);
            else
                tmp = new LeaderCardDrop(lD, quantity);
            this.cards.add(tmp);
            this.add(tmp);
        }
    }

    /**
     * Prepares the LeaderCardDrop in this panel to take drops from the specified source of limited draggable resources (the LimitedResourceDrag panel)
     * @param checkDropFunction the DropChecker function to be used to checks the drop onto this DropLeaderCards
     * @param resDrag a source of limited draggable resources
     */
    public void initFromFiniteRes(DropChecker checkDropFunction, LimitedResourcesDrag resDrag){
        for(LeaderCardDrop lDD : this.cards)
            lDD.initFromFiniteRes(checkDropFunction, resDrag);
    }

    @Override
    public void resetState(){
        for(LeaderCardDrop lDD : this.cards)
            lDD.resetState();
    }

    /**
     * Returns the list of LeaderCardDrop displayed onto this panel
     * @return the list of LeaderCardDrop displayed onto this panel
     */
    public List<LeaderCardDrop> getCards() {
        return cards;
    }
}
