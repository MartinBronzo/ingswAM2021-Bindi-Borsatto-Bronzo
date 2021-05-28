package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {

    public BackButton(){
        super();
        this.setProperties();
    }

    public BackButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.GRAY);
        this.setOpaque(true);
    }
}
