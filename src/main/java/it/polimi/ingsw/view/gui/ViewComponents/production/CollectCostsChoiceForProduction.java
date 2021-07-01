package it.polimi.ingsw.view.gui.ViewComponents.production;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.ActivateProductionMessage;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.PanelDrop;
import it.polimi.ingsw.view.gui.ViewComponents.utils.CollectCostsChoiceAbstract;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ActionListener will listen to the Confirm button of the ProductionGetResources view
 */
public class CollectCostsChoiceForProduction extends CollectCostsChoiceAbstract {
    private List<Integer> devCards;
    private Map<Integer, JComboBox> leadersOutput;
    private BaseProductionParams baseProduction;

    /**
     * Constructs a CollectCostsChoiceForProduction ActionListener
     * @param panelDrop the panel where the player drops the resources they want to use as inputs for the production
     * @param devCards the list of indexes of the player's DevCards whose production method the player wants to use
     * @param leadersOutput the JComboBoxes used to specify the extra output the player gets to choose when they choose to use the LeaderCards production methods
     *                      The indexes of the ExtraProduction LeaderCards the player wants to use are matched with their corresponding JComboBox
     * @param baseProduction the BaseProduction params which represent the player's choice
     */
    public CollectCostsChoiceForProduction(PanelDrop panelDrop, List<Integer> devCards, Map<Integer, JComboBox> leadersOutput, BaseProductionParams baseProduction){
        super.panelDrop = panelDrop;
        this.devCards = devCards;
        this.leadersOutput = leadersOutput;
        this.baseProduction = baseProduction;
    }

    @Override
    protected void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        //Gets the information from the ComboBoxes
        HashMap<Integer, ResourceType> leaders = this.getLeadersOutputInfo();

        this.printExtraInfo(leaders);

        Message message = new ActivateProductionMessage(devCards, leaders, baseProduction, fromDepot, fromLeaderCards, fromStrongBox);
        PanelManager.getInstance().writeMessage(new Command("activateProductionMessage", message));

    }

    private void printExtraInfo(HashMap<Integer, ResourceType> leaders) {
        System.out.println("ACTIVATE THE FOLLOWING DEVCARS:");
        for(Integer i : this.devCards)
            System.out.println(i);
        System.out.println("BASE PROD: " + baseProduction.isActivated() + "\nINPUT:");
        for(ResourceType r : baseProduction.getBaseInput())
            System.out.println(r);
        System.out.println("OUTPUT:");
        for(ResourceType r : baseProduction.getBaseOutput())
            System.out.println(r);
        System.out.println("LEADERS PROD: ");
        for(Map.Entry<Integer, ResourceType> l : leaders.entrySet())
            System.out.println("leader: " + l.getKey() + ", output: " + l.getValue());
    }

    private HashMap<Integer, ResourceType> getLeadersOutputInfo() {
        HashMap<Integer, ResourceType> result = new HashMap<>();

        for(Map.Entry<Integer, JComboBox> e : this.leadersOutput.entrySet())
            result.put(e.getKey(), ResourceType.valueOf(e.getValue().getSelectedItem().toString()));

        return result;
    }
}
