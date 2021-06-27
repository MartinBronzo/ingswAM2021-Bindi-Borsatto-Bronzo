package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.DiscardLeaderAndExtraResBeginningMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.CardCheckbox;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This ActionListener will listen to the Confirm button of the Beginning Decisions view
 */
public class CollectBeginningChoices implements ActionListener {
    private DepotDrop depotDrop;
    private CardCheckbox cardCheckbox1, cardCheckbox2;
    private int numLeaders;
    private static ResourceType[] resources = new ResourceType[]{ResourceType.COIN, ResourceType.STONE, ResourceType.SERVANT, ResourceType.SHIELD};

    //used for testing
    @Deprecated
    public CollectBeginningChoices(DepotDrop dragAndDrop) {
        this.depotDrop = dragAndDrop;
    }

    /**
     * Constructs a CollectBeggingChoices ActionListener which will collect the choices the user takes at the beginning of the game
     * @param dragAndDrop a DepotDrop panel where the user can eventually drop some resources
     * @param cardCheckbox1 a CardCheckbox containing two of the four LeaderCards given to the player at the beginning of the game
     * @param cardCheckbox2 a CardCheckbox containing two of the four LeaderCards given to the player at the beginning of the game
     * @param numLeaders the amount of LeaderCard the player is to discard
     */
    public CollectBeginningChoices(DepotDrop dragAndDrop, CardCheckbox cardCheckbox1, CardCheckbox cardCheckbox2, int numLeaders) {
        this.depotDrop = dragAndDrop;
        this.cardCheckbox1 = cardCheckbox1;
        this.cardCheckbox2 = cardCheckbox2;
        this.numLeaders = numLeaders;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked!");
        //Collects the DepotChoices
        //List<ShelfDrop.Pair<Integer, ResourceType>> depotDecisions = this.depotDrop.getDecisions();

        //List<DepotParams> depotInfo = this.getDepotInfoForModel(depotDecisions);
        List<DepotParams> depotInfo = this.depotDrop.getDecisions();

        this.printDecisions(depotInfo);

        //Collects the discarded LeaderCard
        ArrayList<Integer> leaderChoices = new ArrayList<>(cardCheckbox1.getSelectedLeaderIndexes());
        leaderChoices.addAll(cardCheckbox2.getSelectedLeaderIndexes().stream().map(x -> x + 2).collect(Collectors.toList()));
        if (leaderChoices.size() == numLeaders) {
            Message message = new DiscardLeaderAndExtraResBeginningMessage(leaderChoices, depotInfo);
            PanelManager.getInstance().writeMessage(new Command("discardLeaderAndExtraResBeginning", message));

            //Resets the inner state
            this.depotDrop.resetState();
        } else
            PanelManager.getInstance().printError("You must discard 2 Leader Cards");
            //JOptionPane.showMessageDialog(null, "You must discard 2 Leader Cards");
    }

    private void printDecisions(List<DepotParams> dec) {
        for (DepotParams d : dec)
            System.out.println("In " + d.getShelf() + " put " + d.getQt() + " of " + d.getResourceType());
    }

  /*  public static List<DepotParams> getDepotInfoForModel(List<ShelfDrop.Pair<Integer, ResourceType>> depotDecisions) {
        List<ShelfDrop.Pair<Integer, ResourceType>> someChoices;
        int quantity;
        List<DepotParams> result = new ArrayList<>();

        //Gets how many shelves are there
        List<Integer> presentShelves = depotDecisions.stream().map(ShelfDrop.Pair::getKey).distinct().collect(Collectors.toList());

        //For each shelves we blend the many decisions
        for (Integer i : presentShelves) {
            someChoices = depotDecisions.stream().filter(x -> x.getKey().equals(i)).collect(Collectors.toList());
            for (ResourceType r : resources) {
                quantity = (int) someChoices.stream().filter(x -> x.getValue() == r).count();
                if (quantity > 0)
                    result.add(new DepotParams(r, quantity, i));
            }
        }

        return result;
    }
*/

}
