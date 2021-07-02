package it.polimi.ingsw.view.gui.ViewComponents.market;

import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This panel represents the Market in the game.
 */
public class MarketGridPanel extends JPanel{
    private final Board mainBoard;
    private final String directoryPath = "/front/";

    /**
     * Constructs a MarketGridPanel
     * @param mainBoard the LightModel object which contains the information on the Market
     */
    public MarketGridPanel(Board mainBoard){
        super(new GridLayout(4, 5, 5, 5));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.mainBoard = mainBoard;
        MarbleType[][] marketMatrix = mainBoard.getMarketMatrix();
        JButton button;
        JLabel label;
        for (int i = 0; i < marketMatrix.length; i++) {
            int row = i;
            for (int j = 0; j < marketMatrix[i].length; j++) {
                label = new JLabel();
                this.add(label);
            }
            button = new SubmitButton("←"+ (i + 1));
            button.addActionListener(event -> PanelManager.getInstance().manageGetResourcesFromMarket(true, row));
            this.add(button);
        }
        for (int j = 0; j < marketMatrix[0].length; j++) {
            int col = j;
            button = new SubmitButton("↑" + (j + 1));
            button.addActionListener(event -> PanelManager.getInstance().manageGetResourcesFromMarket(false, col));
            this.add(button);
        }
        label = new JLabel();
        /*
        try {
            img = ImageIO.read(new File(marbleOnSlide.getUrl()));
            dimg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            label.setIcon(imageIcon);
        } catch (IOException e) {
            label.setIcon(null);
            label.setText(marbleOnSlide.toString());
        }

         */
        this.add(label);
        /*
        BackButton backButton = new BackButton("Back");
        backButton.addActionListener(event -> PanelManager.getInstance().showPlayerBoard(this));
        backButton.setSize(new Dimension(50,20));
        this.add(backButton);*/

    }

    /**
     * Updates this panel
     */
    public void update(){
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;
        JLabel label;
        Component component;
        MarbleType[][] marketMatrix = mainBoard.getMarketMatrix();
        MarbleType marbleOnSlide = mainBoard.getMarbleOnSlide();
        final int marble_height = 80;
        final int marble_width = 80;


        int c;
        for (int i = 0; i < marketMatrix.length; i++) {
            for (int j = 0; j < marketMatrix[i].length; j++) {
                c = (j + (marketMatrix.length +2) * i);
                component = this.getComponent(c);
                label = (JLabel)component;

                try {
                    img = ImageIO.read(getClass().getResource(marketMatrix[i][j].getUrl()));
                    dimg = img.getScaledInstance(marble_width, marble_height, Image.SCALE_SMOOTH);
                    //dimg = img.getScaledInstance(component.getWidth()-30, component.getHeight()-30, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(dimg);
                    label.setIcon(imageIcon);
                } catch (IOException e) {
                    label.setIcon(null);
                    label.setText(marketMatrix[i][j].toString());
                }
            }
        }
        component = this.getComponent((marketMatrix.length+1)*(marketMatrix[0].length+1)-1);
        label = (JLabel)component;

        try {
            img = ImageIO.read(getClass().getResource(marbleOnSlide.getUrl()));
            dimg = img.getScaledInstance(marble_width, marble_height, Image.SCALE_SMOOTH);
            //dimg = img.getScaledInstance(component.getWidth()-30, component.getHeight()-30, Image.SCALE_SMOOTH);

            imageIcon = new ImageIcon(dimg);
            label.setIcon(imageIcon);
        } catch (IOException e) {
            label.setIcon(null);
            label.setText(marbleOnSlide.toString());
        }

    }

    /*
    public void update(int width, int height){
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;
        JButton button;
        DevCard devCard;
        int i=0;
        int j=0;
        for (Component component:this.getComponents()){
            //System.out.println(i+","+j);
            devCard = devGrid[i][j];
            button = (JButton)component;

            if (devCard == null){
                button.setIcon(null);
                button.setText("Empty Deck");
            }else {
                try {
                    //System.out.println(devCard.getUrl());
                    img = ImageIO.read(new File(directoryPath+devCard.getUrl()));
                    dimg = img.getScaledInstance(width,height, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(dimg);
                    button.setIcon(imageIcon);
                } catch (IOException e) {
                    button.setIcon(null);
                    button.setText(devCard.toString());
                }
            }

            j++;
            if (j == this.devGrid[i].length) {
                i++;
                j = 0;
            }
        }
    }

     */
}

/*
public class BeginningDecisionsPanel extends JPanel {

    public BeginningDecisionsPanel(Board mainBoard){
        super(new GridLayout(3, 4, 10, 10));
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JPanel discardLeaderPanel = new JPanel();
        discardLeaderPanel.setLayout(new BoxLayout(discardLeaderPanel, BoxLayout.PAGE_AXIS));
        discardLeaderPanel.setBorder(new TitledBorder("Select 2 Leader Cards to discard"));


        CardCheckbox leaderCheckbox1 = new CardCheckbox(leaderList.subList(0, 2), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox1);
        CardCheckbox leaderCheckbox2 = new CardCheckbox(leaderList.subList(2,4), "Discard this leader card");
        discardLeaderPanel.add(leaderCheckbox2);

        this.add(discardLeaderPanel);

        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
        rightSidePanel.add(new DnDDepot());
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setLabelText("Select 2 Leader Cards to discard and place the resources, if present, in the depot");
        rightSidePanel.add(instructionPanel);

        this.add(rightSidePanel);

//        this.setSize(panelWidth, 100);
        // setLocation(panelXPosition,panelYPosition);


    }

}

 */
