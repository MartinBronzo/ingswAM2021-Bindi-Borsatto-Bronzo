package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.LeaderCardOnlyView;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MoveShelfToLeader extends JPanel {
    private List<DepotShelf> shelves;

    public MoveShelfToLeader(){
        super();
        this.setBorder(new TitledBorder("Move from a Depot Shelf to a LeaderSlot"));
        this.setLayout(new BorderLayout());

        DepotOnlyView depot = new DepotOnlyView(PanelManager.getInstance().getNickname());
        this.add(depot, BorderLayout.LINE_START);


        List<LeaderCard> activeLeader = PanelManager.getInstance().getExtraSlotActiveLeaderCards();
        this.shelves = PanelManager.getInstance().getDepotShelves();

        //TODO: questo controllo non dovrebbe essere fatto prima di chiamare il pannello
        if(!activeLeader.isEmpty()) {
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));
            for (LeaderCard leader : activeLeader) {

                JPanel card = new LeaderCardOnlyView(leader, PanelManager.getInstance().getAlreadyStoredInLeaderSlot(leader.getEffect().extraSlotGetType()));

                cardPanel.add(card);
                cardPanel.setAlignmentX(LEFT_ALIGNMENT);
            }
            this.add(cardPanel, BorderLayout.CENTER);
        }

        //Creating the drop-down menu for the shelf choice (the one for the quantity will be displayed when the user chooses a shelf)
        String [] shelfChoice = this.createShelfChoice();
        JComboBox<String> shelf = new JComboBox<>(shelfChoice);

        JPanel container = new JPanel();
        InstructionPanel instructionPanel = new InstructionPanel(true);
        instructionPanel.setLabelText("Choose the shelf where you want to move your resources from\nand how many resources you want to move");
        CollectMoveShelfToLeader collector = new CollectMoveShelfToLeader(shelf);
        instructionPanel.setConfirmActionListener(collector);
        instructionPanel.setCancelActionListener(event -> PanelManager.getInstance().showPlayerBoard((JPanel) this.getParent().getParent()));
        container.add(instructionPanel);

        //Creating the submit button
        /*SubmitButton submit = new SubmitButton("confirm");
        CollectMoveShelfToLeader collector = new CollectMoveShelfToLeader(shelf);
        submit.addActionListener(collector);*/

        //Creating a panel for the drop-down menus
        JPanel menus = new JPanel();
        //menus.setBorder(new TitledBorder("Choose the shelf where you want to move your resources from and how many resources you want to move"));
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        menus.add(shelf);

        //Creating the listener for the ShelfChoice menu
        HashMap<Integer, Integer> numResToShelf = new HashMap<>();
        for(int i = 0; i < this.shelves.size(); i++)
            numResToShelf.put(i, this.shelves.get(i).getQuantity());
        shelf.addActionListener(new ShelfChoiceListener(menus, numResToShelf, collector));
        shelf.setPrototypeDisplayValue("xxxxxxxxxxxxxxx");

        //Creating a panel for the submit button & the menus
        JPanel selectables = new JPanel();
        selectables.setLayout(new BoxLayout(selectables, BoxLayout.Y_AXIS));
        selectables.add(menus);

        JPanel comboPanel = new JPanel();
        comboPanel.add(menus);

        selectables.add(comboPanel);
        selectables.add(Box.createRigidArea(new Dimension(0,10)));
        selectables.add(container);

        //selectables.add(submit);
        this.add(selectables, BorderLayout.LINE_END);

    }

    private String[] createShelfChoice(){
        int counter = 0;

        for(DepotShelf dS: this.shelves)
            if(dS.getQuantity() != 0)
                counter++;

        String [] result = new String[counter];

        int i = 0;
        int shelfNum;
        for(DepotShelf dS: this.shelves)
            if(dS.getQuantity() != 0){
                shelfNum = this.shelves.indexOf(dS) + 1;
                result[i] = "Shelf " + shelfNum;
                i++;
            }

        return result;
    }

    //TODO: controllare che funzioni


}
