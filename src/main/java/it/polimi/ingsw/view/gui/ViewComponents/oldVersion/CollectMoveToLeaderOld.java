package it.polimi.ingsw.view.gui.ViewComponents.oldVersion;

import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveShelfToLeaderMessage;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop.LeaderCardDrop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@Deprecated
public class CollectMoveToLeaderOld implements ActionListener {
    List<LeaderCardDrop> leaders;

    public CollectMoveToLeaderOld(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LeaderCardDrop leaderCard = this.leaders.stream().filter(x -> x.getShelf() != -1).findAny().orElse(null);

        if(leaderCard == null) {
            //PanelManager.getInstance().printError("You must move at least one resource!");
            JOptionPane.showMessageDialog(null, "You must move at least one resource!");
            return;
        }

        Message message = new MoveShelfToLeaderMessage(leaderCard.getShelf(), leaderCard.getQuantity());
        //PanelManager.getInstance().writeMessage(new Command("moveShelfToLeader", message));
        System.out.println("FROM SHELF " + leaderCard.getShelf() + ", " + leaderCard.getQuantity() + " resources!");
    }
}
