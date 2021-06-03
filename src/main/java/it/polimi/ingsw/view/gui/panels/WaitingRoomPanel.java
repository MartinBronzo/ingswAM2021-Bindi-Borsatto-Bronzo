package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import javax.swing.text.BoxView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class WaitingRoomPanel extends JPanel {

    private static final int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();

    private JLabel label;
    private final Color color;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(194, 115, 27));
        g2d.fillRect(100, 100, panelWidth-200, panelHeight-230);
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public WaitingRoomPanel() throws IOException {
        super(new BorderLayout());
        this.setSize(panelWidth, panelHeight);
        color = new Color(139, 128, 44);
        this.setBackground(color);
        this.setOpaque(true);

        label = new JLabel("WAITING ROOM", null, JLabel.CENTER);
        this.add(label, BorderLayout.CENTER);

    }
}