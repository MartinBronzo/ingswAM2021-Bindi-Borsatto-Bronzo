package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionPanel extends JPanel{
    JButton b;
    JLabel l;


    public InstructionPanel() {
        // create a label to display text
        l = new JLabel();

        // create a new buttons
        b = new JButton("Confirm");

        // set Box Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add buttons and textfield to panel
        //The order the elements are added determines how they will be displayed: since we added the label before the button,
        //then we'll see the label above the button
        this.add(l);
        this.add(b);



        // setbackground of panel
        this.setBackground(Color.yellow);
    }
    public void setButtonActionListener(ActionListener actionListener){
        this.b.addActionListener(actionListener);
    }

    public void setLabelText(String text){
        this.l.setText(text);
    }

}
