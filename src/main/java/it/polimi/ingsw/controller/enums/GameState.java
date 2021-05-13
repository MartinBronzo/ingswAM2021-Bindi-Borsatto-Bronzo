package it.polimi.ingsw.controller.enums;


public enum GameState {
    CONFIGURING,
    /**
     * The game has reached the exact number of players and its waiting for all the players to send the LeaderCards they want to discard
     * and the extra resource they want to store.
     */
    STARTED,
    /**
     * After all the players have sent all the beginning infos they need to, then the game is in session, that is the normal flow of turns is followed.
     */
    INSESSION,
    WAITING4PLAYERS,
    LASTTURN,
    ENDED
}
