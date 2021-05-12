package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

public class GetFromMatrixMessage extends Message{
    private int row, col;
    private List<Integer> leaderList;

    public GetFromMatrixMessage(int row, int col, List<Integer>leaderList) {
        this.row = row;
        this.col = col;
        this. leaderList = leaderList;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<Integer> getLeaderList() {
        return new ArrayList<>(leaderList);
    }
}
