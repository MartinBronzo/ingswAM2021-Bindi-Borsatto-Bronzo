package it.polimi.ingsw.view.gui.mainViews.dialogs;

import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InfoDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;

    public InfoDialog(Frame owner) {
        super(owner, "INFO :D ", true);

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


    public void setInfoMessage(String info){
        label.setText(label.getText()+" "+info);
    }
}
