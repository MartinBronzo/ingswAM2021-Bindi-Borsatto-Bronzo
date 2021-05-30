package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class MyDepotPanel extends JPanel {
    //TODO: ci sarà da stampare le risorse già presenti nel depot
    private int shelfNumber;
    /**
     * In each depot panel we have this resToDepot list which takes track of all the resources moved to this depot. When  the confirm
     * button is clicked then all the list from all the three depot panels must be collected and sent to the model somehow
     */
    //TODO: on submit click fondere le varie liste in depot params
    private List<Pair<Integer, ResourceType>> resToDepot;

    class Pair<integer, resourceType> {
        private integer shelf;
        private resourceType res;

        public Pair(integer shelf, resourceType res) {
            this.shelf = shelf;
            this.res = res;
        }

        public integer getKey() {
            return shelf;
        }

        public resourceType getValue() {
            return res;
        }

        public void setKey(integer shelf) {
            this.shelf = shelf;
        }

        public void setValue(resourceType res) {
            this.res = res;
        }
    }

    public MyDepotPanel(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        resToDepot = new ArrayList<>();

        this.setBorder(new TitledBorder("Drag Image onto shelf number " + this.shelfNumber));
        MyDepotPanel theDepotPanel = this;
        TransferHandler dnd = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {
                    return false;
                }
                //only Strings
                if (!support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable tansferable = support.getTransferable();
                Icon ico;
                try {
                    ico = (Icon) tansferable.getTransferData(DataFlavor.imageFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                theDepotPanel.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    public void addDecision(Integer shelf, ResourceType res){
        Pair<Integer, ResourceType> decision = new Pair<>(shelf, res);
        this.resToDepot.add(decision);
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }

    private String getDepotFileName(int shelf){
        switch (shelf){
            case 1:
                return "src/main/resources/shelf 1.png";
            case 2:
                return "src/main/resources/shelf 2.png";
            case 3:
                return "src/main/resources/shelf 3.png";
        }
        return "";
    }

    public List<Pair<Integer, ResourceType>> getResToDepot() {
        return resToDepot;
    }

    public void resetState(){
        this.resToDepot = new ArrayList<>();
    }
}

