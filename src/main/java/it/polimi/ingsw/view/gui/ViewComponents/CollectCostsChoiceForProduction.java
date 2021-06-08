package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;

import java.util.HashMap;
import java.util.List;

public class CollectCostsChoiceForProduction extends CollectCostsChoiceAbstract{
    @Override
    protected void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        //TODO: implementare qui il mandare il messaggio per il costo della devGrid
    }
}
