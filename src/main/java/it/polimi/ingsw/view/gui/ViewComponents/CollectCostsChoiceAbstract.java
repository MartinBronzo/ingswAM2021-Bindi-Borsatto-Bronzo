package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CollectCostsChoiceAbstract implements ActionListener {
    private PanelDrop panelDrop;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Controls whether the player has specified all the resources they are supposed to
        if(!panelDrop.hasPlayerSpecifiedEverything()) {
            System.out.println(panelDrop.hasPlayerSpecifiedEverything());
            //PanelManager.getInstance().printError("You must select all the resources you are supposed to!");
            //TODO: perché non va con il printError?
            JOptionPane.showMessageDialog(null, "You must select all the resources you are supposed to!");
            return;
        }

        //Collects the resources drawn from the Depot
        List<DepotParams> fromDepot = panelDrop.getFromDepot();

        //Collects the resources drawn from the StrongBox
        HashMap<ResourceType, Integer> fromStrongBox = panelDrop.getFromStrongBox();

        //Collects the resources drawn from the LeaderCards
        HashMap<ResourceType, Integer> fromLeaderCards = panelDrop.getFromLeaders();

        this.print(fromDepot, fromStrongBox, fromLeaderCards);

        this.sendMessage(fromDepot, fromStrongBox, fromLeaderCards);

    }

    protected abstract void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards);

    protected void print(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        System.out.println("FROM DEPOT: ");
        for(DepotParams d: fromDepot)
            System.out.println("From " + d.getShelf() + ",  " + d.getQt() + " " + d.getResourceType());

        System.out.println("FROM STRONGBOX: ");
        for(Map.Entry<ResourceType, Integer> el: fromStrongBox.entrySet())
            System.out.println(el.getValue() + " " + el.getKey());

        System.out.println("FROM LEADERS: ");
        for(Map.Entry<ResourceType, Integer> el: fromLeaderCards.entrySet())
            System.out.println(el.getValue() + " " + el.getKey());
        
    }


}
