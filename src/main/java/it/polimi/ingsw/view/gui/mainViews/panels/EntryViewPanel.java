package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * This panel is the welcome panel shown to the player once the GUI is open.
 */
public class EntryViewPanel extends JPanel {

    private static final int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private static final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private final ImageIcon logo;
    private final String logoPath = "/logo.png";
    //private final Image logo = ImageIO.read(getClass().getClassLoader().getResourceAsStream("logo.png"));
    private final JTextPane entryPane;
    private final JTextPane instruction;
    private final JLabel imageLabel;
    private Font f;
    private final Color color;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public EntryViewPanel() {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        color = new Color(206,98,49);
        this.setBackground(color);

        f = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,30);
        System.out.println(panelWidth);
        this.setSize(panelWidth, panelHeight);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PanelManager.getInstance().seeLoginPopup();
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        entryPane = new JTextPane();
        entryPane.setText("welcome in our Game");
        entryPane.setEditable(false);
        entryPane.setBounds(0, 0, panelWidth, 100);
        entryPane.setFont(f);
        entryPane.setBackground(color);
        StyledDocument doc = entryPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        this.add(entryPane, BorderLayout.PAGE_START);
        entryPane.setVisible(true);


        instruction = new JTextPane();
        instruction.setText("click to continue");
        instruction.setEditable(false);
        instruction.setSize(panelWidth, 500);
        //instruction.setBounds(0, 0, panelWidth, 300);
        instruction.setFont(f);
        instruction.setBackground(color);
        doc = instruction.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        this.add(instruction, BorderLayout.PAGE_END);
        instruction.setVisible(true);

        logo = new ImageIcon(getClass().getResource(logoPath));
        imageLabel = new JLabel("", logo, JLabel.CENTER);
        imageLabel.setBounds(0, 0, panelWidth, panelHeight-200);
        imageLabel.setSize(600, 400);
        this.add(imageLabel, BorderLayout.CENTER);
        imageLabel.setVisible(true);

    }
}
