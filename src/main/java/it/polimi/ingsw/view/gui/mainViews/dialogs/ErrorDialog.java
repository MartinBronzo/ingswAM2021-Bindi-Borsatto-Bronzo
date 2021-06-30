package it.polimi.ingsw.view.gui.mainViews.dialogs;

import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This dialog is used to display an error message to the player.
 */
public class ErrorDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;

    /**
     * Constructs an ErrorDialog which still needs the message, it is going to display, to be set
     * @param owner the Frame of the player
     */
    public ErrorDialog(Frame owner) {
        super(owner, "ERROR :( ", true);

        label = new JLabel();


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

    /**
     * Sets the message to be shown to the player
     * @param error the message to be shown to the player
     */
    public void setErrorMessage(String error){
        label.setText(label.getText()+" "+error);
    }
}
