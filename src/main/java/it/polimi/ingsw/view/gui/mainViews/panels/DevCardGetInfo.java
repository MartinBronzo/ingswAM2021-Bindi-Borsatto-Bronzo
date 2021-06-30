package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.CardCheckbox;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel is used to show to the player the DevCard they have selected from the DevGrid and to let them specify the
 * Discount LeaderCards they would like to use in order to pay for the card cost.
 */
public class DevCardGetInfo extends JPanel {
    private int row;
    private int column;
    private DevCard[][] devMatrix;
    private Player player;
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private final String directoryPath = "src/main/resources/front/";
    private JLabel selectedCard;
    private InstructionPanel instructionPanel;
    private CardCheckbox cardCheckboxPanel;

    /**
     * Constructs a DevCardGetInfo. Before using this object, the user of this class must specify the player who's using
     * this panel by calling the setPlayer method
     */
    public DevCardGetInfo() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(panelWidth, panelHeight-100);
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
            //System.out.println(row+1+" "+column+1+" "+cardCheckboxPanel.getSelectedLeaderIndexes());
            PanelManager.getInstance().manageGetCardCost(row, column, cardCheckboxPanel.getSelectedLeaderIndexes());
            //this.setVisible(false);
            //TODO idk if there is the need to print something else
        });
        instructionPanel.setCancelActionListener(e -> {
            //TODO: idk if there is the need to print something else
            PanelManager.getInstance().displayDevGrid();
            this.setVisible(false);
        });
        instructionPanel.setLabelText("Press submit to get the final CardCost after selecting the leader Cards");
        rightSidePanel.add(instructionPanel);
        this.add(rightSidePanel);
    }

    /**
     * Communicates to this object which row and column the player chose in the DevGrid
     * @param row the row the player chose
     * @param column the row the player chose
     * @throws NullPointerException if the LightModel communicated to the PanelManager by the Server has no DevGrid specified
     */
    public synchronized void selectCell(int row, int column) throws NullPointerException{
        this.row = row;
        this.column = column;
        this.devMatrix = PanelManager.getInstance().getGameModel().getMainBoard().getDevMatrix();
        if (devMatrix == null) throw new NullPointerException("can't select cell if gamemodel has devgrid null");
    }

    /**
     * Sets the Player object this panel will use in order to display the right information to the specified player
     * @param player the LightModel object which represents the player who's using this panel
     * @throws NullPointerException if the specified player attribute is a null pointer
     */
    public synchronized void setPlayer(Player player) throws NullPointerException{
        if (player==null) throw new NullPointerException("Player can't be null");
        this.player = player;
    }

    /**
     * Print this panel
     * @throws NullPointerException if the Player has not been specified yet or the LightModel communicated to the PanelManager by the Server has no DevGrid specified
     */
    public synchronized void print() throws NullPointerException {
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;

        if (player == null || devMatrix == null)
            throw new NullPointerException("missing parameters to setup the panel");
        DevCard devCard = devMatrix[row][column];
        if (devCard == null) throw new NullPointerException("there is no card in the selected cell");

        try {
            img = ImageIO.read(new File(directoryPath + devCard.getUrl()));
            dimg = img.getScaledInstance(333, 555, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            selectedCard.setIcon(imageIcon);
        } catch (IOException e) {
            selectedCard.setIcon(null);
            selectedCard.setText(devCard.toString());
        }

        List<String> leaderCardsPath = player.getUsedLeaders().stream()./*filter(card -> card.getEffect() instanceof DiscountLeaderEffect).*/map(card -> card.getUrl()).collect(Collectors.toList());
        this.cardCheckboxPanel.resetView(leaderCardsPath, "Do you want to use this card?");

        this.setVisible(true);
    }
    
    /*
    public DevCardGetInfo() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(panelWidth, panelHeight);
        this.leader1Selector = new AtomicBoolean();
        this.leader2Selector = new AtomicBoolean();

        this.selectedCard = new JLabel();
        this.add(this.selectedCard);


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
        });
        this.add(this.submit);

        this.cancel = new JButton("cancel");
        this.cancel.addActionListener(e -> {
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
