package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews.DepotOnlyView;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

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

        DepotOnlyView depot = new DepotOnlyView(PanelManager.getInstance().getNickname());
        this.add(depot, BorderLayout.CENTER);

        List<DepotShelf> shelves = PanelManager.getInstance().getDepotShelves();

        //Creating the drop-down menu for the start shelf
        String [] sourceShelf = this.createSourceShelfChoice(shelves);
        JComboBox<String> source = new JComboBox<>(sourceShelf);

        //Creating the submit button
        JPanel container = new JPanel();
        InstructionPanel instructionPanel = new InstructionPanel(true);
        instructionPanel.setLabelText("Choose the shelf where you want to move your resources from\nand how many resources you want to move");
        CollectMoveBetweenShelves collector = new CollectMoveBetweenShelves(source);
        instructionPanel.setConfirmActionListener(collector);
        instructionPanel.setCancelActionListener(event -> PanelManager.getInstance().showPlayerBoard((JPanel) this.getParent().getParent()));
        container.add(instructionPanel);

        /*SubmitButton submit = new SubmitButton("Confirm");
        CollectMoveBetweenShelves collector = new CollectMoveBetweenShelves(source);
        submit.addActionListener(collector);
        BackButton back = new BackButton("Back");
        back.addActionListener(event -> PanelManager.getInstance().showPlayerBoard((JPanel) this.getParent().getParent()));
*/

        //Creating a panel for the drop-down menus
        JPanel menus = new JPanel();
        //menus.setBorder(new TitledBorder("Choose the shelf where you want to move your resources from and how many resources you want to move"));
        menus.setLayout(new BoxLayout(menus, BoxLayout.X_AXIS));
        menus.add(source);

        //Creating the listener for the SourceShelf menu
        source.addActionListener(new SourceShelfListener(shelves.size(), collector, menus));
        source.setPrototypeDisplayValue("xxxxxxxxxxxxxxx");

        //Creating a panel for the submit button & the menus
        JPanel selectables = new JPanel();
        selectables.setLayout(new BoxLayout(selectables, BoxLayout.Y_AXIS));
        JPanel comboPanel = new JPanel();
        comboPanel.add(menus);

        selectables.add(comboPanel);
        selectables.add(Box.createRigidArea(new Dimension(0,10)));
        selectables.add(container);
        //selectables.add(submit);
        //selectables.add(Box.createRigidArea(new Dimension(5, 0)));
        //selectables.add(back);
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
