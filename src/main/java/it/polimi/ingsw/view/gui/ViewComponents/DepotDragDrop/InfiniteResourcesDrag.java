package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

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

        this.label1 = new JLabel(new ImageIcon("src/main/resources/coins small.png"));
        this.label2 = new JLabel(new ImageIcon("src/main/resources/servant small.png"));
        this.label3 = new JLabel(new ImageIcon("src/main/resources/shield small.png"));
        this.label4 = new JLabel(new ImageIcon("src/main/resources/stone small.png"));

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
