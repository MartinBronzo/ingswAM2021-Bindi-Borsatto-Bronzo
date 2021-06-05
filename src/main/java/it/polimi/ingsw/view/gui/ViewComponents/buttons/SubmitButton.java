package it.polimi.ingsw.view.gui.ViewComponents.buttons;

import javax.swing.*;
import java.awt.*;

public class SubmitButton extends JButton {

    public SubmitButton(){
        super();
        this.setProperties();
    }

    public SubmitButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.GREEN);
        this.setOpaque(true);
    }
}
