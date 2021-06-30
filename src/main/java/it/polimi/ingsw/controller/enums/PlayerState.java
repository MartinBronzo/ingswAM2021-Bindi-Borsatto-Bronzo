package it.polimi.ingsw.controller.enums;

/**
 * This Enumeration represents the state the player can be found in.
 */
public enum PlayerState {
    /**
     * The ClientHandler is waiting for the player to set their nickname.
     */
    WAITING4NAME,
    /**
     * The player is disconnected.
     */
    DISCONNECTED,
    /**
     * The player is waiting for their turn to arrive.
     */
    WAITING4TURN,
    /**
     * When the player's in this state it means all the players are making their beginning of the game decisions (what LeaderCards to
     * discard, what extra resources to choose).
     */
    WAITING4BEGINNINGDECISIONS,
    /**
     * The player is making their begging of the game decisions.
     */
    PLAYINGBEGINNINGDECISIONS,
    //WAITING4OTHERBEGINNINGDECISIONS,
    /**
     * The player is playing in their turn.
     */
    PLAYING,
    /**
     * The player is waiting for the game to begin.
     */
    WAITING4GAMESTART,
    /**
     * The ClientHandler is waiting for the player who is creating a game to set the number of players they want in their game.
     */
    WAITING4SETNUMPLAYER,
    /**
     * The player is waiting to play their last turn.
     */
    WAITING4LASTTURN,
    /**
     * The player is playing their last turn.
     */
    PLAYINGLASTTURN,
    /**
     * The player is waiting for the game to end (they don't have to play in another turn).
     */
    WAITING4GAMEEND,
}
