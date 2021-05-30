package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DepotDragAndDrop extends JPanel {
    private List<MyDepotPanel> depots;

    public DepotDragAndDrop(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.depots = new ArrayList<>();

        MyDepotPanel panel1 = new MyDepotPanel(1);
        new MyDropTargetListener(panel1, new RegisterDrop(panel1));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel1);
        depots.add(panel1);

        MyDepotPanel panel2 = new MyDepotPanel(2);
        new MyDropTargetListener(panel2, new RegisterDrop(panel2));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel2);
        depots.add(panel2);

        MyDepotPanel panel3 = new MyDepotPanel(3);
        new MyDropTargetListener(panel3, new RegisterDrop(panel3));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel3);
        depots.add(panel3);


        try {
            this.add(new ResourcesPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<MyDepotPanel.Pair<Integer, ResourceType>> getDecisions(){
        List<MyDepotPanel.Pair<Integer, ResourceType>> result = new ArrayList<>();
        for(MyDepotPanel e: this.depots)
            result.addAll(e.getResToDepot());
        return result;
    }

    public void resetState(){
        for(MyDepotPanel e: this.depots)
            e.resetState();
    }
}
