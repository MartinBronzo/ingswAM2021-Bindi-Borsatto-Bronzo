package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel shows the panel of the playerBoard of the selected player in the JComboBox
 */
public class MainPanel extends JPanel {
    private ActualPlayerBoardPanel actualPlayerBoardPanel;
    private JLabel stateLabel;
    private boolean created;

    /**
     * shows dynamically the playerboard of the selected player
     * @param playersNicks list of names of all the players in the game
     * @param actualPlayer the player that controls the GUI
     */
    public MainPanel(List<String> playersNicks, Player actualPlayer, Game game) {
        this.created = true;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        String actualPlayerName = actualPlayer.getNickName();
        JPanel playerBoardPanel = new JPanel();
        playerBoardPanel.setLayout(new CardLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new TitledBorder("ControlPanel"));
        controlPanel.setAlignmentX(CENTER_ALIGNMENT);

        stateLabel = new JLabel();
        stateLabel.setText(actualPlayer.getPlayerState().name());
        stateLabel.setBorder(new TitledBorder("PlayerState"));
        stateLabel.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(stateLabel);

        JButton moveButton = new JButton("Move Resources");
        moveButton.setAlignmentX(LEFT_ALIGNMENT);
        moveButton.addActionListener(event -> PanelManager.getInstance().showMoveResources());
        controlPanel.add(moveButton);

        JButton productionButton = new JButton("Activavte Production");
        productionButton.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(productionButton);

        JButton marketButton = new JButton("Show Market");
        marketButton.setAlignmentX(LEFT_ALIGNMENT);
        marketButton.addActionListener(event -> PanelManager.getInstance().displayMarket());
        controlPanel.add(marketButton);

        JButton devGridButton = new JButton("Show DevGrid");
        devGridButton.setAlignmentX(LEFT_ALIGNMENT);
        devGridButton.addActionListener(event -> PanelManager.getInstance().displayDevGrid());
        controlPanel.add(devGridButton);

        JButton endButton = new JButton("End turn");
        endButton.addActionListener(event -> PanelManager.getInstance().writeMessage(
                new Command("endTurn", new Message())));
        endButton.setAlignmentX(RIGHT_ALIGNMENT);
        controlPanel.add(endButton);

        JComboBox<String> comboBox = new JComboBox<>(playersNicks.toArray(new String[0]));
        comboBox.setEditable(false);
        comboBox.addItemListener(e -> {
            CardLayout cl = (CardLayout) (playerBoardPanel.getLayout());
            cl.show(playerBoardPanel, (String) e.getItem());
        });
        comboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxx");
        comboBox.setAlignmentX(RIGHT_ALIGNMENT);
        controlPanel.add(comboBox);

        JButton quitButton = new JButton("QUIT");
        quitButton.addActionListener(event -> PanelManager.getInstance().writeMessage(
                new Command("quit", new Message())));
        quitButton.setAlignmentX(LEFT_ALIGNMENT);
        //todo:
        //quitButton.addActionListener(event -> PanelManager.getInstance().quit());
        controlPanel.add(quitButton);

        this.add(controlPanel);

        //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
        //adds first the actual player because this is the default value of the component

        actualPlayerBoardPanel = new ActualPlayerBoardPanel(actualPlayer);
        playerBoardPanel.add(actualPlayerBoardPanel, actualPlayerName);

        for (String playerName : playersNicks) {
            //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
            if (!playerName.equals(actualPlayerName)) {
                /*OthersPlayerBoardPanel othersPlayerBoardPanel = new OthersPlayerBoardPanel();
                playerBoardPanel.add(othersPlayerBoardPanel, playerName);*/

                //TODO: questa è una prova, cancellabile
                JPanel prova1 = new JPanel();
                prova1.add(new JLabel("prova panel 1"));
                playerBoardPanel.add(prova1, playerName);
            }
        }

        this.add(playerBoardPanel);

    }

    public MainPanel(){
        this.created = false;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

   /* public void setMainPanel(List<String> playersNicks, Player actualPlayer, Game game){
        this.created = true;

        String actualPlayerName = actualPlayer.getNickName();
        JPanel playerBoardPanel = new JPanel();
        playerBoardPanel.setLayout(new CardLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new TitledBorder("ControlPanel"));
        controlPanel.setAlignmentX(CENTER_ALIGNMENT);

        stateLabel = new JLabel();
        stateLabel.setText(actualPlayer.getPlayerState().name());
        stateLabel.setBorder(new TitledBorder("PlayerState"));
        stateLabel.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(stateLabel);

        JButton actionButton = new JButton("Choose main action");
        actionButton.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(actionButton);

        JButton endButton = new JButton("End turn");
        endButton.setAlignmentX(RIGHT_ALIGNMENT);
        controlPanel.add(endButton);

        JComboBox<String> comboBox = new JComboBox<>(playersNicks.toArray(new String[0]));
        comboBox.setEditable(false);
        comboBox.addItemListener(e -> {
            CardLayout cl = (CardLayout) (playerBoardPanel.getLayout());
            cl.show(playerBoardPanel, (String) e.getItem());
        });
        comboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxx");
        comboBox.setAlignmentX(RIGHT_ALIGNMENT);
        comboBox.setSelectedItem(actualPlayer.getNickName());
        controlPanel.add(comboBox);

        this.add(controlPanel);

        //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
        //adds first the actual player because this is the default value of the component

        actualPlayerBoardPanel = new ActualPlayerBoardPanel(game.getMainBoard(), (ArrayList<LeaderCard>) actualPlayer.getUsedLeaders(), (ArrayList<LeaderCard>) actualPlayer.getUnUsedLeaders() );
        playerBoardPanel.add(actualPlayerBoardPanel, actualPlayerName);

        for (String playerName : playersNicks) {
            //TODO: AGGIUNGERE QUANDO LE CLASSI SONO FATTE
            if (!playerName.equals(actualPlayerName)) {
                /*OthersPlayerBoardPanel othersPlayerBoardPanel = new OthersPlayerBoardPanel();
                playerBoardPanel.add(othersPlayerBoardPanel, playerName);*/
/*
                //TODO: questa è una prova, cancellabile
                JPanel prova1 = new JPanel();
                prova1.add(new JLabel("prova panel 1"));
                playerBoardPanel.add(prova1, playerName);
            }
        }

        this.add(playerBoardPanel);
        this.validate();
    }*/

    public synchronized void updateMainPanel(Game game){
        String nickname;
        nickname = PanelManager.getInstance().getNickname();
        stateLabel.setText(game.findByNick(nickname).getPlayerState().name());
        actualPlayerBoardPanel.updatePlayerBoard(game);
        this.validate();
    }

    public boolean getCreated(){
        return created;
    }
}
