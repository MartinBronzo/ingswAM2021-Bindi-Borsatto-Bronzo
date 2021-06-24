package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;

public class InfiniteResourcesDrag extends JPanel {
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;

    public InfiniteResourcesDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from here to the Depot above"));

        ImageIcon image;

        //COIN
        image = new ImageIcon("src/main/resources/coins small.png");
        image.setDescription("InfiniteRes " + ResourceType.COIN);
        this.label1 = new JLabel(image);

        //SERVANT
        image = new ImageIcon("src/main/resources/servant small.png");
        image.setDescription("InfiniteRes " + ResourceType.SERVANT);
        this.label2 = new JLabel(image);

        //SHIELD
        image = new ImageIcon("src/main/resources/shield small.png");
        image.setDescription("InfiniteRes " + ResourceType.SHIELD);
        this.label3 = new JLabel(image);

        //STONE
        image = new ImageIcon("src/main/resources/stone small.png");
        image.setDescription("InfiniteRes " + ResourceType.STONE);
        this.label4 = new JLabel(image);

        MyDragGestureListener dlistener = new MyDragGestureListener();
        DragSource ds1 = new DragSource();
        ds1.createDefaultDragGestureRecognizer(label1, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds2 = new DragSource();
        ds2.createDefaultDragGestureRecognizer(label2, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds3 = new DragSource();
        ds3.createDefaultDragGestureRecognizer(label3, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds4 = new DragSource();
        ds4.createDefaultDragGestureRecognizer(label4, DnDConstants.ACTION_COPY, dlistener);

        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
    }
}
