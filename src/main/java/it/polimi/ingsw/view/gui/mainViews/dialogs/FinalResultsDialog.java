package it.polimi.ingsw.view.gui.mainViews.dialogs;

import it.polimi.ingsw.view.gui.ViewComponents.utils.MultiLineTextMaker;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This dialog is used to show the final results of a game.
 */
public class FinalResultsDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;

    /**
     * Constructs a FinalResultsDialog which still needs the message, which it is going to display, to be set
     * @param owner the Frame of the player
     */
    public FinalResultsDialog(Frame owner) {
        super(owner, "The Game Ended", true);

        label = new JLabel();


        this.setLayout( new FlowLayout() );
        this.add(label);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                PanelManager.getInstance().manageLogoutCommand();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.setSize(panelWidth, 100);
        setLocation(panelXPosition,panelYPosition);
    }

    /**
     * Sets the message to be shown to the player if the player is playing in a Solo Game
     * @param string the message to be shown to the player
     */
    //used for soloGame
    public void setMultilineText(String string){
        MultiLineTextMaker.multilineJLabelSetText(label, string);
    }

    /**
     * Sets the message to be shown to the player if the player is playing in a multi-player game
     * @param results the map containing the results of this game
     */
    public void setFinalScores(Map<String, Integer> results){
        String scores = results.keySet().stream().sorted(Comparator.comparing(results::get)).map(key -> key + ":\t" + results.get(key) + "\n").collect(Collectors.joining());
        setMultilineText(scores);
    }
}
