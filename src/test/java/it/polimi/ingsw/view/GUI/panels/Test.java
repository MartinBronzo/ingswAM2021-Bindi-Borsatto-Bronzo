package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.CollectBeginningChoices;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDragAndDrop;
import it.polimi.ingsw.view.gui.ViewComponents.SubmitButton;

import java.awt.*;
import javax.swing.*;

/**
 * THIS IS A TEST FOR THE BEGINNING DECISIONS DEPOT DRAG & DROP
 */
public class Test {

    public static void main(String[] args) {
        createAndShowJFrame();
    }

    public static void createAndShowJFrame() {
        SwingUtilities.invokeLater(() -> {

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DepotDragAndDrop depotPanels = new DepotDragAndDrop();
            frame.add(depotPanels, BorderLayout.CENTER);
            frame.setTitle("Depot test");
            frame.pack();
            JButton submit = new SubmitButton("Confirm");
            submit.addActionListener(new CollectBeginningChoices(depotPanels));
            frame.add(submit, BorderLayout.PAGE_END);
            frame.setVisible(true);
        });
    }
}
/*
package it.polimi.ingsw.view.GUI.panels;

        import it.polimi.ingsw.model.ResourceType;
        import it.polimi.ingsw.view.gui.ViewComponents.SubmitButton;

        import javax.swing.*;
        import javax.swing.border.TitledBorder;
        import java.awt.*;
        import java.awt.datatransfer.DataFlavor;
        import java.awt.datatransfer.Transferable;
        import java.awt.datatransfer.UnsupportedFlavorException;
        import java.awt.dnd.*;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) {
        createAndShowJFrame();
    }

    public static void createAndShowJFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                //JFrame frame = createJFrame();
                JFrame frame = new JFrame();
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel depotPanels = createDepotPanels();
                frame.add(depotPanels, BorderLayout.CENTER);
                frame.setTitle("Depot test");
                frame.pack();
                frame.add( new SubmitButton("Confirm"), BorderLayout.PAGE_END);
                frame.setVisible(true);
            }
        });
    }

    private static JPanel createDepotPanels(){
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));

        MyDepotPanel panel1 = createEmptyJPanel(1);
        new MyDropTargetListener(panel1);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel1);

        MyDepotPanel panel2 = createEmptyJPanel(2);
        new MyDropTargetListener(panel2);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel2);

        MyDepotPanel panel3 = createEmptyJPanel(3);
        new MyDropTargetListener(panel3);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel3);


        try {
            panelContainer.add(createResourcesPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return panelContainer;

    }

    private static JFrame createJFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setTitle("Test");

        MyDepotPanel panel = createEmptyJPanel(1);
        new MyDropTargetListener(panel);//this must be done or we wont be able to drop any image onto the empty panel
        MyDepotPanel panel2 = createEmptyJPanel(2);
        new MyDropTargetListener(panel2);//this must be done or we wont be able to drop any image onto the empty panel
        MyDepotPanel panel3 = createEmptyJPanel(3);
        new MyDropTargetListener(panel3);//this must be done or we wont be able to drop any image onto the empty panel

        frame.add(panel, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);

        try {
            frame.add(createResourcesPanel(), BorderLayout.SOUTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        frame.pack();

        return frame;
    }


    private static MyDepotPanel createEmptyJPanel(int shelf) {
        final MyDepotPanel p = new MyDepotPanel(shelf);

        p.setBorder(new TitledBorder("Drag Image onto this panel "+ shelf));

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
                p.add(new JLabel(ico));
                return true;
            }
        };

        p.setTransferHandler(dnd);

        return p;
    }

    private static JPanel createResourcesPanel() throws Exception {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Drag Resources from here to the Depot above"));

        JLabel label1 = new JLabel(new ImageIcon("src/main/resources/coins small.png"));
        JLabel label2 = new JLabel(new ImageIcon("src/main/resources/servant small.png"));
        JLabel label3 = new JLabel(new ImageIcon("src/main/resources/shield small.png"));
        JLabel label4 = new JLabel(new ImageIcon("src/main/resources/stone small.png"));

        MyDragGestureListener dlistener = new MyDragGestureListener();
        DragSource ds1 = new DragSource();
        ds1.createDefaultDragGestureRecognizer(label1, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds2 = new DragSource();
        ds2.createDefaultDragGestureRecognizer(label2, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds3 = new DragSource();
        ds3.createDefaultDragGestureRecognizer(label3, DnDConstants.ACTION_COPY, dlistener);
        DragSource ds4 = new DragSource();
        ds3.createDefaultDragGestureRecognizer(label4, DnDConstants.ACTION_COPY, dlistener);

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        return panel;
    }
}
*//*
class MyDropTargetListener extends DropTargetAdapter {

    private DropTarget dropTarget;
    private MyDepotPanel p;
    private Consumer<Icon> makeCall;

    public MyDropTargetListener(MyDepotPanel panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = new RegisterDrop(panel);
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            DropTarget test = (DropTarget) event.getSource();
            Component ca = (Component) test.getComponent();
            Point dropPoint = ca.getMousePosition();
            Transferable tr = event.getTransferable();

            if (event.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Icon ico = (Icon) tr.getTransferData(DataFlavor.imageFlavor);

                if (ico != null) {

                    p.add(new JLabel(ico));
                    p.revalidate();
                    p.repaint();
                    event.dropComplete(true);
                    this.makeCall.accept(ico);
                }
            } else {
                event.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }

    public void addActionListener(Consumer<Icon> function){
        this.makeCall = function;
    }
}
*//*
class MyDragGestureListener implements DragGestureListener {

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        JLabel label = (JLabel) event.getComponent();
        final Icon ico = label.getIcon();


        Transferable transferable = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                if (!isDataFlavorSupported(flavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return ico;
            }
        };
        event.startDrag(null, transferable);
    }
}
*/
/*
class MyDepotPanel extends JPanel{
    private int shelfNumber;
    /**
     * In each depot panel we have this resToDepot list which takes track of all the resources moved to this depot. When  the confirm
     * button is clicked then all the list from all the three depot panels must be collected and sent to the model somehow
     */
//TODO: on submit click fondere le varie liste in depot params
    /*private List<Pair<Integer, ResourceType>> resToDepot;

    class Pair<Integer, ResourceType> {
        private Integer shelf;
        private ResourceType res;

        public Pair(Integer shelf, ResourceType res) {
            this.shelf = shelf;
            this.res = res;
        }

        public Integer getKey() {
            return shelf;
        }

        public ResourceType getValue() {
            return res;
        }

        public void setKey(Integer shelf) {
            this.shelf = shelf;
        }

        public void setValue(ResourceType res) {
            this.res = res;
        }
    }

    public MyDepotPanel(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        resToDepot = new ArrayList<>();
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
}

class RegisterDrop implements Consumer<Icon>{
    private final MyDepotPanel panel;

    public RegisterDrop(MyDepotPanel panel){
        this.panel = panel;
    }
    @Override
    public void accept(Icon icon) {
        switch (icon.toString()){
            case "src/main/resources/coins small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.STONE);
                break;
        }
    }
}
*/

