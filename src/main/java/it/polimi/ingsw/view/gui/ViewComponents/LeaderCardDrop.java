package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderCardDrop extends JPanel implements DropResettable {
    private MyDropTargetListener targetListener;
    private List<JLabel> resources; //The already present resources
    private int shelf;
    private int maximumCapacity; //The maximum capacity of the LeaderCard
    private List<JLabel> droppedRes; //The resources so far dropped
    private boolean droppedHere; //It's needed in order to understand in which LeaderCard the resource was dropped (useful when there are two LeaderCards which can store the same kind of Resource)
    private ResourceType resStored;

    public LeaderCardDrop(LeaderCard leader, int alreadyStoredRes){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.resources = new ArrayList<>();
        shelf = -1;
        this.droppedRes = new ArrayList<>();
        this.maximumCapacity = leader.getEffect().extraSlotGetResourceNumber();
        this.droppedHere = false;

        JLabel label = new JLabel();
        ImageIcon image = LeaderCardPanel.scaleImage(leader.getUrl(), 100, 150);
        //ImageIcon image = new ImageIcon(leader.getUrl());
        label.setIcon(image);
        label.setAlignmentX(LEFT_ALIGNMENT);

        this.add(label);
        this.setAlignmentX(LEFT_ALIGNMENT);

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
                LeaderCardDrop.this.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);
        this.targetListener = null;

        this.resStored = leader.getEffect().extraSlotGetType();
        this.fillLeaderCard(resStored, alreadyStoredRes);
    }

    private void fillLeaderCard(ResourceType resourceType, int alreadyPresent){
        JLabel resource;
        for(int i = 0; i < alreadyPresent; i++){
            resource = new JLabel(new ImageIcon(DepotDrop.getImagePathFromResource(resourceType)));
            this.add(resource);
            this.resources.add(resource);
        }
    }

    public void init(DropChecker checker, RegisterDropInterface registerDrop){
        this.targetListener = new MyDropTargetListener(this, registerDrop, checker);
    }

    public void addDecision(String shelf){
        this.shelf = Integer.parseInt(shelf);
    }

    @Override
    public void resetState(){
        this.shelf = -1;
        for(JLabel label : this.droppedRes)
            this.remove(label);
        this.droppedRes = new ArrayList<>();
        this.droppedHere = false;
        this.targetListener.resetChecker();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.droppedRes.add(label);
    }

    public int getShelf() {
        return shelf;
    }

    public int getQuantity(){
        return this.droppedRes.size();
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getOriginalPresent(){
        return this.resources.size();
    }

    public boolean isDroppedHere() {
        return droppedHere;
    }

    public void setDroppedHere(boolean droppedHere) {
        this.droppedHere = droppedHere;
    }

    public ResourceType getResStored() {
        return resStored;
    }
}
