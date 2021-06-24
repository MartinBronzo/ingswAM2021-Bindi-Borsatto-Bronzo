package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag.DepotDrag;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag.DragLeaderCards;
import it.polimi.ingsw.view.gui.ViewComponents.production.CollectCostsChoiceForProduction;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.CheckLimitedDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.PanelDrop;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxDrag;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.utils.ResetState;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionGetResources extends JPanel {

    public ProductionGetResources(HashMap<ResourceType, Integer> resToBeTaken, List<Integer> prodLeaders, List<Integer> devCards, BaseProductionParams baseProduction){
        super();
        this.setLayout(new BorderLayout());

        //Creating all the elements

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        strongBox.init();

        //Setting up the LeaderCards
        DragLeaderCards leaders = new DragLeaderCards();

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        pDrop.init(checker, depotDrag, strongBox, leaders);
        this.add(pDrop, BorderLayout.PAGE_END);

        //Creating the drop-down menu for picking the extra prod info
        JPanel comboPanel = null;
        HashMap<Integer, JComboBox> leaderOutMenuList = new HashMap<>();
        if(prodLeaders != null && !prodLeaders.isEmpty()) {
            leaderOutMenuList = this.createAllMenus(prodLeaders);
            comboPanel = new JPanel();
            comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
            comboPanel.setBorder(new TitledBorder("Choose the extra output for the LeaderCards you picked!"));
            for(Map.Entry<Integer, JComboBox> e : leaderOutMenuList.entrySet())
                comboPanel.add(e.getValue());
        }

        //Creating the Submit button
        SubmitButton submit = new SubmitButton("submit");
        submit.addActionListener(new CollectCostsChoiceForProduction(pDrop, devCards, leaderOutMenuList, baseProduction));

        //Creating the cancel button
        CancelButton cancel = new CancelButton("cancel");
        if(!leaders.isEmpty())
            cancel.addActionListener(new ResetState(depotDrag, strongBox, leaders, pDrop));
        else
            cancel.addActionListener(new ResetState(depotDrag, strongBox, pDrop));

        //Creating the Back button
        BackButton back = new BackButton("Back");
        back.addActionListener(event -> PanelManager.getInstance().showPlayerBoard(this));

        //Creating the InfoBox
        //InstructionPanelFree infoBox = new InstructionPanelFree(this.createDescription(resToBeTaken), submit, cancel);
        InstructionPanel infoBox = new InstructionPanel(this.createDescription(resToBeTaken), submit, cancel, back);

        //Adding all the elements to the panel

        //Creating the central panel which holds the strongbox, the drop-down menus, and the Info box
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.add(strongBox);
        if(!prodLeaders.isEmpty())
            centralPanel.add(comboPanel);
        centralPanel.add(infoBox);

        //Creating a panel that can hold the Depot and the Central Panel
        JPanel dragSources = new JPanel();
        dragSources.setLayout(new BoxLayout(dragSources, BoxLayout.X_AXIS));
        dragSources.add(depotDrag);
        dragSources.add(centralPanel);
        if(!leaders.isEmpty())
            dragSources.add(leaders);
        this.add(dragSources, BorderLayout.CENTER);


        //Finishing it up
        String border = "Get the resources from the Depot, ";
        if(!leaders.isEmpty()) {
            border = border +  "LeaderCards, and/or StrongBox";
        } else {
            border = border + " and/or StrongBox";
        }
        this.setBorder(new TitledBorder(border));

    }

    private HashMap<Integer, JComboBox> createAllMenus(List<Integer> prodLeaders) {
        HashMap<Integer, JComboBox> result = new HashMap<>();

        String[] resources = new String[]{ResourceType.COIN.toString(), ResourceType.STONE.toString(), ResourceType.SERVANT.toString(), ResourceType.SHIELD.toString()};

        for(Integer i : prodLeaders)
            result.put(i, new JComboBox(resources));

        return result;

    }

    private String createDescription(HashMap<ResourceType, Integer> resToBeTaken) {
        //Describing what resources must be picked
        String result = "<html>Drag the resources you want to use as input for your Production! You must pick the following resources:<br/>";

        for(Map.Entry<ResourceType, Integer> e : resToBeTaken.entrySet()){
            result = result + e.getKey() +  " " + e.getValue() + "<br/>";
        }

        //Describing that the player must select a DevSlot to put the card
        result += "<br/>For each LeaderCard you picked, choose a Resource you want to have as your extra output!";

        result = result + "</html>";

        return result;
    }

    //TODO: per le carte che leader fare combo box per scegliere il resource type, mappare il risultato del combo box con l'indice della carta
}
