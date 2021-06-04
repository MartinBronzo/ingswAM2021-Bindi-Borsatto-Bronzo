package it.polimi.ingsw.view.gui.ViewComponents.devGrid;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.fromClient.GetProductionCostMessage;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.panels.CardCheckbox;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DevCardPanel1 extends JPanel {
    private int row;
    private int column;
    private DevCard[][] devMatrix;
    private Player player;
    private AtomicBoolean leader1Selector;
    private AtomicBoolean leader2Selector;
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private final String directoryPath = "src/main/resources/front/";
    private LeaderCard[] leaderCards;
    private JLabel selectedCard;
    private InstructionPanel instructionPanel;
    private CardCheckbox cardCheckboxPanel;
    private JButton leader1;
    private JButton leader2;
    private JButton submit;
    private JButton cancel;

    public DevCardPanel1() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(panelWidth, panelHeight);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        this.selectedCard = new JLabel();
        this.selectedCard.setSize(200, 300);
        this.add(this.selectedCard);

        this.add(Box.createHorizontalGlue());

        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
        cardCheckboxPanel = new CardCheckbox();
        rightSidePanel.add(cardCheckboxPanel);

        instructionPanel = new InstructionPanel(true);
        instructionPanel.setConfirmActionListener(e -> {
            System.out.println(row+" "+column+" "+cardCheckboxPanel.getSelectedLeaderIndexes());
            PanelManager.getInstance().writeMessage(new Command("getCardCost", new GetFromMatrixMessage(row, column, cardCheckboxPanel.getSelectedLeaderIndexes())));
            //TODO idk if there is the need to print something else
        });
        instructionPanel.setCancelActionListener(e -> {
            //TODO: idk if there is the need to print something else
            this.setVisible(false);
        });
        instructionPanel.setLabelText("Press submit to get the final CardCost after selecting the leader Cards");
        rightSidePanel.add(instructionPanel);
        this.add(rightSidePanel);


    }

    /*
    public DevCardPanel1() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(panelWidth, panelHeight);
        this.leader1Selector = new AtomicBoolean();
        this.leader2Selector = new AtomicBoolean();

        this.selectedCard = new JLabel();
        this.add(this.selectedCard);


        //TODO; metti tutto il resto in un altro panel verticale e aggiungi info (istruzioni)
        this.leader1 = new JButton();
        this.leader1.addActionListener(e -> {
            if (!leader1Selector.get()){
                leader1Selector.set(true);
                leader1.setBorder(BorderFactory.createLoweredBevelBorder());
            }else{
                leader1Selector.set(false);
                leader1.setBorder(BorderFactory.createRaisedBevelBorder());
            }

        });
        this.add(this.leader1);


        this.leader2 = new JButton("submit");
        this.leader2.addActionListener(e -> {
            if (!leader2Selector.get()){
                leader2Selector.set(true);
                leader2.setBorder(BorderFactory.createLoweredBevelBorder());
            }else{
                leader2Selector.set(false);
                leader2.setBorder(BorderFactory.createRaisedBevelBorder());
            }
        });
        this.add(this.leader2);


        this.submit = new JButton("submit");
        this.submit.addActionListener(e -> {
            List<Integer> leaders = new ArrayList<>(2);
            if (leader1Selector.get())
                leaders.add(0);
            if (leader2Selector.get())
                leaders.add(1);
            PanelManager.getInstance().writeMessage(new Command("getCardCost", new GetFromMatrixMessage(row, column, leaders)));
            //TODO idk if there is the need to print something else
        });
        this.add(this.submit);

        this.cancel = new JButton("cancel");
        this.cancel.addActionListener(e -> {
            //TODO: idk if there is the need to print something else
            this.setVisible(false);
        });
        this.add(this.cancel);

    }
*/
    public synchronized void selectCell(int row, int column) throws NullPointerException{
        this.row = row;
        this.column = column;
        this.devMatrix = PanelManager.getInstance().getGameModel().getMainBoard().getDevMatrix();
        if (devMatrix == null) throw new NullPointerException("can't select cell if gamemodel has devgrid null");
    }

    public synchronized void setPlayer(Player player) throws NullPointerException{
        if (player==null) throw new NullPointerException("Player can't be null");
        this.player = player;
    }

    public synchronized void print() throws NullPointerException{
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;

        if (player == null || devMatrix == null) throw new NullPointerException("missing parameters to setup the panel");
        DevCard devCard = devMatrix[row][column];
        if (devCard == null) throw new NullPointerException("there is no card in the selected cell");

        try {
            img = ImageIO.read(new File(directoryPath+devCard.getUrl()));
            dimg = img.getScaledInstance(333, 555, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            selectedCard.setIcon(imageIcon);
        } catch (IOException e) {
            selectedCard.setIcon(null);
            selectedCard.setText(devCard.toString());
        }

        List<String> leaderCardsPath = player.getUsedLeaders().stream().map(card -> card.getUrl()).collect(Collectors.toList());
        this.cardCheckboxPanel.resetView(leaderCardsPath, "Do you want to use this card?");

        this.setVisible(true);
/*
        try {
            leaderCard = leaderCards.get(0);
            img = ImageIO.read(new File(directoryPath+leaderCard.getUrl()));
            dimg = img.getScaledInstance(selectedCard.getWidth(), selectedCard.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            leader1.setIcon(imageIcon);
        } catch (IOException e) {
            leader1.setIcon(null);
            leader1.setText(devCard.toString());
        } catch (IndexOutOfBoundsException e){
            leader1.setVisible(false);
        }

        try {
            leaderCard = leaderCards.get(1);
            img = ImageIO.read(new File(directoryPath+leaderCard.getUrl()));
            dimg = img.getScaledInstance(selectedCard.getWidth(), selectedCard.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            leader2.setIcon(imageIcon);
        } catch (IOException e) {
            leader2.setIcon(null);
            leader2.setText(devCard.toString());
        } catch (IndexOutOfBoundsException e){
            leader2.setVisible(false);
        }
         */

    }

}
