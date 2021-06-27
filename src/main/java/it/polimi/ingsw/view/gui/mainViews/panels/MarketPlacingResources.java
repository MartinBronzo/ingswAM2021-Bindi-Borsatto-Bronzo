package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.CheckDropInDepot;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.CollectPlacingResources;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop.CheckDropInLeader;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop.DropLeaderCards;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.DiscardedResDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.utils.DumbCheckDrop;
import it.polimi.ingsw.view.gui.ViewComponents.utils.ResetState;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * This panel lets the player specify where they want to put the resources they have gotten from the market. The player can either store
 * the resources in their Depot or discard them.
 */
public class MarketPlacingResources extends JPanel {
    private boolean created;
    //TODO: a cosa servono questi metodi?
    public MarketPlacingResources(boolean created){
        this.created = created;
    }

    public boolean isCreated(){return created;}

    /**
     * Constructs a MarketPlacingResources panel
     * @param resourcesToBePlaced the resources the player got from the market and that now they have to place
     * @param row the row of the market they want to buy from or 0
     * @param col the column of the market they want to buy from or 0
     * @param leaderList the list of indexes of WhiteMarble LeaderCards whose effects the player wants to use in order to buy resources from the market
     */
    public MarketPlacingResources(HashMap<ResourceType, Integer> resourcesToBePlaced, int row, int col, List<Integer> leaderList){
        super();
        this.setLayout(new BorderLayout());
        this.created = true;

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        limitedResourcesDrag.init(resourcesToBePlaced);
        this.add(limitedResourcesDrag, BorderLayout.PAGE_START);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);

        //Setting up the LeaderCards
        DropLeaderCards leaders = new DropLeaderCards();
        if(!leaders.isEmpty())
            leaders.initFromFiniteRes(new CheckDropInLeader(), limitedResourcesDrag);

        //Setting up the DiscardedDrop
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);

        //Uniting the Depot, LeaderDrop and the Discarded in a JPanel
        JLabel centralPanel = new JLabel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
        centralPanel.add(depot);
        centralPanel.add(trashCan);
        if(!leaders.isEmpty())
            centralPanel.add(leaders);
        this.add(centralPanel, BorderLayout.CENTER);

        //Adding the submit button
        SubmitButton submit = new SubmitButton("Submit");
        submit.addActionListener(new CollectPlacingResources(depot, trashCan, leaders.getCards(), row, col, leaderList));

        //Adding the Cancel button
        CancelButton cancel = new CancelButton("Reset");
        if(!leaders.isEmpty())
            cancel.addActionListener(new ResetState(depot, leaders, trashCan, limitedResourcesDrag));
        else
            cancel.addActionListener(new ResetState(depot, trashCan, limitedResourcesDrag));

        //Adding Back button
        BackButton back = new BackButton("Back");
        back.addActionListener(event -> PanelManager.getInstance().showPlayerBoard(this));

        //Uniting the buttons altogether
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.add(submit);
        buttons.add(Box.createRigidArea(new Dimension(0,10)));
        buttons.add(cancel);
        buttons.add(Box.createRigidArea(new Dimension(0,10)));
        buttons.add(back);
        this.add(buttons, BorderLayout.LINE_END);

        //Finishing it up
        String border = "Place the resource onto the Depot, ";
        if(!leaders.isEmpty()) {
            border = border +  "LeaderCards, and/or StrongBox";
        } else {
            border = border + " and/or StrongBox";
        }
        this.setBorder(new TitledBorder(border));
    }


}
