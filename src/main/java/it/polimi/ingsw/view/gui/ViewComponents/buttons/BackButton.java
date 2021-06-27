package it.polimi.ingsw.view.gui.ViewComponents.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a gray button, usually used as a "back" button.
 */
public class BackButton extends JButton {

    /**
     * Constructs a gray, opaque button without a text.
     */
    public BackButton(){
        super();
        this.setProperties();
    }

    /**
     * Constructs a gray, opaque button with the specified text
     * @param text the text on the button
     */
    public BackButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.GRAY);
        this.setOpaque(true);
    }
}
