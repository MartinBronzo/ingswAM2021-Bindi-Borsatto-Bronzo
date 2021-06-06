package it.polimi.ingsw.view.gui.ViewComponents.MoveResourcesElements;

import it.polimi.ingsw.view.gui.ViewComponents.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveBetweenShelves extends JPanel {

    public MoveBetweenShelves(){
        super();

        this.setBorder(new TitledBorder("Move from a Shelf to another Shelf"));
        this.setLayout(new BorderLayout());

        DepotOnlyView depot = new DepotOnlyView();
        this.add(depot, BorderLayout.CENTER);

        List<DepotShelf> shelves = PanelManager.getInstance().getDepotShelves();

        //Creating the drop-down menu for the start shelf
        String [] sourceShelf = this.createSourceShelfChoice(shelves);
        JComboBox<String> source = new JComboBox<>(sourceShelf);

        //Creating the submit button
        SubmitButton submit = new SubmitButton("confirm");
        CollectMoveBetweenShelves collector = new CollectMoveBetweenShelves(source);
        submit.addActionListener(collector);

        //Creating a panel for the drop-down menus
        JPanel menus = new JPanel();
        menus.setBorder(new TitledBorder("Choose the shelf where you want to move your resources from and how many resources you want to move"));
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        menus.add(source);

        //Creating the listener for the SourceShelf menu
        source.addActionListener(new SourceShelfListener(shelves.size(), collector, menus));

        //Creating a panel for the submit button & the menus
        JPanel selectables = new JPanel();
        selectables.setLayout(new BoxLayout(selectables, BoxLayout.X_AXIS));
        selectables.add(menus);
        selectables.add(submit);
        this.add(selectables, BorderLayout.LINE_END);
    }

    private String[] createSourceShelfChoice(List<DepotShelf> shelves) {
        List<Integer> notEmpty = new ArrayList<>();
        for(int i = 0; i < shelves.size(); i++)
            if(shelves.get(i).getQuantity() != 0)
                notEmpty.add(i + 1);

        String[] result = new String[notEmpty.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = "Shelf " + notEmpty.get(i);

        return result;
    }
}
