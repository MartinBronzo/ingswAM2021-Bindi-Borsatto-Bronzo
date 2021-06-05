package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.ViewComponents.CardCheckbox;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.CheckDropAtBeginningDecisionsTime;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.CollectBeginningChoices;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DnDDepot;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;

//TODO: MAGARI CAMBIARE A DIALOG
public class BeginningDecisionsPanel extends JPanel {

    public BeginningDecisionsPanel(ArrayList<String> leaderList, int numRes, int numLeaders){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel discardLeaderPanel = new JPanel();
        discardLeaderPanel.setLayout(new BoxLayout(discardLeaderPanel, BoxLayout.PAGE_AXIS));
        discardLeaderPanel.setBorder(new TitledBorder("Select 2 Leader Cards to discard"));

        if(leaderList.size() != 4)
            throw new IllegalArgumentException("You should have 4 leader card in yout hand, in this moment are "+ leaderList.size());
        CardCheckbox leaderCheckbox1 = new CardCheckbox(leaderList.subList(0,2), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox1);
        CardCheckbox leaderCheckbox2 = new CardCheckbox(leaderList.subList(2,4), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox2);

        this.add(discardLeaderPanel);

        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
        DnDDepot dnDDepot = new DnDDepot();
        dnDDepot.initFromInfiniteDrag(new CheckDropAtBeginningDecisionsTime(dnDDepot.getDepot()));
        rightSidePanel.add(dnDDepot);
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setLabelText("Select 2 Leader Cards to discard and place " + numRes + " resources, in the depot");

        CollectBeginningChoices collectBeginningChoices = new CollectBeginningChoices(dnDDepot.getDepot(), leaderCheckbox1, leaderCheckbox2, numLeaders);

        instructionPanel.setConfirmActionListener(collectBeginningChoices);
        rightSidePanel.add(instructionPanel);
        this.add(rightSidePanel);

        //this.setSize(panelWidth, 100);


    }


}
