package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.InfiniteResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel contains all the elements for the BaseProduction.
 */
public class DragAndDropBaseProd extends JPanel implements Resettable {
    private InfiniteResourcesDrag infiniteResourcesDrag;
    private List<MyDropTargetListener> targetListeners;
    private BaseProdPanel input1;
    private BaseProdPanel input2;
    private BaseProdPanel output;

    /**
     * Constructs a DragAndDropBaseProd where the player can drag and drop the resources they want to use for the BaseProduction. Before
     * being able to use this object, the user of this class must specify the DropChecker function used for this panel
     */
    public DragAndDropBaseProd(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.targetListeners = new ArrayList<>();

        infiniteResourcesDrag = new InfiniteResourcesDrag();
        this.add(infiniteResourcesDrag);

        //panel that contains all the components for the base production
        JPanel prodPanel = new JPanel();
        prodPanel.setLayout(new BoxLayout(prodPanel, BoxLayout.LINE_AXIS));

        //panel that contains the inputs of the base production
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        input1 = new BaseProdPanel();
        this.targetListeners.add(new MyDropTargetListener(input1, new RegisterBaseProdDrop(input1)));//this must be done or we wont be able to drop any image onto the empty panel
        inputPanel.add(input1);
        inputPanel.add(new JLabel("Input"));

        inputPanel.add(Box.createRigidArea(new Dimension(0,30)));

        input2 = new BaseProdPanel();
        this.targetListeners.add(new MyDropTargetListener(input2, new RegisterBaseProdDrop(input2)));//this must be done or we wont be able to drop any image onto the empty panel
        inputPanel.add(input2);
        inputPanel.add(new JLabel("Input"));

        prodPanel.add(inputPanel);
        prodPanel.add(Box.createRigidArea(new Dimension(20,0)));
        prodPanel.add(new JLabel(new ImageIcon(getClass().getResource("/brackets.png"))));
        prodPanel.add(Box.createRigidArea(new Dimension(20,0)));

        output = new BaseProdPanel();
        this.targetListeners.add(new MyDropTargetListener(output, new RegisterBaseProdDrop(output)));//this must be done or we wont be able to drop any image onto the empty panel

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.PAGE_AXIS));

        outputPanel.add(output);
        outputPanel.add(new JLabel("Output"));

        prodPanel.add(outputPanel);

        this.add(prodPanel);
    }

    /**
     * Returns the resources the player wants to use as inputs for the BaseProduction
     * @return the resources the player wants to use as inputs for the BaseProduction
     */
    public List<ResourceType> getInputs(){
        List<ResourceType> inputList = new ArrayList<>();
        inputList.add(input1.getResource());
        inputList.add(input2.getResource());

        return inputList;
    }

    /**
     * Returns whether the player wants to actually use the BaseProduction
     * @return true if the player wants to use the BaseProduction (that is, if all the inputs and output have been specified
     * by the player), false otherwise
     */
    public boolean isActivated(){
        return (input1.getResource() != null && input2.getResource() != null && output.getResource()!= null);
    }

    /**
     * Returns the resource the player wants to use as output for the BaseProduction
     * @return the resource the player wants to use as output for the BaseProduction
     */
    public ResourceType getOutput(){
        return output.getResource();
    }

    /**
     * Returns the resource the player wants to use as output for the BaseProduction in a list
     * @return the resource the player wants to use as output for the BaseProduction in a list
     */
    public List<ResourceType> getOutputsList(){
        List<ResourceType> outputList = new ArrayList<>();
        outputList.add(output.getResource());
        return outputList;
    }

    /**
     * Sets the DropChecker function used for this panel
     * @param checkDropFunction the DropChecker function used for this panel
     */
    public void setCheckDropFunction(DropChecker checkDropFunction){
        for(MyDropTargetListener listener : this.targetListeners)
            listener.setCheckDrop(checkDropFunction);
    }

    @Override
    public void resetState() {
        input1.resetState();
        input2.resetState();
        output.resetState();
    }
}
