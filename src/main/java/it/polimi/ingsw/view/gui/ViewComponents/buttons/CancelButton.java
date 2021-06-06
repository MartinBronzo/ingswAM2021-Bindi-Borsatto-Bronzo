package it.polimi.ingsw.view.gui.ViewComponents.buttons;

import javax.swing.*;
import java.awt.*;

public class CancelButton extends JButton {
    public CancelButton(){
        super();
        this.setProperties();
    }

    public CancelButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.RED);
        this.setOpaque(true);
    }
}
