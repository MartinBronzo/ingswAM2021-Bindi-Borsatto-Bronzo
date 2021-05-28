package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.panels.CardCheckbox;
import it.polimi.ingsw.view.gui.panels.MoveResourceChoice;
import it.polimi.ingsw.view.gui.panels.MainPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GuiTest {

    static CardCheckbox check;
    static JFrame gameFrame;

    public static void main(String[] args) throws InterruptedException {
        gameFrame= new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        gameFrame.setSize(600,600);
        gameFrame.setLocation(400,20);
        gameFrame.validate();

        //addInstructionPanel();

        //test for the leaderCheckboxPanel display
        //addLeaderCheckBoxPanel();

        //test for the moveResourceChoice display
        //moveResourceChoice();

        //test
        seeOthersPlayers();

        gameFrame.setVisible(true);

        //test of the leaderCheckboxPanel's checkbox functionality
        /*Thread.sleep(5000);
        System.out.println(check.getSelectedLeaderIndexes());*/
    }


    private static void addInstructionPanel(){
        InstructionPanel p = new InstructionPanel();

        p.setLabelText("This is a text");

        // add panel to frame
        gameFrame.add(p);
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

}
