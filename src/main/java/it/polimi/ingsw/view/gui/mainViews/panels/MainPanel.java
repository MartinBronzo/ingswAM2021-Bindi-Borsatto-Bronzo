package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * This panel shows the panel of the playerBoard of the selected player in the JComboBox
 */
public class MainPanel extends JPanel {
    private ActualPlayerBoardPanel actualPlayerBoardPanel;
    private JLabel stateLabel;
    private JLabel vpLabel;
    private boolean created;

    /**
     * shows dynamically the playerboard of the selected player
     *
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
        stateLabel.setBorder(new TitledBorder("Player State"));
        stateLabel.setPreferredSize(new Dimension(100, 45));
        stateLabel.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(stateLabel);

        vpLabel = new JLabel();
        vpLabel.setText(actualPlayer.getVictoryPoints().toString());
        vpLabel.setBorder(new TitledBorder("Victory Points"));
        vpLabel.setPreferredSize(new Dimension(80, 45));
        vpLabel.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(vpLabel);

        JButton moveButton = new JButton("Move Resources");
        moveButton.setAlignmentX(LEFT_ALIGNMENT);
        moveButton.addActionListener(event -> PanelManager.getInstance().showMoveResources());
        controlPanel.add(moveButton);

        JButton productionButton = new JButton("Activate Production");
        productionButton.setAlignmentX(LEFT_ALIGNMENT);
        productionButton.addActionListener(event -> PanelManager.getInstance().displayProduction());
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
        comboBox.setSelectedItem(actualPlayer.getNickName());
        controlPanel.add(comboBox);

        JButton quitButton = new JButton("QUIT");
        quitButton.addActionListener(event -> PanelManager.getInstance().manageLogoutCommand());
        quitButton.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(quitButton);

        JButton cheatButton = new JButton("Cheat");
        cheatButton.addActionListener((event -> PanelManager.getInstance().manageCheat()));
        cheatButton.setAlignmentX(LEFT_ALIGNMENT);
        controlPanel.add(cheatButton);

        this.add(controlPanel);

        //adds first the actual player because this is the default value of the component
        if (playersNicks.size() == 1) {
            SoloPLayerBoardPanel soloPLayerBoardPanel = new SoloPLayerBoardPanel(actualPlayer);
            playerBoardPanel.add(soloPLayerBoardPanel, actualPlayerName);
        } else {
            actualPlayerBoardPanel = new ActualPlayerBoardPanel(actualPlayer);
            playerBoardPanel.add(actualPlayerBoardPanel, actualPlayerName);

            for (String playerName : playersNicks) {
                if (!playerName.equals(actualPlayerName)) {
                    OthersPlayerPlayerBoardPanel othersPlayerBoardPanel = new OthersPlayerPlayerBoardPanel(game.findByNick(playerName));
                    playerBoardPanel.add(othersPlayerBoardPanel, playerName);
                }
            }
        }

        this.add(playerBoardPanel);

    }

    /**
     * Constructs a dumb MarketPlacingResources
     */
    public MainPanel() {
        this.created = false;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    /*public synchronized void updateMainPanel(Game game){
        String nickname;
        nickname = PanelManager.getInstance().getNickname();
        stateLabel.setText(game.findByNick(nickname).getPlayerState().name());
        actualPlayerBoardPanel.updatePlayerBoard(game);
        this.validate();
    }*/

    /**
     * Returns a boolean representing the constructor used to create this object
     * @return false if this object has been created with the dumb constructor, true if it has been created with the main constructor
     */
    public boolean getCreated() {
        return created;
    }
}
