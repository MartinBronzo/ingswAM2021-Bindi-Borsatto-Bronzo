package it.polimi.ingsw.view.gui.View;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.view.gui.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JDialog {
    private final JLabel label;
    private final JTextField textField;
    private final JButton button;


    public Welcome(Frame owner) {
        super(owner, "What's your Nickname?", true);

        label = new JLabel("Insert your Nickname:");
        textField = new JTextField(("Nickname"));
        button = new JButton("Login");
        button.addActionListener(event -> PanelManager.getInstance().writeMessage(new Command("login", new LoginMessage(textField.getText()))));

        this.setLayout( new FlowLayout() );

        this.add(label);
        this.add(textField);
        this.add(button);
        this.setSize(600, 200);


    }
}
