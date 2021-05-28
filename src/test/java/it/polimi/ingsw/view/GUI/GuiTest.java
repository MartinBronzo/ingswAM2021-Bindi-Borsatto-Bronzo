package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.gui.panels.InstructionPanel;

import javax.swing.*;

public class GuiTest {

    public static void main(String[] args){
        JFrame gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        gameFrame.setSize(600,600);
        gameFrame.setLocation(400,20);
        gameFrame.validate();


        addInstructionPanel(gameFrame);
        gameFrame.setVisible(true);
    }


    private static void addInstructionPanel(JFrame f){
        InstructionPanel p = new InstructionPanel();

        p.setLabelText("This is a text");

        // add panel to frame
        f.add(p);
    }

}
