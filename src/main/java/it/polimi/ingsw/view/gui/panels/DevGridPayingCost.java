package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.*;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

public class DevGridPayingCost extends JPanel {

    public DevGridPayingCost(HashMap<ResourceType, Integer> resToBeTaken){
        super();
        this.setLayout(new BorderLayout());

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        strongBox.init();

        //Setting up the LeaderCards
        DragLeaderCards leaders = new DragLeaderCards();

        //Creating a panel that can hold the three DragSource
        JPanel dragSources = new JPanel();
        dragSources.setLayout(new BoxLayout(dragSources, BoxLayout.X_AXIS));
        dragSources.add(depotDrag);
        dragSources.add(strongBox);
        if(!leaders.isEmpty())
            dragSources.add(leaders);
        this.add(dragSources, BorderLayout.CENTER);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        pDrop.init(checker, depotDrag, strongBox, leaders);
        this.add(pDrop, BorderLayout.PAGE_END);

        //Creating the Submit button
        SubmitButton submit = new SubmitButton("submit");
        submit.addActionListener(new CollectCostsChoiceForDevCard(pDrop));
        //TODO: passare tutti i parametri corretti al collectcostschoice

        //Creating the cancel button
        CancelButton cancel = new CancelButton("cancel");
        cancel.addActionListener(new ResetState(depotDrag, strongBox, leaders, pDrop));

        //Creating a panel for the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.add(submit);
        buttons.add(cancel);
        this.add(buttons, BorderLayout.LINE_END);

        //Finishing it up
        String border = "Get the resources from the Depot, ";
        if(!leaders.isEmpty()) {
            border = border +  "LeaderCards, and/or StrongBox";
        } else {
            border = border + " and/or StrongBox";
        }
        this.setBorder(new TitledBorder(border));

        //TODO: fare drop down menu per scegliere i dev slot
        //TODO: aggiungere instruction panel
    }
}
