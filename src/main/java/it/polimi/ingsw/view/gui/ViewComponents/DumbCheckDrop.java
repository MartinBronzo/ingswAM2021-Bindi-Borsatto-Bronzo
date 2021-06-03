package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;

import javax.swing.*;

//This class has been used for testing purposes so far but it may be used for checking whether a drop in the trash can can be done
//TODO: probabilmente ci sarà da fare una funziona checker che restituisce sempre true in caso il drop sia sempre permesso
//come nel caso del numero di icone limitate che possono essere messe dentro lo strongbox
public class DumbCheckDrop implements DropChecker {
    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return true;
    }
}
