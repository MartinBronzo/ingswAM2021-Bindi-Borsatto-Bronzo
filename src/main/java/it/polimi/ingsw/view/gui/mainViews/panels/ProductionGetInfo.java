package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.CardCheckbox;
import it.polimi.ingsw.view.gui.ViewComponents.devCards.DevSlotCheckBox;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.production.baseProd.CheckBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.production.baseProd.DragAndDropBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionGetInfo extends JPanel {
    private Player player;
    private int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private InstructionPanel instructionPanel;
    private CardCheckbox cardCheckboxPanel;
    private DragAndDropBaseProd dragAndDropBaseProd;
    private DevSlotCheckBox devSlotcheckBox;

    public ProductionGetInfo(Player player){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setSize(panelWidth, panelHeight-100);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));

        this.dragAndDropBaseProd = new DragAndDropBaseProd();
        dragAndDropBaseProd.setCheckDropFunction(new CheckBaseProd(dragAndDropBaseProd));
        upperPanel.add(dragAndDropBaseProd);
        upperPanel.add(Box.createHorizontalGlue());

        //TODO: add info text and JButtons functions
        String info = "Select the desired dev and leader cards, move resources to activate baseProd and then press submit";
        JButton back = new BackButton();
        back.setText("Back");
        back.addActionListener(e -> {
            PanelManager.getInstance().showPlayerBoard(this);
        });
        JButton reset = new CancelButton();
        reset.setText("Reset");
        reset.addActionListener(e -> {
            dragAndDropBaseProd.resetState();
            //TODO: ci sono altre cose da resettare?
        });

        JButton submit = new SubmitButton();
        submit.setText("submit");
        submit.addActionListener(e -> {
            dragAndDropBaseProd.getInputs();
            boolean activated = dragAndDropBaseProd.isActivated();
            if(!activated) {
                List<ResourceType> inputs = dragAndDropBaseProd.getInputs();

                for (ResourceType res : inputs) {
                    if (res != null) {
                        PanelManager.getInstance().printError("Specify all the resources for the input or reset the choice");
                        return;
                    }
                }
                if (dragAndDropBaseProd.getOutput() != null) {
                    PanelManager.getInstance().printError("Specify the resource for setting the choice");
                    return;
                }
            }
            BaseProductionParams baseProductionParams = new BaseProductionParams(activated, dragAndDropBaseProd.getInputs(), dragAndDropBaseProd.getOutputsList());
            //this.setVisible(false);
            PanelManager.getInstance().manageProductionInfos(devSlotcheckBox.getSelectedDevSlotIndexes(), cardCheckboxPanel.getSelectedLeaderIndexes(), baseProductionParams);
        });

        instructionPanel = new InstructionPanel(info, submit, reset, back);
        instructionPanel.setMinimumSize(new Dimension(50, 50));
        upperPanel.add(instructionPanel);
        this.add(upperPanel);
        this.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

        List<String> leaderPaths = player.getUsedLeaders().stream().map(card -> card.getUrl()).collect(Collectors.toList());
        cardCheckboxPanel = new CardCheckbox(leaderPaths, "select desired leader cards");
        bottomPanel.add(cardCheckboxPanel);

        devSlotcheckBox = new DevSlotCheckBox(player);
        bottomPanel.add(devSlotcheckBox);

        this.add(bottomPanel);

    }


/*
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
        //TODO aggiungi lavoro bottoni
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

    public void SetPlayer(Player player){}*/


}
