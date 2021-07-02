package it.polimi.ingsw.network.messages.fromClient;

public class CheatMessage extends Message{
    private int numRes;

    public CheatMessage(int numRes){
        this.numRes = numRes;
    }

    public int getNumRes(){return numRes;}
}
