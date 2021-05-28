package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.*;

public class UndoButton extends JButton {
    public UndoButton(){
        super();
        this.setProperties();
    }

    public UndoButton(String text){
        super(text);
        this.setProperties();
    }

    private void setProperties(){
        this.setBackground(Color.RED);
        this.setOpaque(true);
    }
}
