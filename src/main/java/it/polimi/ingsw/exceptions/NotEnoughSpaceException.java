package it.polimi.ingsw.exceptions;

public class NotEnoughSpaceException extends Exception {
    int availableSpace;

    public NotEnoughSpaceException(String s, int remainingSpace) {
        super(s);
        this.availableSpace = remainingSpace;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }
}
