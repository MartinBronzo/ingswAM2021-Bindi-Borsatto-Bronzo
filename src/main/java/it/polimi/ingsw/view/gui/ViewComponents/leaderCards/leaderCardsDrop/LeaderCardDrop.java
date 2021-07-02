package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.RegisterDropFromFiniteRes;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a player's ExtraSlot LeaderCard where resources can be dropped onto. N.B.: this class has methods used to
 * implement drops whose resources come from a shelf in the case the moving resources from the Depot to the ExtraSlot LeaderCards were made
 * with a drag and drop (this isn't the case for this application, but the drag and drop for this case works).
 */
public class LeaderCardDrop extends JPanel implements DropResettable, Droppable {
    private MyDropTargetListener targetListener;
    private List<JLabel> resources; //The already present resources
    private int shelf;
    private int maximumCapacity; //The maximum capacity of the LeaderCard
    private List<JLabel> droppedRes; //The resources so far dropped
    private boolean droppedHere; //It's needed in order to understand in which LeaderCard the resource was dropped (useful when there are two LeaderCards which can store the same kind of Resource)
    private ResourceType resStored;
    private int dropInfo;

    /**
     * Constructs a LeaderCardDrop panel which represents the specified LeaderCard and with the specified amount of LeaderCards already stored
     * @param leader the LeaderCard this panel represents
     * @param alreadyStoredRes the quantity of resources the player has stored onto the card
     */
    public LeaderCardDrop(LeaderCard leader, int alreadyStoredRes){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.resources = new ArrayList<>();
        shelf = -1;
        this.droppedRes = new ArrayList<>();
        this.maximumCapacity = leader.getEffect().extraSlotGetResourceNumber();
        this.droppedHere = false;
        this.dropInfo = 0;

        JLabel label = new JLabel();
        ImageIcon image = LeaderCardPanel.scaleImage(getClass().getResource(leader.getUrl()), 100, 150);
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
            resource = new JLabel(new ImageIcon(getClass().getResource(DepotDrop.getImagePathFromResource(resourceType))));
            this.add(resource);
            this.resources.add(resource);
        }
    }

    /**
     * Prepares this LeaderCardDrop to take drops from a source of limited draggable resources (a LimitedResourceDrag panel)
     * @param checker the DropChecker function to be used to checks the drop onto this LeaderCardDrop
     * @param registerDrop the RegisterDropInterface function used to register the drops made onto the collection of LeaderCardDrop this LeaderCardDrop belongs to
     */
    //Used for testing purposes
    public void init(DropChecker checker, RegisterDropInterface registerDrop){
        this.targetListener = new MyDropTargetListener(this, registerDrop, checker);
    }

    @Deprecated
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
        this.dropInfo = 0;
        this.targetListener.resetChecker();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.droppedRes.add(label);
    }

    /**
     * Returns from which shelf the dropped resources come from
     * @return the number of the shelf the dropped resources come from or -1
     */
    public int getShelf() {
        return shelf;
    }

    /**
     * Returns the amount of dropped resources onto this card
     * @return the amount of dropped resources onto this card
     */
    public int getQuantity(){
        return this.droppedRes.size();
    }

    /**
     * Returns how many resources can be stored onto this card
     * @return the amount of resources that can be dropped onto this card
     */
    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    /**
     * Returns how many resources this card held before any drop had been made
     * @return the resources stored before any drop had been made
     */
    public int getOriginalPresent(){
        return this.resources.size();
    }

    /**
     * Returns whether a resource has been dropped here
     * @return true if at least a resource was dropped here, false otherwise
     */
    public boolean isDroppedHere() {
        return droppedHere;
    }

    /**
     * Sets a boolean which takes track of whether at least a resource was dropped onto the card with the specified value
     * @param droppedHere a boolean
     */
    public void setDroppedHere(boolean droppedHere) {
        this.droppedHere = droppedHere;
    }

    /**
     * Returns the type of resources that can be stored onto this card
     * @return the type of resources that can be stored onto this card
     */
    public ResourceType getResStored() {
        return resStored;
    }

    @Override
    public void addDecision(Integer intInfo, ResourceType res) throws IllegalActionException{
        if(!res.equals(this.resStored))
            throw new IllegalActionException("You can only store " + this.resStored + "s here!");
        this.dropInfo++;
    }

    @Override
    public int getDropInfo() {
        return 0;
    }

    /**
     * Prepares this LeaderCardDrop to take drops from the specified source of limited draggable resources (the LimitedResourceDrag panel)
     * @param checkDropFunction the DropChecker function to be used to checks the drop onto this LeaderCardDrop and the collection of LeaderCardDrops this panel belongs to
     * @param resDrag a source of limited draggable resources
     */
    public void initFromFiniteRes(DropChecker checkDropFunction, LimitedResourcesDrag resDrag){
        this.targetListener = new MyDropTargetListener(this, new RegisterDropFromFiniteRes(this, resDrag));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    /**
     * Returns the number of resources dropped onto this card
     * @return the number of resources dropped onto this card
     */
    public int getDroppedHereAmount(){
        return this.dropInfo;
    }
}
