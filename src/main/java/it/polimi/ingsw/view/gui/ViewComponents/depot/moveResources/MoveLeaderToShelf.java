package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.LeaderCardOnlyView;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel lets the user specify how they want to move resources from an ExtraSlot LeaderCard to a Depot shelf.
 */
public class MoveLeaderToShelf extends JPanel {
    private List<LeaderCard> activeLeader;

    /**
     * Constructs a MoveLeaderToShelf panel showing the player's Depot and ExtraSlot LeaderCard eventually filled with resources
     */
    public MoveLeaderToShelf(){
        super();
        this.setBorder(new TitledBorder("Move from a LeaderSlot to a Depot Shelf"));
        this.setLayout(new BorderLayout());

        DepotOnlyView depot = new DepotOnlyView(PanelManager.getInstance().getNickname());
        this.add(depot, BorderLayout.LINE_START);

        this.activeLeader = PanelManager.getInstance().getExtraSlotActiveLeaderCards();
        List<DepotShelf> shelves = PanelManager.getInstance().getDepotShelves();

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
        //TODO: DECIDERE SE METTERLO
        /*else{
            JLabel errorMessage = new JLabel("You don't have any Extra Slot Leader Card activated");
            this.add(errorMessage);
        }*/

        //Creating the drop-down menu for the res choice
        String [] resChoice = this.createResChoice(PanelManager.getInstance().getLeaderSlots());
        JComboBox<String> res = new JComboBox<>(resChoice);

        JPanel container = new JPanel();
        InstructionPanel instructionPanel = new InstructionPanel(true);
        instructionPanel.setLabelText("Choose the shelf where you want to move your resources from\nand how many resources you want to move");
        CollectMoveLeaderToShelf collector = new CollectMoveLeaderToShelf(res);
        instructionPanel.setConfirmActionListener(collector);
        instructionPanel.setCancelActionListener(event -> PanelManager.getInstance().showPlayerBoard((JPanel) this.getParent().getParent()));
        container.add(instructionPanel);

        /*//Creating the submit button
        SubmitButton submit = new SubmitButton("confirm");
        CollectMoveLeaderToShelf collector = new CollectMoveLeaderToShelf(res);
        submit.addActionListener(collector);*/

        //Creating a panel for the drop-down menus
        JPanel menus = new JPanel();
        //menus.setBorder(new TitledBorder("Choose the shelf where you want to move your resources from and how many resources you want to move"));
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        menus.add(res);

        //Creating the listener for the ShelfChoice menu
        res.addActionListener(new ResChoiceListener(PanelManager.getInstance().getLeaderSlots(), collector, menus, this.getAddableShelf(shelves)));
        res.setPrototypeDisplayValue("xxxxxxxxxxxxxxx");

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

    private List<Integer> getAddableShelf(List<DepotShelf> shelves) {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < shelves.size(); i++)
            if(shelves.get(i).getQuantity() < i + 1)
                result.add(i + 1);
        return result;
    }

    private String[] createResChoice(HashMap<ResourceType, Integer> leaderSlots) {
        List<ResourceType> present = this.activeLeader.stream().filter(x -> {
            if (leaderSlots.get(x.getEffect().extraSlotGetType()) == null)
                return false;
            else return leaderSlots.get(x.getEffect().extraSlotGetType()) > 0;
            })
                .map(x -> x.getEffect().extraSlotGetType()).distinct().collect(Collectors.toList());
        String[] result = new String[present.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = present.get(i).toString();

        return result;
    }

}
