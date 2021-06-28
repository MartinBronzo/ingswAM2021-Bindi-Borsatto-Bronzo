package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This enumeration represents all the possible messages that the GameController sends back to the players.
 */
public enum ResponseType {
    PING,
    UPDATE,
    ERROR,
    EXTRARESOURCEANDLEADERCARDBEGINNING,
    HASHMAPRESOURCESFROMDEVGRID,
    HASHMAPRESOURCESFROMMARKET,
    HASHMAPRESOURCESFROMPRODCOST,
    INFOSTRING,
    ASKFORNUMPLAYERS,
    KICKEDOUT,
    TURNINFO,
    SETNICK,
    SETBEGINNINGDECISIONS,
    FINALSCORES,
    SETNUMPLAYERCONF,
    LORENZOSACTION,
    PLAYERCONNECTIONUPDATE,
    SOLOGAMERESULT
}
