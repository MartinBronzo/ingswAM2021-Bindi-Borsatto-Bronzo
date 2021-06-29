package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveBetweenShelves;
import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveLeaderToShelf;
import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveShelfToLeader;

import javax.swing.*;
import java.awt.*;

/**
 * This panel shows to the player all the possible move of resources actions they can make.
 */
public class MoveResourceChoice extends JPanel {
    private boolean created = false;

    /**
     * Constructs a dumb MarketPlacingResources
     * @param created boolean
     */
    public MoveResourceChoice(boolean created){
        this.created = created;
    }

    /**
     * Constructs a MoveResourceChoice panel ready to be used
     */
    public MoveResourceChoice(){
        final String MOVEBTW = "Move between shelves";
        final String MOVESHELFTOLEAD = "Move from Shelf to Leader Depot";
        final String MOVELEADTOSHELF = "Move from Leader Depot to Shelf" ;

        String[] comboBoxItems = { MOVEBTW, MOVESHELFTOLEAD, MOVELEADTOSHELF };

        //orientation of the outside panel
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.created = true;

        //creates the layouts to display with the configurations of the JComboBox
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new CardLayout());

        //TODO: MANCANO I LISTENER
        MoveBetweenShelves moveBetweenShelvesPanel = new MoveBetweenShelves();
        MoveLeaderToShelf moveLeaderToShelfPanel = new MoveLeaderToShelf();
        MoveShelfToLeader moveShelfToLeaderPanel = new MoveShelfToLeader();
        resourcePanel.add(moveBetweenShelvesPanel, MOVEBTW);
        resourcePanel.add(moveLeaderToShelfPanel, MOVESHELFTOLEAD);
        resourcePanel.add(moveShelfToLeaderPanel, MOVELEADTOSHELF);

        JPanel selectionPanel = new JPanel();

        JLabel infoText = new JLabel("Select the action you want to do:");
        infoText.setAlignmentX(LEFT_ALIGNMENT);
        selectionPanel.add(infoText);

        JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);
        comboBox.setEditable(false);
        comboBox.setPrototypeDisplayValue(comboBoxItems[2]);
        comboBox.addItemListener(e -> {
            CardLayout cl = (CardLayout)(resourcePanel.getLayout());
            cl.show(resourcePanel, (String)e.getItem());
        });
        comboBox.setAlignmentX(LEFT_ALIGNMENT);

        JPanel comboPanel = new JPanel();
        comboPanel.add(comboBox);
        selectionPanel.add(comboPanel);
        this.add(selectionPanel);
        this.add(resourcePanel);
    }

    /**
     * Returns whether the object has been created
     * @return true
     */
    public boolean isCreated(){return created;}

}
