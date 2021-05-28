package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

public class EntryViewPanel extends JPanel {

    private static final int panelHeight = PanelManager.getInstance().getGameFrame().getHeight();
    private final int panelWidth = PanelManager.getInstance().getGameFrame().getWidth();
    private final ImageIcon logo;
    private final String logoPath = "src/main/resources/logo.png";
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
    public EntryViewPanel() throws IOException {
        super(new BorderLayout());
        color = new Color(206,98,49);
        this.setBackground(color);

        f = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,30);
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
        entryPane.setBounds(0, 0, 600, 100);
        entryPane.setFont(f);
        entryPane.setBackground(color);
        StyledDocument doc = entryPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        this.add(entryPane, BorderLayout.NORTH);
        entryPane.setVisible(true);

        logo = new ImageIcon(logoPath);
        imageLabel = new JLabel("", logo, JLabel.CENTER);
        imageLabel.setBounds(0, 0, 600, 400);
        imageLabel.setSize(600, 400);
        this.add(imageLabel, BorderLayout.CENTER);
        imageLabel.setVisible(true);

        instruction = new JTextPane();
        instruction.setText("click to continue");
        instruction.setEditable(false);
        instruction.setBounds(0, 0, 600, 100);
        instruction.setFont(f);
        instruction.setBackground(color);
        doc = instruction.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        this.add(instruction, BorderLayout.SOUTH);
        instruction.setVisible(true);
    }
}
