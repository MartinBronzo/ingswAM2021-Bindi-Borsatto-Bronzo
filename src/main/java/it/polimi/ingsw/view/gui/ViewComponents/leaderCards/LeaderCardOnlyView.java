package it.polimi.ingsw.view.gui.ViewComponents.leaderCards;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;

import javax.swing.*;

/**
 * This panel represents an ExtraSlot LeaderCard which can only be seen by the user.
 */
public class LeaderCardOnlyView extends JPanel {

    /**
     * Constructs a LeaderCardOnlyView which represents the specified ExtraSlot LeaderCard and with the specified amount of resources stored onto
     * @param leader the ExtraSlot LeaderCard this panel represents
     * @param alreadyStored the amount of resources the player stores onto this card
     */
    public LeaderCardOnlyView(LeaderCard leader, int alreadyStored){
        super();

        JLabel label = new JLabel();
        ImageIcon image = LeaderCardPanel.scaleImage(leader.getUrl(), 100, 150);
        //ImageIcon image = new ImageIcon(leader.getUrl());
        label.setIcon(image);
        label.setAlignmentX(LEFT_ALIGNMENT);

        this.add(label);

        this.fillLeaderCard(leader.getEffect().extraSlotGetType(), alreadyStored);
    }

    private void fillLeaderCard(ResourceType resourceType, int alreadyPresent){
        JLabel resource;
        for(int i = 0; i < alreadyPresent; i++){
            resource = new JLabel(new ImageIcon(getClass().getResource(DepotDrop.getImagePathFromResource(resourceType))));
            this.add(resource);
        }
    }
}
