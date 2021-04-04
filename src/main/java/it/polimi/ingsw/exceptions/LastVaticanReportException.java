package it.polimi.ingsw.exceptions;

public class LastVaticanReportException extends Exception{
    private boolean lastValueReturnedByCell;

    public LastVaticanReportException(String s, boolean b){
        this.lastValueReturnedByCell = b;
    }

    public boolean getLastValue(){
        return this.lastValueReturnedByCell;
    }
}
