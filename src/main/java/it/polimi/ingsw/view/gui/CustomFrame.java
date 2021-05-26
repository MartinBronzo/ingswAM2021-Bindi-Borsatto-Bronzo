package it.polimi.ingsw.view.gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CustomFrame extends JFrame {

    public void paint(Graphics g) {
        g.drawString("Hello", 200, 50); int x = 30;
        int y = 100;
        int rectwidth = 50;
        int rectheight = 100;
        Color c = Color.red;
        g.setColor(c);
        x=100;
        g.drawRect(x, y, rectwidth, rectheight);
        this.myDrawImage(g);
    }

    private void myDrawImage(Graphics g){
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream("PlayerBoard.jpg");
        BufferedImage img= null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        g.drawImage(img, 10,20, 50,50, null); }
}