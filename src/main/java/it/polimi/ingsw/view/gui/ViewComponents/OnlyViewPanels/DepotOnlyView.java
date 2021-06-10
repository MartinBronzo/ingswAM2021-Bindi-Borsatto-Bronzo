package it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels;

import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.ShelfOnlyView;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.List;

public class DepotOnlyView extends JPanel {
    private List<ShelfOnlyView> shelves;
    private String nickname;

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
