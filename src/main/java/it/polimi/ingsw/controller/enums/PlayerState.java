package it.polimi.ingsw.controller.enums;

public enum PlayerState {
    WAITING4NAME,
    DISCONNECTED,
    WAITING4TURN,
    /**
     * When the player's in this state it means all the players are making their beginning of the game decisions (what LeaderCards to
     * discard, what extra resources to choose).
     */
    WAITING4BEGINNINGDECISIONS,
    WAITING4OTHERBEGINNINGDECISIONS,
    PLAYING,
    WAITING4LASTTURN,
    PLAYINGLASTTURN,
    WAITING4GAMEEND,
    WAITING4GAMESTART,
    WAITING4SETNUMPLAYER
}
