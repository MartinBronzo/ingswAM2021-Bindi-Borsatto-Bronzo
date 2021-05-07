package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;

import java.util.List;

public class GetFromMatrixMessage {
    private int row, col;
    private List<Integer> leaderList;

    public GetFromMatrixMessage(int row, int col, List<Integer>leaderList) {
        this.row = row;
        this.col = col;
        this. leaderList = leaderList;
    }
}
