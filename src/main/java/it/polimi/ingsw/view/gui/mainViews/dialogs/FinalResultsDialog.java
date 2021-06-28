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

public class FinalResultsDialog extends JDialog {
    private static final int panelXPosition = PanelManager.getInstance().getGameFrame().getX();
    private static final int panelYPosition = PanelManager.getInstance().getGameFrame().getY();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private JLabel label;

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

    //used for soloGame
    public void setMultilineText(String string){
        MultiLineTextMaker.multilineJLabelSetText(label, string);
    }


    public void setFinalScores(Map<String, Integer> results){
        String scores = results.keySet().stream().sorted(Comparator.comparing(results::get)).map(key -> key + ":\t" + results.get(key) + "\n").collect(Collectors.joining());
        setMultilineText(scores);
    }
}
