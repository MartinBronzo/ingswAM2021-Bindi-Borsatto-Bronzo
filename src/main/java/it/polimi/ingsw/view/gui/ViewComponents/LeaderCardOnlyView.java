package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;

import javax.swing.*;

public class LeaderCardOnlyView extends JPanel {

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
            resource = new JLabel(new ImageIcon(DepotDrop.getImagePathFromResource(resourceType)));
            this.add(resource);
        }
    }
}
