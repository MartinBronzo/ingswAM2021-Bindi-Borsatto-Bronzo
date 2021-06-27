package it.polimi.ingsw.view.gui.ViewComponents.utils;

import javax.swing.*;
import java.awt.*;

/**
 * This panel is an instruction panel that can hold print information and up to three buttons. It has no layout.
 */
public class InstructionPanelFree extends JPanel{
    //private JTextArea text;
    private JLabel text;
    private JPanel buttons;

    /**
     * Constructs an information panel which shows the specified text and two buttons
     * @param info the information to be shown
     * @param confirm the first button to be used as a confirmation button
     * @param cancel the second button to be used as a cancellation button
     */
    public InstructionPanelFree(String info, JButton confirm, JButton cancel){
        this.setLayout(new BorderLayout());

        //this.text = new JTextArea(100, 100);
        //this.text.append(info);
        this.text = new JLabel(info);
        this.add(text, BorderLayout.CENTER);

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(confirm);
        buttons.add(cancel);
        this.add(buttons, BorderLayout.PAGE_END);

        this.validate();
        this.repaint();

    }

    /**
     * Constructs an information panel which shows the specified text and three buttons
     * @param info the information to be shown
     * @param confirm the first button to be used as a confirmation button
     * @param cancel the second button to be used as a cancellation button
     * @param back the third button to be used as a back button
     */
     public InstructionPanelFree(String info, JButton confirm, JButton cancel, JButton back){
        this.setLayout(new BorderLayout());

        //this.text = new JTextArea(100, 100);
        //this.text.append(info);
        this.text = new JLabel(info);
        this.add(text, BorderLayout.CENTER);

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(confirm);
        buttons.add(cancel);
        buttons.add(back);
        this.add(buttons, BorderLayout.PAGE_END);

        this.validate();
        this.repaint();

    }
}
