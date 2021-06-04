package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ErrorDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;

    public ErrorDialog(Frame owner) {
        super(owner, "ERROR :( ", true);

        label = new JLabel("error message");


        this.setLayout( new FlowLayout() );
        this.add(label);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setText("");
                setVisible(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.setSize(panelWidth, 100);
        setLocation(panelXPosition,panelYPosition);

    }


    public void setErrorMessage(String error){
        label.setText(label.getText()+" "+error);
    }
}
