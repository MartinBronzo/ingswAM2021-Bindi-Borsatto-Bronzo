package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.CollectMoveLeaderToShelf;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MoveLeaderToShelf extends JPanel {
    private List<LeaderCard> activeLeader;

    public MoveLeaderToShelf(){
        super();
        this.setBorder(new TitledBorder("Move from a LeaderSlot to a Depot Shelf"));
        this.setLayout(new BorderLayout());

        DepotOnlyView depot = new DepotOnlyView();
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

        //Creating the drop-down menu for the res choice
        String [] resChoice = this.createResChoice(PanelManager.getInstance().getLeaderSlots());
        JComboBox<String> res = new JComboBox<>(resChoice);

        //Creating the submit button
        SubmitButton submit = new SubmitButton("confirm");
        CollectMoveLeaderToShelf collector = new CollectMoveLeaderToShelf(res);
        submit.addActionListener(collector);

        //Creating a panel for the drop-down menus
        JPanel menus = new JPanel();
        menus.setBorder(new TitledBorder("Choose the shelf where you want to move your resources from and how many resources you want to move"));
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        menus.add(res);

        //Creating the listener for the ShelfChoice menu
        res.addActionListener(new ResChoiceListener(PanelManager.getInstance().getLeaderSlots(), collector, menus, this.getAddableShelf(shelves)));

        //Creating a panel for the submit button & the menus
        JPanel selectables = new JPanel();
        selectables.setLayout(new BoxLayout(selectables, BoxLayout.X_AXIS));
        selectables.add(menus);
        selectables.add(submit);
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
