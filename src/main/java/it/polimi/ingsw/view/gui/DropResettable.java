package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;

public interface DropResettable extends Resettable {
    void addDroppedLabel(JLabel label);
}
