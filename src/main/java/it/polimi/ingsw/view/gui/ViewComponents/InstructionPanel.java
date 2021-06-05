package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionPanel extends JPanel{
    JButton confirmButton;
    JButton cancelButton;
    JLabel l;


    public InstructionPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //this.setBackground(Color.YELLOW);

        l = new JLabel();
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        this.add(l, c);

        confirmButton = new SubmitButton("Confirm");
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,10,10,10);  //top padding
        c.gridx = 3;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(confirmButton, c);
    }

    public InstructionPanel(boolean cancelButtom) {
        this();
        if (cancelButtom) {
            GridBagConstraints c = new GridBagConstraints();
            cancelButton = new CancelButton("Cancel");
            c.fill = GridBagConstraints.RELATIVE;
            c.ipady = 0;       //reset to default
            c.weighty = 1.0;   //request any extra vertical space
            c.anchor = GridBagConstraints.PAGE_END; //bottom of space
            c.insets = new Insets(10, 10, 10, 10);  //top padding
            c.gridx = 2;       //aligned with button 2
            c.gridwidth = 1;   //2 columns wide
            c.gridy = 2;       //third row
            this.add(cancelButton, c);
        }
    }

    public void setConfirmActionListener(ActionListener actionListener){
        this.confirmButton.addActionListener(actionListener);
    }

    public void setCancelActionListener(ActionListener actionListener) {
        if (cancelButton!=null)
            this.confirmButton.addActionListener(actionListener);
    }

    public void setLabelText(String text){
        this.l.setText(text);
    }

}
