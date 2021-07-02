package it.polimi.ingsw.view.cli;

/**
 * This Enumeration contains the commands that the CliClient can make.
 */
public enum CliCommandType {
    /**
     * The player wants to quit the game.
     */
    QUIT,
    /**
     * The player wants to set their nickname.
     */
    SETNICKNAME,
    /**
     * The player wants to the number of players in the game they are creating.
     */
    SETNUMOFPLAYERS,
    /**
     * The player wants to make their begging of the game decisions.
     */
    CONFIGURESTART,
    /**
     * The player wants to know what resources they would get if they were to buy from a column or row of the Market.
     */
    GETRESOURCESFROMMARKET,
    /**
     * The player wants to buy from the Market.
     */
    BUYFROMMARKET,
    /**
     * The player wants to know what resources they would need to pay if they were to buy a DevCard from the DevGrid.
     */
    GETDEVCARDCOST,
    /**
     * The player wants to buy a DevCard.
     */
    BUYDEVCARD,
    /**
     * The player wants to know what resources they would need to use if they were to activate some production powers.
     */
    GETPRODUCTIONCOST,
    /**
     * The player wants to activate some production powers.
     */
    ACTIVATEPRODUCTION,
    /**
     * The player wants to discard a LeaderCard during a game.
     */
    DISCARDLEADER,
    /**
     * The player wants to activate a LeaderCard during a game.
     */
    ACTIVATELEADER,
    /**
     * The player wants to move resources between Depot shelves.
     */
    MOVEBETWEENSHELF,
    /**
     * The player wants to move resources from an ExtraSlot LeaderCard to a Depot shelf.
     */
    MOVELEADERTOSHELF,
    /**
     * The player wants to move resources from a Depot shelf to an ExtraSlot LeaderCard.
     */
    MOVESHELFTOLEADER,
    /**
     * The player wants to see an opponent's PlayerBoard.
     */
    SEEPLAYERBOARD,
    /**
     * The player wants to see their PlayerBoard.
     */
    PRINTMYBOARD,
    /**
     * The player wants to end their turn.
     */
    ENDTURN,
    /**
     * The player wants to their opponents' names.
     */
    SEEOTHERSPLAYERSNAMES,
    /**
     * The player wants to print the help message.
     */
    HELP,
    /**
     * The player wants to print the holp message.
     */
    HOLP,
    /**
     * The player wants to cheat
     */
    CHEAT
}
