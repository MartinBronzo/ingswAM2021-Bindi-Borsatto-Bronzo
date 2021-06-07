package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DropLeaderCards extends JPanel implements Resettable {
    private List<LeaderCardDrop> cards;

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

    public void initFromFiniteRes(DropChecker checkDropFunction, LimitedResourcesDrag resDrag){
        for(LeaderCardDrop lDD : this.cards)
            lDD.initFromFiniteRes(checkDropFunction, resDrag);
    }

    @Override
    public void resetState(){
        for(LeaderCardDrop lDD : this.cards)
            lDD.resetState();
    }

    public List<LeaderCardDrop> getCards() {
        return cards;
    }
}
