package it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews;

import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a Depot that is used only to be seen by the user (the resources are not draggable and no drop can occur onto the panel).
 */
public class DepotOnlyView extends JPanel {
    private List<ShelfOnlyView> shelves;
    private String nickname;

    /**
     * Constructs a view of the player's Depot containing the player's stored resources
     * @param nickname the nickname of a player
     */
    public DepotOnlyView(String nickname) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //this.setBorder(new TitledBorder("Your Depot"));
        //this.setAlignmentX(LEFT_ALIGNMENT);

        this.nickname = nickname;
        this.shelves = new ArrayList<>();

        ShelfOnlyView shelf;

        shelf = new ShelfOnlyView(1);
        shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfOnlyView(2);
        shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfOnlyView(3);
        shelves.add(shelf);
        this.add(shelf);

        fillDepot();
    }

    private void fillDepot() {
        List<DepotShelf> depotShelves = PanelManager.getInstance().getDepotShelves(nickname);
        int i = 0;
        for (ShelfOnlyView sView : this.shelves) {
            sView.filShelf(depotShelves.get(i));
            i++;
        }
    }

}
