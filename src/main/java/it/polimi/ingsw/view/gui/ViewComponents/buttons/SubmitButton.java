package it.polimi.ingsw.view.gui.ViewComponents.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a green button, usually used as a "submit" button.
 */
public class SubmitButton extends JButton {

    /**
     * Constructs a green, opaque button without a text.
     */
    public SubmitButton(){
        super();
        this.setProperties();
    }

    /**
     * Constructs a green, opaque button with the specified text
     * @param text the text on the button
     */
    public SubmitButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.GREEN);
        this.setOpaque(true);
    }
}
