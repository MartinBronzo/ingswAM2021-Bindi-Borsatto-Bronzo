package it.polimi.ingsw.view.gui.ViewComponents.market;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.view.gui.ViewComponents.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.InstructionPanel;
import it.polimi.ingsw.view.gui.panels.CardCheckbox;
import it.polimi.ingsw.view.gui.panels.CardComboBox;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyFromMarketPanel extends JPanel {
    private Player player;
    private Board board;
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private final String infoPath ="src/main/resources/PUNCHBOARD/infoBiglie.png";
    private JPanel market;
    private JLabel instructionLabel;
    private JButton cancelButton;
    private CardComboBox cardComboBoxPanel;
    private MarketGridPanel marketGridPanel;


    public BuyFromMarketPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(panelWidth, panelHeight);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        this.market = new JPanel();
        this.market.setLayout(new BoxLayout(market, BoxLayout.PAGE_AXIS));
        this.market.setSize(300, panelHeight-30);

        JLabel info = new JLabel();
        info.setSize(300, 200);
        try {
            Image img = ImageIO.read(new File(infoPath));
            Image dimg = img.getScaledInstance(300, info.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            info.setIcon(imageIcon);
        } catch (IOException e) {
            info.setIcon(null);
            info.setText("Error printing infoPath");
        }
        this.market.add(info);
        this.add(this.market);

        this.add(Box.createHorizontalGlue());

        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
        this.cardComboBoxPanel = new CardComboBox();
        rightSidePanel.add(cardComboBoxPanel);

        instructionLabel = new JLabel("Choice as many leader as the number of whiteMarbles");
        //instructionLabel.setSize(rightSidePanel.getWidth(), rightSidePanel.getHeight() - cardComboBoxPanel.getHeight() - 100);
        instructionLabel.setMinimumSize(new Dimension(400, 20));
        instructionLabel.setPreferredSize(new Dimension(400, 20));
        instructionLabel.setMaximumSize(new Dimension(400, 20));
        rightSidePanel.add(instructionLabel);
        instructionLabel = new JLabel("in the desired row or column and then press the desired button");
        //instructionLabel.setSize(rightSidePanel.getWidth(), rightSidePanel.getHeight() - cardComboBoxPanel.getHeight() - 100);
        instructionLabel.setMinimumSize(new Dimension(400, 20));
        instructionLabel.setPreferredSize(new Dimension(400, 20));
        instructionLabel.setMaximumSize(new Dimension(400, 20));
        rightSidePanel.add(instructionLabel);
        this.cancelButton = new CancelButton();
        this.cancelButton.setText("cancel");
        this.cancelButton.addActionListener(e -> setVisible(false));
        rightSidePanel.add(cancelButton);
        this.add(rightSidePanel);
    }

    public synchronized void setPlayer(Player player) throws NullPointerException{
        if (player==null) throw new NullPointerException("Player can't be null");
        this.player = player;
    }

    public synchronized void setBoard(Board board) throws NullPointerException{
        if (board==null) throw new NullPointerException("Player can't be null");
        this.board = board;
        this.marketGridPanel = new MarketGridPanel(board);
        this.market.add(marketGridPanel);
        marketGridPanel.setSize(this.market.getWidth(), this.market.getHeight()-250);

        this.revalidate();
    }

    public synchronized void print() throws NullPointerException {
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;

        if (player == null || board == null)
            throw new NullPointerException("missing parameters to setup the panel");

        //TODO we can see only DiscountLeaderEffect adding a filter with instaceof but doing this lose the control of the getusedLeaers index to send to the controller, we should change cardComboBoxPanel
        List<String> leaderCardsPath = player.getUsedLeaders().stream()./*filter(card -> card.getEffect() instanceof DiscountLeaderEffect).*/map(card -> card.getUrl()).collect(Collectors.toList());
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <5; i++)
            list.add(i);
        this.cardComboBoxPanel.resetView(leaderCardsPath, list);
        this.marketGridPanel.update();

        this.setVisible(true);
    }

    public List<Integer> getLeaderList(){
        return this.cardComboBoxPanel.getSelectedLeaderIndexes();
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
