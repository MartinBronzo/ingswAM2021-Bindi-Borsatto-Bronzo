package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.gui.ViewComponents.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD.CheckBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD.DragAndDropBaseProd;
import it.polimi.ingsw.view.gui.panels.CardCheckbox;
import it.polimi.ingsw.view.gui.panels.MoveResourceChoice;
import it.polimi.ingsw.view.gui.panels.MainPanel;
import it.polimi.ingsw.view.gui.ViewComponents.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.UndoButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiTest {

    static CardCheckbox check;
    static JFrame gameFrame;
    static DragAndDropBaseProd dragAndDropBaseProd;

    public static void main(String[] args) throws InterruptedException {
        gameFrame= new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(true);

        gameFrame.setSize(600,600);
        gameFrame.setLocation(400,20);
        gameFrame.validate();

        //addInstructionPanel();

        //test for the leaderCheckboxPanel display
        //addLeaderCheckBoxPanel();

        //test for the moveResourceChoice display
        //moveResourceChoice();

        //test
        //seeOthersPlayers();

        //test for base production
        baseProduction();

        //printButtons(gameFrame);
        gameFrame.setVisible(true);

        //test of the leaderCheckboxPanel's checkbox functionality
        /*Thread.sleep(5000);
        System.out.println(check.getSelectedLeaderIndexes());*/

        //test for base production choices
        Thread.sleep(5000);
        System.out.println("input: " + dragAndDropBaseProd.getInputs() + " output: " + dragAndDropBaseProd.getOutput());

    }


    private static void addInstructionPanel(){
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

        gameFrame.add(allPanel);
    }

    private static void printButtons(JFrame f){
        JButton submit = new SubmitButton("Confirm");
        JButton back = new BackButton("Back");
        JButton undo = new UndoButton("Undo");

        f.add(submit);
        f.add(back);
        f.add(undo);
    }

    private static void addLeaderCheckBoxPanel() throws InterruptedException {
        List<String> leaderList = new ArrayList<>();
        leaderList.add("src/main/resources/PUNCHBOARD/croce.png");
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio7.png");

        check = new CardCheckbox(leaderList, "use this Dev Card");
        gameFrame.add(check);
    }

    private static void moveResourceChoice(){
        MoveResourceChoice panel = new MoveResourceChoice();

        gameFrame.add(panel);
    }

    private static void seeOthersPlayers(){
        List<String> playersNames = new ArrayList<>();
        playersNames.add("satto");
        playersNames.add("ludo");
        playersNames.add("martin");

        MainPanel mainPanel = new MainPanel(playersNames, "satto");
        gameFrame.add(mainPanel);
    }

    private static void baseProduction(){
        dragAndDropBaseProd = new DragAndDropBaseProd();
        dragAndDropBaseProd.setCheckDropFunction(new CheckBaseProd(dragAndDropBaseProd));
        gameFrame.add(dragAndDropBaseProd);

    }

}
