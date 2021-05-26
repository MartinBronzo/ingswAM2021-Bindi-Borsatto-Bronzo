package it.polimi.ingsw.view.gui;

import javax.swing.*;

public class GuiClient {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                    public void run() {
                        createAndShowGUI();
                        createAndShowGUI();
                    }
            }
            );
    }
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new CustomFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250,250);
        f.setVisible(true);
    } }