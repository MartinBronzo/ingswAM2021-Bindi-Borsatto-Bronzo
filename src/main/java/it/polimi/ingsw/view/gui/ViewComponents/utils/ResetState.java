package it.polimi.ingsw.view.gui.ViewComponents.utils;

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

    /**
     * Constructs an empty ResetState object (that is, it is not referring to any Resettable object)
     */
    public ResetState(){
        this.resettables = new ArrayList<>();
    }

    /**
     * Constructs a ResetState object which refers the specified Resettable object
     * @param resettable a Resettable object
     */
    public ResetState(Resettable resettable){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable);
    }

    /**
     * Constructs a ResetState object which refers the two specified Resettable objects
     * @param resettable1 a Resettable object
     * @param resettable2 a Resettable object
     */
    public ResetState(Resettable resettable1, Resettable resettable2){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable1);
        this.resettables.add(resettable2);
    }

    /**
     * Constructs a ResetState object which refers the three specified Resettable objects
     * @param resettable1 a Resettable object
     * @param resettable2 a Resettable object
     * @param resettable3 a Resettable object
     */
    public ResetState(Resettable resettable1, Resettable resettable2, Resettable resettable3){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable1);
        this.resettables.add(resettable2);
        this.resettables.add(resettable3);
    }

    /**
     * Constructs a ResetState object which refers the three specified Resettable objects
     * @param resettable1 a Resettable object
     * @param resettable2 a Resettable object
     * @param resettable3 a Resettable object
     * @param resettable4 a Resettable object
     */
    public ResetState(Resettable resettable1, Resettable resettable2, Resettable resettable3, Resettable resettable4){
        this.resettables = new ArrayList<>();
        this.resettables.add(resettable1);
        this.resettables.add(resettable2);
        this.resettables.add(resettable3);
        this.resettables.add(resettable4);
    }

    /**
     * Makes this object to refers to the specified list of resettable objects
     * @param resettables a list of Resettable objects
     */
    public ResetState(List<Resettable> resettables) {
        this.resettables = new ArrayList<>(resettables);
    }

    /**
     * Makes this object to refer also to the specified Resettable object
     * @param resettable a Resettable object
     */
    public void addResettable(Resettable resettable){
        this.resettables.add(resettable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Resettable r : this.resettables)
            r.resetState();
    }
}
