package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionPanel extends JPanel{
    JButton b;
    JLabel l;


    public InstructionPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setBackground(Color.YELLOW);

        l = new JLabel();
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        this.add(l, c);

        b = new SubmitButton("Confirm");
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,10,10,10);  //top padding
        c.gridx = 3;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(b, c);
    }

    public void setButtonActionListener(ActionListener actionListener){
        this.b.addActionListener(actionListener);
    }

    public void setLabelText(String text){
        this.l.setText(text);
    }

}
