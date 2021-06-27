package it.polimi.ingsw.view.gui.ViewComponents.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a red button, usually used as a "cancel" button.
 */
public class CancelButton extends JButton {
    /**
     * Constructs a red, opaque button without a text.
     */
    public CancelButton(){
        super();
        this.setProperties();
    }

    /**
     * Constructs a gray, opaque button with the specified text
     * @param text the text on the button
     */
    public CancelButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.RED);
        this.setOpaque(true);
    }
}
