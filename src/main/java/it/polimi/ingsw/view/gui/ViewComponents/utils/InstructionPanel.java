package it.polimi.ingsw.view.gui.ViewComponents.utils;

import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This panel is an instruction panel that can hold print information and up to three buttons. It has a GridLayout.
 */
public class InstructionPanel extends JPanel{
    JButton confirmButton;
    JButton cancelButton;
    JButton backButton;
    JLabel l;

    /**
     * Constructs an information panel which shows the specified text and two buttons
     * @param info the information to be shown
     * @param confirm the first button to be used as a confirmation button
     * @param cancel the second button to be used as a cancellation button
     */
    public InstructionPanel(String info, JButton confirm, JButton cancel){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //this.setBackground(Color.YELLOW);

        l = new JLabel(info);
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        this.add(l, c);

        confirmButton = confirm;
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,10,10,10);  //top padding
        c.gridx = 3;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(confirmButton, c);

        c = new GridBagConstraints();
        this.cancelButton = cancel;
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10, 10, 10, 10);  //top padding
        c.gridx = 2;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(this.cancelButton, c);

    }

    /**
     * Constructs an information panel which shows the specified text and three buttons
     * @param info the information to be shown
     * @param confirm the first button to be used as a confirmation button
     * @param cancel the second button to be used as a cancellation button
     * @param back the third button to be used as a back button
     */
    public InstructionPanel(String info, JButton confirm, JButton cancel, JButton back){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //this.setBackground(Color.YELLOW);

        l = new JLabel(info);
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        this.add(l, c);

        confirmButton = confirm;
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,10,10,10);  //top padding
        c.gridx = 3;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(confirmButton, c);

        c = new GridBagConstraints();
        this.cancelButton = cancel;
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10, 10, 10, 10);  //top padding
        c.gridx = 2;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(this.cancelButton, c);

        c = new GridBagConstraints();
        this.backButton = back;
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10, 10, 10, 10);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        this.add(this.backButton, c);

    }

    /**
     * Constructs a panel with only a SubmitButton
     */
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

    /**
     * Constructs a panel with the SubmitButton and, if specified, a CancelButton
     * @param cancelButton true if the CancelButton is to be created and shown, false otherwise
     */
    public InstructionPanel(boolean cancelButton) {
        this();
        if (cancelButton) {
            GridBagConstraints c = new GridBagConstraints();
            this.cancelButton = new CancelButton("Cancel");
            c.fill = GridBagConstraints.RELATIVE;
            c.ipady = 0;       //reset to default
            c.weighty = 1.0;   //request any extra vertical space
            c.anchor = GridBagConstraints.PAGE_END; //bottom of space
            c.insets = new Insets(10, 10, 10, 10);  //top padding
            c.gridx = 2;       //aligned with button 2
            c.gridwidth = 1;   //2 columns wide
            c.gridy = 2;       //third row
            this.add(this.cancelButton, c);
        }
    }

    /**
     * Sets the actionLister to the button which is used a confirmation button
     * @param actionListener the actionListener to be added
     */
    public void setConfirmActionListener(ActionListener actionListener){
        this.confirmButton.addActionListener(actionListener);
    }

    /**
     * Sets the actionLister to the button which is used a cancellation button
     * @param actionListener the actionListener to be added
     */
    public void setCancelActionListener(ActionListener actionListener) {
        if (cancelButton!=null)
            this.cancelButton.addActionListener(actionListener);
    }

    /**
     * Sets the actionLister to the button which is used a back button
     * @param actionListener the actionListener to be added
     */
    public void setBackActionListener(ActionListener actionListener){
        if(backButton != null)
            this.backButton.addActionListener(actionListener);
    }

    /**
     * Sets the information text to be shown to the user
     * @param text the text to be shown to the user
     */
    public void setLabelText(String text){
        //this.l.setText(text);
        MultiLineTextMaker.multilineJLabelSetText(this.l, text);
    }

}
