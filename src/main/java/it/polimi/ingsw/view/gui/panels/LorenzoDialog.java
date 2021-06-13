package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

public class LorenzoDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;
    private JLabel image;

    public LorenzoDialog(Frame owner) {
        super(owner, "INFO :D ", true);

        label = new JLabel();
        image = new JLabel();

        this.setLayout( new FlowLayout());
        this.add(label);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(image);

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

        this.setSize(panelWidth, 150);
        setLocation(panelXPosition,panelYPosition);

    }


    public void setInfoMessage(String info){
        label.setText(label.getText()+" "+info);
    }

    public void setTokenImage(String path){
        image.setIcon(scaleImage(path, 100, 100));
        image.setVisible(true);
    }

    private ImageIcon scaleImage(String image, int width, int height) {
        //scale image
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(dimg);
        //ImageIcon icon = new ImageIcon(leader);
    }

}
