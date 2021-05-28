package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.network.messages.fromClient.SetNumPlayerMessage;

import javax.swing.*;
import java.awt.*;

public class SetNumOfPlayersDialog extends JDialog {
    private final JLabel label;
    private final JTextField textField;
    private final JButton button;


    public SetNumOfPlayersDialog(Frame owner) {
        super(owner, "How many Players there will be in the Game", true);

        label = new JLabel("Insert the number of desired players:");
        textField = new JTextField(("1-4"));
        button = new JButton("Configure Game");
        button.addActionListener(event -> {
            try {
                int numOfPlayers = Integer.parseInt(textField.getText());
                PanelManager.getInstance().writeMessage(new Command("setNumPlayer", new SetNumPlayerMessage(numOfPlayers)));
            }catch (NumberFormatException e){
                //TODO: not a number error display
                System.out.println("configure Game Text Field is not a number");
            }
        });

        this.setLayout( new FlowLayout() );

        this.add(label);
        this.add(textField);
        this.add(button);
        this.setSize(600, 200);


    }
}
