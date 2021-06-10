package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.*;

public class InstructionPanelFree extends JPanel{
    //private JTextArea text;
    private JLabel text;
    private JPanel buttons;

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
}
