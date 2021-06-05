package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.DiscardLeaderAndExtraResBeginningMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveShelfToLeaderMessage;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CollectMoveToLeader implements ActionListener {
    List<LeaderCardDrop> leaders;

    public CollectMoveToLeader(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LeaderCardDrop leaderCard = this.leaders.stream().filter(x -> x.getShelf() != -1).findAny().orElse(null);

        if(leaderCard == null) {
            //PanelManager.getInstance().printError("You must move at least one resource!");
            //TODO: perché non funziona con il panel manager?
            JOptionPane.showMessageDialog(null, "You must move at least one resource!");
            return;
        }

        Message message = new MoveShelfToLeaderMessage(leaderCard.getShelf(), leaderCard.getQuantity());
        //PanelManager.getInstance().writeMessage(new Command("moveShelfToLeader", message));
        System.out.println("FROM SHELF " + leaderCard.getShelf() + ", " + leaderCard.getQuantity() + " resources!");

        //TODO: reset the state
    }
}
