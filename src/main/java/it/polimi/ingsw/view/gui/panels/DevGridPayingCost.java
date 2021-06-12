package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevSlots;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.*;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevGridPayingCost extends JPanel {

    public DevGridPayingCost(HashMap<ResourceType, Integer> resToBeTaken, int row, int col, List<Integer> leaderList){
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

        //Creating the drop-down menu for choosing the DevSlot
        //JComboBox devSlotMenu = new JComboBox(this.createMenuList(PanelManager.getInstance().getTopDevCardInDevSlot()));
        JComboBox devSlotMenu = new JComboBox(this.createMenuListString(PanelManager.getInstance().getTopDevCardInDevSlot()));


        //Creating the Submit button
        SubmitButton submit = new SubmitButton("submit");
        submit.addActionListener(new CollectCostsChoiceForDevCard(pDrop, row, col, leaderList, devSlotMenu));

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
        //InstructionPanel infoBox = new InstructionPanel(this.createDescription(resToBeTaken), submit, cancel, back);
        InstructionPanelFree infoBox = new InstructionPanelFree(this.createDescription(resToBeTaken), submit, cancel, back);

        //Adding all the elements to the panel

        //Creating the central panel which holds the strongbox, the drop-down menu, and the Info box
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.add(strongBox);
        centralPanel.add(devSlotMenu);
        centralPanel.add(infoBox);

        //Creating a panel that can hold the Depot and the Central Panel
        JPanel dragSources = new JPanel();
        dragSources.setLayout(new BoxLayout(dragSources, BoxLayout.X_AXIS));
        dragSources.add(depotDrag);
        dragSources.add(centralPanel);
        if(!leaders.isEmpty())
            dragSources.add(leaders);
        this.add(dragSources, BorderLayout.CENTER);

        //Creating a panel for the buttons
/*        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        //buttons.add(devSlotMenu);
        buttons.add(submit);
        buttons.add(cancel);
        this.add(buttons, BorderLayout.LINE_END);*/

        //Finishing it up
        String border = "Get the resources from the Depot, ";
        if(!leaders.isEmpty()) {
            border = border +  "LeaderCards, and/or StrongBox";
        } else {
            border = border + " and/or StrongBox";
        }
        this.setBorder(new TitledBorder(border));

    }

    private String createDescription(HashMap<ResourceType, Integer> resToBeTaken) {
        //Describing what resources must be picked
        String result = "<html>Drag the resources you want to use to buy the DevCard! You must pick the following resources:<br/>";

        for(Map.Entry<ResourceType, Integer> e : resToBeTaken.entrySet()){
            result = result + e.getKey() +  " " + e.getValue() + "<br/>";
        }

        //Describing that the player must select a DevSlot to put the card
        result += "<br/>Pick a DevSlot where you want to put the DevCard!";

        result = result + "</html>";

        return result;
    }

    private ImageIcon[] createMenuList(HashMap<Integer, DevCard> topCards){
        ImageIcon[] result = new ImageIcon[topCards.size()];
        ImageIcon image;

        int i = 0;
        for(Map.Entry<Integer, DevCard> dCSlot : topCards.entrySet()){
            if(dCSlot.getValue() == null)
                image = new ImageIcon("empty dev Slot.jpg");
            else
                image = new ImageIcon(dCSlot.getValue().getUrl());
            image.setDescription("DevSlot " + dCSlot.getKey());
            result[i] = image;
            i++;
        }

        //TODO: perché non stampa le immagini? Se riesco,

        return result;
    }

    private String[] createMenuListString(HashMap<Integer, DevCard> topCards){
        String [] result = new String[3];
        String slotDescription;
        int i = 0;
        int number;
        for(Map.Entry<Integer, DevCard> dCSlot : topCards.entrySet()){
            number = i + 1;
            if(dCSlot.getValue() == null)
                slotDescription = "DevSlot " + number + " (empty)";
            else
                slotDescription = "DevSlot " + number + " (Card color " + dCSlot.getValue().getColour() + ", level " + dCSlot.getValue().getLevel() + ")";
            result[i] = slotDescription;
            i++;
        }



        return result;

    }
}
