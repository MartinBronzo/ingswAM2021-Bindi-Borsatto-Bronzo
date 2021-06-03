package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DnDDepot;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

//TODO: MAGARI CAMBIARE A DIALOG
public class BeginningDecisionsPanel extends JPanel {
    /*private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();*/

    public BeginningDecisionsPanel(Frame owner, ArrayList<String> leaderList){
       // super(owner, "Set Beginning Decisions", true);
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel discardLeaderPanel = new JPanel();
        discardLeaderPanel.setLayout(new BoxLayout(discardLeaderPanel, BoxLayout.PAGE_AXIS));
        discardLeaderPanel.setBorder(new TitledBorder("Select 2 Leader Cards to discard"));


        CardCheckbox leaderCheckbox1 = new CardCheckbox(leaderList.subList(0,2), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox1);
        CardCheckbox leaderCheckbox2 = new CardCheckbox(leaderList.subList(2,4), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox2);

        this.add(discardLeaderPanel);

        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
        rightSidePanel.add(new DnDDepot());
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setLabelText("Select 2 Leader Cards to discard and place the resources, if present, in the depot");
        rightSidePanel.add(instructionPanel);

        this.add(rightSidePanel);

//        this.setSize(panelWidth, 100);
       // setLocation(panelXPosition,panelYPosition);

    }


}
