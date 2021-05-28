package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.gui.ViewComponents.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.UndoButton;

import javax.swing.*;
import java.awt.*;

public class GuiTest {

    public static void main(String[] args){
        JFrame gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        gameFrame.setSize(600,600);
        gameFrame.setLocation(400,20);
        gameFrame.validate();


        //addInstructionPanel(gameFrame);
        printButtons(gameFrame);
        gameFrame.setVisible(true);
    }


    private static void addInstructionPanel(JFrame f){
        InstructionPanel p = new InstructionPanel();

        p.setLabelText("This is a loooooooooooooooooooooooooooooooooooooooooooooooooong text");
        p.setVisible(true);

        p.setButtonActionListener(event -> System.out.println("A click"));

        JPanel allPanel = new JPanel();
        allPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 2;
        c.gridy = 2;
        allPanel.add(p, c);


        f.add(allPanel);
    }

    private static void printButtons(JFrame f){
        JButton submit = new SubmitButton("Confirm");
        JButton back = new BackButton("Back");
        JButton undo = new UndoButton("Undo");

        f.add(submit);
        f.add(back);
        f.add(undo);
    }

}
