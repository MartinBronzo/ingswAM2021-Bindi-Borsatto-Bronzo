package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represent a generic token. It's an abstract class
 */
public abstract class SoloActionToken {
    public abstract boolean playEffect() throws LastVaticanReportException, EmptyDevColumnException;
}