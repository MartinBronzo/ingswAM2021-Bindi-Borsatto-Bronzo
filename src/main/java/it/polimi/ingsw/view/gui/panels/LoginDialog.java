package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private final JLabel label;
    private final JTextField textField;
    private final JButton button;


    public LoginDialog(Frame owner) {
        super(owner, "What's your Nickname?", true);

        label = new JLabel("Insert your Nickname:");
        textField = new JTextField(("Nickname"));
        button = new JButton("Login");
        button.addActionListener(event -> PanelManager.getInstance().writeMessage(new Command("login", new LoginMessage(textField.getText()))));

        this.setLayout( new FlowLayout() );

        this.add(label);
        this.add(textField);
        this.add(button);
        this.setSize(panelWidth, 200);
        this.setLocation(panelXPosition, panelYPosition);


    }
}
