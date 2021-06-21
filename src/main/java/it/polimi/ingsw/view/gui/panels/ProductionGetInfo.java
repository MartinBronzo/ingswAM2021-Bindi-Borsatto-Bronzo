package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.ViewComponents.CardCheckbox;
import it.polimi.ingsw.view.gui.ViewComponents.DevSlotCheckBox;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD.CheckBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD.DragAndDropBaseProd;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.swing.*;

public class ProductionGetInfo extends JPanel {
    private Player player;
    private int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private InstructionPanel instructionPanel;
    private CardCheckbox cardCheckboxPanel;
    private DragAndDropBaseProd dragAndDropBaseProd;
    private DevSlotCheckBox devSlotcheckBox;
    private JPanel bottomPanel;



    public ProductionGetInfo() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setSize(panelWidth, panelHeight-100);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel upperPanel = new JPanel(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.dragAndDropBaseProd = new DragAndDropBaseProd();
        dragAndDropBaseProd.setCheckDropFunction(new CheckBaseProd(dragAndDropBaseProd));
        upperPanel.add(dragAndDropBaseProd);
        upperPanel.add(Box.createHorizontalGlue());
        instructionPanel = new InstructionPanel(true);
        instructionPanel.setConfirmActionListener(e -> {
            //TODO idk if there is the need to print something else
        });
        instructionPanel.setCancelActionListener(e -> {
            //TODO: idk if there is the need to print something else
        });
        instructionPanel.setLabelText("Press submit to get the final CardCost after selecting the leader Cards");
        upperPanel.add(instructionPanel);

        this.add(upperPanel);
        this.add(Box.createVerticalGlue());
        this.bottomPanel =  new JPanel(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(bottomPanel);
    }

    public void SetPlayer(Player player){}
}
