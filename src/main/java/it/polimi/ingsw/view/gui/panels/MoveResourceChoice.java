package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MoveResourceChoice extends JPanel {

    public MoveResourceChoice(){
        final String MOVEBTW = "Move between shelves";
        final String MOVESHELFTOLEAD = "Move from Shelf to Leader Depot";
        final String MOVELEADTOSHELF = "Move from Leader Depot to Shelf" ;

        String[] comboBoxItems = { MOVEBTW, MOVESHELFTOLEAD, MOVELEADTOSHELF };

        //orientation of the outside panel
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //creates the layouts to display with the configurations of the JComboBox
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new CardLayout());

        //TODO: RIMETTERE QUESTA PARTE QUANDO LE CLASSI SONO COMPLETE
        //TODO: MANCANO I LISTENER
        /*MoveBetweenShelvesPanel moveBetweenShelvesPanel = new MoveBetweenShelvesPanel();
        MoveLeaderToShelfPanel moveLeaderToShelfPanel = new MoveLeaderToShelfPanel();
        MoveShelfToLeaderPanel moveShelfToLeaderPanel = new MoveShelfToLeaderPanel();
        resourcePanel.add(moveBetweenShelvesPanel, MOVEBTW);
        resourcePanel.add(moveLeaderToShelfPanel, MOVESHELFTOLEAD);
        resourcePanel.add(moveShelfToLeaderPanel, MOVELEADTOSHELF);*/

        JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);
        comboBox.setEditable(false);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout)(resourcePanel.getLayout());
                cl.show(resourcePanel, (String)e.getItem());
            }
        });
        this.add(comboBox);

        this.add(resourcePanel);




    }

}
