package it.polimi.ingsw.view.gui.ViewComponents.oldVersion;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.PanelDrop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
//TODO: questa classe sarà da togliere quando avremo tutto pronto perché solo serve per testing purposes
public class CollectCostsChoices implements ActionListener {
    private PanelDrop panelDrop;

    public CollectCostsChoices(PanelDrop panelDrop) {
        this.panelDrop = panelDrop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked!");

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

        System.out.println("FROM DEPOT: ");
        for(DepotParams d: fromDepot)
            System.out.println("From " + d.getShelf() + ",  " + d.getQt() + " " + d.getResourceType());

        System.out.println("FROM STRONGBOX: ");
        for(Map.Entry<ResourceType, Integer> el: fromStrongBox.entrySet())
            System.out.println(el.getValue() + " " + el.getKey());

        System.out.println("FROM LEADERS: ");
        for(Map.Entry<ResourceType, Integer> el: fromLeaderCards.entrySet())
            System.out.println(el.getValue() + " " + el.getKey());

        //TODO: ci sarà da creare messaggio ad hoc per i vari casi: magari fare un collect cost per card e per prod oppure fare diventare
        //questa classe un wrapper che contiene una classe ad hoc che differisce per il tipo di messaggio

    }
}
