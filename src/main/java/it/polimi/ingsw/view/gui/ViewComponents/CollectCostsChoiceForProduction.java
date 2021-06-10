package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.BaseProduction;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.ActivateProductionMessage;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectCostsChoiceForProduction extends CollectCostsChoiceAbstract{
    private List<Integer> devCards;
    private Map<Integer, JComboBox> leadersOutput;
    private BaseProductionParams baseProduction;

    public CollectCostsChoiceForProduction(PanelDrop panelDrop, List<Integer> devCards, Map<Integer, JComboBox> leadersOutput, BaseProductionParams baseProduction){
        super.panelDrop = panelDrop;
        this.devCards = devCards;
        this.leadersOutput = leadersOutput;
        this.baseProduction = baseProduction;
    }



    @Override
    protected void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        //TODO: implementare qui il mandare il messaggio per il costo della devGrid

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
