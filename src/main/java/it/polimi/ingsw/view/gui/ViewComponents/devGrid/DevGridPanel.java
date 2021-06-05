package it.polimi.ingsw.view.gui.ViewComponents.devGrid;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DevGridPanel extends JPanel{
    //DevCard[][] devGrid;
    private final Board mainBoard;
    private final String directoryPath = "src/main/resources/front/";

    public DevGridPanel(Board mainBoard){
        super(new GridLayout(3, 4, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.mainBoard = mainBoard;
        DevCard[][] devGrid = this.mainBoard.getDevMatrix();
        BufferedImage img = null;
        /*
        try {
            img = ImageIO.read(new File(logoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg;
        ImageIcon logo = new ImageIcon(logoPath);

         */
        int c;
        JButton button;
        for (int i = 0; i < devGrid.length; i++) {
            for (int j = 0; j < devGrid[i].length; j++) {
                //c = (1 + j + 4 * i);
                int row = i;
                int col = j;
                button = new JButton(/*Integer.toString(c)*/);
                button.addActionListener(event -> {
                    try {
                        PanelManager.getInstance().printGetCardCostPanel(row, col);
                    }catch (IllegalArgumentException ae){
                        PanelManager.getInstance().printError("You can't buy this card");
                    }catch (IllegalStateException se){
                        PanelManager.getInstance().printError("You can't buy a card now");
                    }
                });
                this.add(button);
                /*
                dimg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
                logo = new ImageIcon(dimg);
                button.setIcon(logo);

                 */
            }
        }
    }

    public void update(){
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;
        JButton button;
        DevCard devCard;
        int i=0;
        int j=0;
        DevCard[][] devGrid = this.mainBoard.getDevMatrix();
        for (Component component:this.getComponents()){
            System.out.println(i+","+j);
            devCard = devGrid[i][j];
            button = (JButton)component;

            if (devCard == null){
                button.setIcon(null);
                button.setText("Empty Deck");
            }else {
                try {
                    img = ImageIO.read(new File(directoryPath+devCard.getUrl()));
                    dimg = img.getScaledInstance(component.getWidth(), component.getHeight(), Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(dimg);
                    button.setIcon(imageIcon);
                } catch (IOException e) {
                    button.setIcon(null);
                    button.setText(devCard.toString());
                }
            }

            j++;
            if (j == devGrid[i].length) {
                i++;
                j = 0;
            }
        }

    }

    public void update(int width, int height){
        BufferedImage img = null;
        Image dimg;
        ImageIcon imageIcon;
        JButton button;
        DevCard devCard;
        int i=0;
        int j=0;
        DevCard[][] devGrid = this.mainBoard.getDevMatrix();
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
            if (j == devGrid[i].length) {
                i++;
                j = 0;
            }
        }

    }
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
