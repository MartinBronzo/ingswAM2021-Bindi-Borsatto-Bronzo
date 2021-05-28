package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * This panel shows the panel of the playerBoard of the selected player in the JComboBox
 */
public class MainPanel extends JPanel {

    /**
     * shows dynamically the playerboard of the selected player
     * @param playersNicks list of names of all the players in the game
     * @param guisPlayerName the name of the player that controls the GUI
     */
    public MainPanel(List<String> playersNicks, String guisPlayerName) {

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel playerBoardPanel = new JPanel();
        playerBoardPanel.setLayout(new CardLayout());

        //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
        //adds first the actual player because this is the default value of the component
        /*ActualPlayerBoardPanel actualPlayerBoardPanel = new ActualPlayerBoardPanel();
        playerBoardPanel.add(actualPlayerBoardPanel, playerName);*/

        //TODO: questa è una prova, cancellabile
        /*JPanel prova2 = new JPanel();
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setLabelText("aaaaaaa");
        instructionPanel.setButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionPanel.setLabelText("seeeee");
            }
        });
        prova2.add(instructionPanel);
        playerBoardPanel.add(prova2, guisPlayerName);*/

        for (String playerName : playersNicks) {
            //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
            if (!playerName.equals(guisPlayerName)) {
                /*OthersPlayerBoardPanel othersPlayerBoardPanel = new OthersPlayerBoardPanel();
                playerBoardPanel.add(othersPlayerBoardPanel, playerName);*/

                //TODO: questa è una prova, cancellabile
                /*JPanel prova1 = new JPanel();
                prova1.add(new JLabel("prova panel 1"));
                playerBoardPanel.add(prova1, playerName);*/
            }
        }


        JComboBox<String> comboBox = new JComboBox<>(playersNicks.toArray(new String[0]));
        comboBox.setEditable(false);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) (playerBoardPanel.getLayout());
                cl.show(playerBoardPanel, (String) e.getItem());
            }
        });

        this.add(playerBoardPanel);
        this.add(comboBox);
    }


}
