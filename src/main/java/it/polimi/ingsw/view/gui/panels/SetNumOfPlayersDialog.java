package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.SetNumPlayerMessage;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SetNumOfPlayersDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private final JLabel label;
    private final JTextField textField;
    private final JButton button;


    public SetNumOfPlayersDialog(Frame owner) {
        super(owner, "How many Players there will be in the Game", true);

        label = new JLabel("Insert the number of desired players (1-4):");
        textField = new JTextField(("1-4"));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                textField.setText("");
            }
            public void focusLost(FocusEvent e) {
                // nothing
            }
        });
        button = new SubmitButton("Configure Game");
        button.addActionListener(event -> {
            try {
                int numOfPlayers = Integer.parseInt(textField.getText());
                PanelManager.getInstance().writeMessage(new Command("setNumPlayer", new SetNumPlayerMessage(numOfPlayers)));
            }catch (NumberFormatException e){
                PanelManager.getInstance().printError("You must choice a number");
            }
        });

        this.setLayout( new FlowLayout() );

        this.add(label);
        this.add(textField);
        this.add(button);
        this.setSize(panelWidth, 200);
        this.setLocation(panelXPosition, panelYPosition);


    }
}
