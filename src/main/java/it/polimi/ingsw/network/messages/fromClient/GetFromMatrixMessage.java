package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is used to specify a choice made onto an object with a matrix structure (the Market or the DevGrid).
 */
public class GetFromMatrixMessage extends Message {
    private int row, col;
    private List<Integer> leaderList;

    /**
     * Constructs a GetFromMatrixMessage
     * @param row the selected row of the object at issue
     * @param col the selected column of the object at issue
     * @param leaderList the list of indexes of the LeaderCards whose effect the player wants to use
     */
    public GetFromMatrixMessage(int row, int col, List<Integer> leaderList) {
        this.row = row;
        this.col = col;
        this.leaderList = leaderList;
    }

    /**
     * Returns the selected row of the object at issue
     * @return the selected row of the object at issue
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the selected column of the object at issue
     * @return the selected column of the object at issue
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the list of indexes of the LeaderCards whose effect the player wants to use
     * @return the list of indexes of the LeaderCards whose effect the player wants to use
     */
    public List<Integer> getLeaderList() {
        return new ArrayList<>(leaderList);
    }
}
