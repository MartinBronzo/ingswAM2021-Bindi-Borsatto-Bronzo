package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This ActionListener resets all the states of all the components it takes track of and which implement the Resettable interface.
 */
public class ResetState implements ActionListener {
    List<Resettable> resettables;

    public ResetState(){
        this.resettables = new ArrayList<>();
    }

    public ResetState(Resettable resettable){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable);
    }

    public ResetState(Resettable resettable1, Resettable resettable2){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable1);
        this.resettables.add(resettable2);
    }

    public ResetState(Resettable resettable1, Resettable resettable2, Resettable resettable3){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable1);
        this.resettables.add(resettable2);
        this.resettables.add(resettable3);
    }

    public ResetState(List<Resettable> resettables) {
        this.resettables = resettables;
    }

    public void addResettable(Resettable resettable){
        this.resettables.add(resettable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Resettable r : this.resettables)
            r.resetState();
    }
}
