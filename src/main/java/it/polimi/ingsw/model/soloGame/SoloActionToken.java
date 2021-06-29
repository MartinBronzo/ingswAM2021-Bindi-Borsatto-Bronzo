package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represent a generic token. It's an abstract class
 */
public abstract class SoloActionToken {

    /**
     * Plays this token effect
     * @return a boolean representing whether the effect has been played correctly
     * @throws LastVaticanReportException if the Last Vatican Report has been consumed
     * @throws EmptyDevColumnException if there is an empty column in the DevGrid
     */
    public abstract boolean playEffect() throws LastVaticanReportException, EmptyDevColumnException;

    /**
     * Returns the path to this token image
     * @return the path to this token image
     */
    public abstract String getName();
}