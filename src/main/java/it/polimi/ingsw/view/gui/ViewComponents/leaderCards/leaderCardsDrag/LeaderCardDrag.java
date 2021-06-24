package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.List;

public class LeaderCardDrag extends JPanel implements DragUpdatable, Resettable {
    private MyDragGestureListener dlistener;
    private List<JLabel> resources; //The resources already present
    private ResourceType resStored;

    public LeaderCardDrag(LeaderCard leader, int alreadyStoredRes){
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.resources = new ArrayList<>();

        JLabel label = new JLabel();
        ImageIcon image = LeaderCardPanel.scaleImage(leader.getUrl(), 100, 150);
        //ImageIcon image = new ImageIcon(leader.getUrl());
        label.setIcon(image);
        label.setAlignmentX(LEFT_ALIGNMENT);

        this.add(label);
        this.setAlignmentX(LEFT_ALIGNMENT);

        this.dlistener = new MyDragGestureListener();
        this.resStored = leader.getEffect().extraSlotGetType();
        this.fillLeaderCard(alreadyStoredRes);

    }

    private void fillLeaderCard(int alreadyStoredRes) {
        ImageIcon resource;
        JLabel label;
        DragSource ds;
        for(int i = 0; i < alreadyStoredRes; i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(this.resStored));
            resource.setDescription("leaderCard " + this.resStored);
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            //label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }
    }

    @Override
    public void updateAfterDrop(String info) {
        JLabel draggedAway = this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get();
        draggedAway.setVisible(false);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void resetState() {
        for(JLabel label : this.resources)
            label.setVisible(true);
    }

    public ResourceType getResStored() {
        return resStored;
    }
}
