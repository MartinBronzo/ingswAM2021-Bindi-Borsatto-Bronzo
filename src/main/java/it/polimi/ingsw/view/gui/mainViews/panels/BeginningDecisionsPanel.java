package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.CardCheckbox;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.CheckDropAtBeginningDecisionsTime;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.CollectBeginningChoices;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DnDDepot;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.utils.ResetState;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;

/**
 * This panel enables the player to make their begging of the game decisions, that is, discarding LeaderCards and, eventually,
 * to get extra resources.
 */
public class BeginningDecisionsPanel extends JPanel {

    /**
     * Constructs a BeginningDecisionsPanel
     * @param leaderList the absolute path to the LeaderCards the player has received in the game and that now they are supposed to discard
     * @param numRes the number of extra resources the player can get
     * @param numLeaders the number of LeaderCards the player is supposed to discard
     */
    public BeginningDecisionsPanel(ArrayList<String> leaderList, int numRes, int numLeaders){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel discardLeaderPanel = new JPanel();
        discardLeaderPanel.setLayout(new BoxLayout(discardLeaderPanel, BoxLayout.PAGE_AXIS));
        discardLeaderPanel.setBorder(new TitledBorder("Select " + numLeaders + " Leader Cards to discard"));

        if(leaderList.size() != 4)
            throw new IllegalArgumentException("You should have 4 leader card in your hand, in this moment are "+ leaderList.size());
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
        InstructionPanel instructionPanel = new InstructionPanel(true);
        instructionPanel.setLabelText("Select " + numLeaders + " Leader Cards to discard and place " + numRes + " resources, in the depot");

        CollectBeginningChoices collectBeginningChoices = new CollectBeginningChoices(dnDDepot.getDepot(), leaderCheckbox1, leaderCheckbox2, numLeaders);
        ResetState resetFunction = new ResetState(dnDDepot);

        instructionPanel.setConfirmActionListener(collectBeginningChoices);
        instructionPanel.setCancelActionListener(resetFunction);

        rightSidePanel.add(instructionPanel);
        this.add(rightSidePanel);

        //this.setSize(panelWidth, 100);


    }


}
