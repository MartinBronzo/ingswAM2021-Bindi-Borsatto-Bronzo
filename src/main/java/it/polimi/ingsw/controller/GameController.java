package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.board.MainBoard;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.model.soloGame.SoloBoard;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.lightModel.Board;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


public class GameController {
    private ClientHandler activePlayer;
    private MainBoard mainBoard;
    private List<Pair<ClientHandler, PlayerBoard>> players;
    private int numberOfPlayers;
    private int numOfTurn;
    private int maxPlayersNum;
    private GameState state;
    private MainBoard modelCopy;
    private int firstPlayer;
    private Integer howManyPlayersReady;
    private Timer turnTimer;
    private boolean timerElapsed, timerStarted;
    /**
     * List of all the disconnected players who have not specified their beginning decisions
     */
    private List<ClientHandler> disconnectedBeforeStarting;
    /**
     * List of all the partial copy of the ClientHandlers in the game
     */
    private List<ClientHandler> clientHandlersCopy;
    private GameState gameStateCopy;


    /**
     * This class represents the relationship between the ClientHandler of the player and the their PlayerBoard.
     *
     * @param <clientHandler> the ClientHandler of the player
     * @param <playerBoard>   the PlayerBoard of the player
     */
    class Pair<clientHandler, playerBoard> {
        private clientHandler l;
        private playerBoard r;

        public Pair(clientHandler l, playerBoard r) {
            this.l = l;
            this.r = r;
        }

        public clientHandler getKey() {
            return l;
        }

        public playerBoard getValue() {
            return r;
        }

        public void setKey(clientHandler l) {
            this.l = l;
        }

        public void setValue(playerBoard r) {
            this.r = r;
        }
    }

    /*
    ###########################################################################################################
     GENERAL SETTERS
    ###########################################################################################################
     */

    /**
     * Returns the GameState of the game at the moment this method is called
     *
     * @return the GameState the game is in
     */
    public GameState getState() {
        return this.state;
    }

    /**
     * Changes the GameState of the game to the specified value
     *
     * @param state the new GameState the game will be in
     */
    public synchronized void setState(GameState state) {
        this.state = state;
    }

    /**
     * Substitutes a ClientHandler with the one specified. This method is used when a player loses their connection and they reconnect to the server (the new
     * ClientHandler they get is the one specified as a parameter) and their PlayerState value is based on the actual state of the game and the state at which the client disconnected
     *
     * @param newClientHandler the ClientHandler which is going to substitute the one ClientHandler present in the game which has the same nickname as the one stored inside the
     *                         specified object
     * @return true if the substitution took place correctly, false if there was no substitution because the client associated with the specified ClientHandler is not in this game
     */
    public synchronized boolean substitutesClient(ClientHandler newClientHandler) throws IllegalActionException {
        //Case gameState == STARTED when disconnection happend
        //If the player was adding the resources and discarding the leaderCards at the beginning of the game, before disconnecting

        ////TODO: se lui è prima dell'active player deve essere a gameend (NOT TESTED)
        if (state == GameState.LASTTURN)
            if (this.disconnectedBeforeStarting.contains(newClientHandler)) {
                for (Pair<ClientHandler, PlayerBoard> e : players)
                    if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                        if (getPlayerPositionInTurn(e.getKey()) < getPlayerPositionInTurn(activePlayer))
                            newClientHandler.setPlayerState(PlayerState.WAITING4GAMEEND);
                        else
                            newClientHandler.setPlayerState(PlayerState.WAITING4LASTTURN);
                        e.setKey(newClientHandler);
                        newClientHandler.send(new GeneralInfoStringMessage("You are back in the game!"));

                        newClientHandler.send(this.getWholeMessageUpdateToClient());
                        updatesAfterDisconnection(newClientHandler);
                        return true;
                    }
            }

        if (state != GameState.WAITING4PLAYERS)
            for (ClientHandler ch : disconnectedBeforeStarting) {
                if (ch.getNickname().equals(newClientHandler.getNickname())) {
                    newClientHandler.setPlayerState(PlayerState.WAITING4BEGINNINGDECISIONS);
                    for (Pair<ClientHandler, PlayerBoard> e : players)
                        if (e.getKey().getNickname().equals(newClientHandler.getNickname()))
                            e.setKey(newClientHandler);

                    newClientHandler.send(this.getWholeMessageUpdateToClient());
                    this.sendNumExtraResBeginningToDisconnectedPlayer(newClientHandler);
                    //TODO:QUESTO NON DOVREBBE PIù SERVIRE
                    //updates all the other clients that tha player has reconnected
                    //updatesAfterDisconnection(newClientHandler);
                    return true;
                }
            }

        switch (state) {
            case INSESSION:
                for (Pair<ClientHandler, PlayerBoard> e : players)
                    if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                        newClientHandler.setPlayerState(PlayerState.WAITING4TURN);
                        e.setKey(newClientHandler);
                        newClientHandler.send(new GeneralInfoStringMessage("You are back in the game!"));
                        newClientHandler.send(this.getWholeMessageUpdateToClient());
                        updatesAfterDisconnection(newClientHandler);
                        return true;
                    }
                break;

            //TODO: CASE NOT TESTED
            case WAITING4PLAYERS:
                for (Pair<ClientHandler, PlayerBoard> e : players)
                    if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                        newClientHandler.setPlayerState(PlayerState.WAITING4GAMESTART);
                        if (this.disconnectedBeforeStarting.contains(newClientHandler))
                            this.disconnectedBeforeStarting.remove(newClientHandler);
                        e.setKey(newClientHandler);
                        newClientHandler.send(new GeneralInfoStringMessage("You are back in the game!"));
                        newClientHandler.send(this.getWholeMessageUpdateToClient());
                        updatesAfterDisconnection(newClientHandler);
                        return true;
                    }
                break;

            case LASTTURN:
                boolean willPlay = willPlayInThisTurn(newClientHandler);
                if (willPlay)
                    newClientHandler.setPlayerState(PlayerState.WAITING4LASTTURN);
                else
                    newClientHandler.setPlayerState(PlayerState.WAITING4GAMEEND);

                for (Pair<ClientHandler, PlayerBoard> e : players)
                    if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                        e.setKey(newClientHandler);
                        newClientHandler.send(new GeneralInfoStringMessage("You are back in the game!"));
                        newClientHandler.send(this.getWholeMessageUpdateToClient());
                        updatesAfterDisconnection(newClientHandler);
                        return true;
                    }
                break;
        }


        /*for (Pair<ClientHandler, PlayerBoard> e : players) {
            if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                newClientHandler.setPlayerState(PlayerState.WAITING4TURN);
                e.setKey(newClientHandler);
                return true;
            }
        }*/
        return false;
    }

    //public only for testing purposes
    public boolean willPlayInThisTurn(ClientHandler player) {
        int activePlayerPosition, newPlayerPosition, firstPlayerPosition;
        activePlayerPosition = getPlayerPositionInTurn(activePlayer);
        newPlayerPosition = getPlayerPositionInTurn(player);
        if (activePlayerPosition == -1 || newPlayerPosition == -1) {
            throw new IllegalArgumentException("Couldn't find the player in the game");
        }

        return newPlayerPosition >= activePlayerPosition;

        /*if (newPlayerPosition == firstPlayerPosition && activePlayerPosition != firstPlayerPosition) {
            return false;
        } else {
            //checks if the reconnected player lost his last turn or if will play it
            if (activePlayerPosition == firstPlayerPosition)
                return true;
                //newClientHandler.setPlayerState(PlayerState.WAITING4LASTTURN);
            else {
                //newClientHandler.setPlayerState(PlayerState.WAITING4LASTTURN);
                if (activePlayerPosition > firstPlayerPosition) {
                    //newClientHandler.setPlayerState(PlayerState.WAITING4LASTTURN);
                    return newPlayerPosition > activePlayerPosition || newPlayerPosition < firstPlayerPosition;
                    //newClientHandler.setPlayerState(PlayerState.WAITING4GAMEEND);
                } else //activePlayerPosition < firstPlayer
                    return newPlayerPosition > activePlayerPosition && newPlayerPosition < firstPlayerPosition;
                //newClientHandler.setPlayerState(PlayerState.WAITING4GAMEEND);
            }
        }*/
    }

    /*
    ###########################################################################################################
     BEGINNING OF THE GAME METHODS
    ###########################################################################################################
     */

    /**
     * Constructs an empty GameController, without anything set yet.
     */
    public GameController() {
        this.activePlayer = null;
        this.mainBoard = null;
        this.players = new ArrayList<>();
        this.numberOfPlayers = -1;
        this.maxPlayersNum = 4;
        this.howManyPlayersReady = 0;
        this.disconnectedBeforeStarting = new ArrayList<>();
        this.timerElapsed = false;
        this.timerStarted = false;
        numOfTurn = 0;
    }

    /**
     * Creates the MainBoard for this game with as many players as the parameter specifies
     *
     * @param numberOfPlayers the number of players for this game
     */
    public void startMainBoard(int numberOfPlayers) {
        if (numberOfPlayers <= 0 || numberOfPlayers > this.maxPlayersNum)
            throw new IllegalArgumentException("The number of players must be a number between 1 and " + this.maxPlayersNum + " included!");
        if (this.numberOfPlayers > 0)
            return;
        if (numberOfPlayers == 1) {
            try {
                this.mainBoard = new SoloBoard();
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
                System.exit(1); //FATAL ERROR
            }
        } else {
            try {
                this.mainBoard = new MainBoard(numberOfPlayers);
            } catch (Exception e) {
                //System.out.println("Can't create the MainBoard because there are problems with the configuration files!");
                e.printStackTrace();
                System.exit(1); //FATAL ERROR
            }
        }
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Adds a player represented by the specified ClientHandler to the game if the player hasn't been added, yet, and if the game hasn't reach its maximum
     * capacity, yet.
     *
     * @param player the ClientHandler of the player to be added at the game
     */
    @Deprecated
    public boolean setPlayerOld(ClientHandler player) throws IllegalActionException {
        //We can't add more players than the one given by the numberOfPlayers number
        if (this.players.size() == this.numberOfPlayers)
            throw new IllegalActionException("You can't be added to this game!");
        //We can't add an already added player
        if (this.findClientHandler(player))
            //if (this.getPlayerBoardOfPlayer(player) != null)
            return false;
        PlayerBoard playerBoard = this.mainBoard.getPlayerBoard(this.players.size());
        players.add(new Pair<>(player, playerBoard));
        //We added the last player: the game must begin
        this.state = GameState.STARTED;
        return true;
    }

    /**
     * Adds a player represented by the specified ClientHandler to the game if the player hasn't been added, yet, and if the game hasn't reach its maximum
     * capacity, yet. If the this method is adding the last player this game can hold then it starts the game itselft.
     *
     * @param player the ClientHandler of the player to be added at the game
     */
    public boolean setPlayer(ClientHandler player) throws IllegalActionException {
        //We can't add more players than the one given by the numberOfPlayers number
        if (this.players.size() == this.numberOfPlayers)
            throw new IllegalActionException("You can't be added to this game!");
        //We can't add an already added player
        if (this.findClientHandler(player))
            //if (this.getPlayerBoardOfPlayer(player) != null)
            return false;
        PlayerBoard playerBoard = this.mainBoard.getPlayerBoard(this.players.size());
        players.add(new Pair<>(player, playerBoard));
        if (numberOfPlayers > 1)
            player.send(new GeneralInfoStringMessage("You are in Game! You'll soon start play with others!"));
        //We added the last player: the game must begin
        player.setPlayerState(PlayerState.WAITING4GAMESTART);
        if (players.size() == this.numberOfPlayers)
            this.startGame();
        return true;
    }

    private synchronized void startGame() {
        this.state = GameState.STARTED;
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey().getPlayerState() != PlayerState.DISCONNECTED)
                e.getKey().setPlayerState(PlayerState.WAITING4BEGINNINGDECISIONS);
        this.sendBroadcastUpdate(new GeneralInfoStringMessage("GAME STARTED!"));
        this.showLeaderCardAtBeginning();
        this.sendNumExtraResBeginning();
        //this.state = GameState.INSESSION;//This player is the last one who's adding stuff so the players can play in turns
        this.updatesTurnAndSendInfo(this.firstPlayer);

        /*for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey().getPlayerState() != PlayerState.DISCONNECTED)
                if (e.getKey().getPlayerState() == PlayerState.PLAYINGBEGINNINGDECISIONS)
                    e.getKey().send(new GeneralInfoStringMessage("You're the first player"));
                else
                    e.getKey().send(new GeneralInfoStringMessage("Wait your turn"));*/


        if (numberOfPlayers != 1) //starts timer only if we are in multiplayer
            startTurnTimer();
    }

    private void startTurnTimer() {
        timerElapsed = false;
        timerStarted = true;
        turnTimer = new Timer();
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerElapsed = true;
                timerStarted = false;
                activePlayer.send(new ErrorMessage("Turn timer elapsed; Your turn is ended"));
                specifyNextPlayer(activePlayer);
            }
        }, 120000); //TODO: DECIDERE QUANTO FAR DURARE IL TURNO
    }

    /*private void checkIfGameMustBegin() {
        synchronized (this.howManyPlayersReady) {
            //TODO: fare un lock unico e metterlo anche dall'altra parte
            synchronized (this.disconnectedBeforeStarting) {
                //We may end up here if a player send their beginning decisions after the game has become INSESSION
                if (!(this.state.equals(GameState.STARTED)))
                    return;
                this.howManyPlayersReady++;
                if (this.howManyPlayersReady + this.disconnectedBeforeStarting.size() == this.numberOfPlayers) {
                    this.state = GameState.INSESSION;//This player is the last one who's adding stuff so the players can play in turns
                    this.sendBroadcastUpdate(new GeneralInfoStringMessage("Now turns will begin!"));
                    this.updatesTurnAndSendInfo(this.firstPlayer);
                }
            }
        }
    }*/

    /**
     * Communicates to the GameController that the specified player (represented by their ClientHandler) has been found disconnected before they were able to
     * specify their beginning decisions
     *
     * @param disconnectedPlayer the player who disconnected before taking their beginning decisions
     */
    public synchronized void registerPlayerDisconnectionBeforeStarting(ClientHandler disconnectedPlayer) {
        if (this.disconnectedBeforeStarting.contains(disconnectedPlayer))
            return;
        this.disconnectedBeforeStarting.add(disconnectedPlayer);
    }

    /**
     * Changes the player's state after someone ends their turn
     *
     * @param currentPlayer the player who is ending their turn
     */
    public synchronized void specifyNextPlayer(ClientHandler currentPlayer) throws IllegalStateException {
        numOfTurn++;

        //checks if the timer has already elapsed
        if (!timerElapsed && timerStarted) {
            turnTimer.cancel();
            timerStarted = false;
        }

        //Retrieves this player's index
        int index = this.getPlayerNumber(currentPlayer);
        if (index < 0)
            throw new IllegalArgumentException("The specified ClientHandler isn't in this game!");
        //The next active player is the next one in the list of players and the list must be cyclically covered
        index++;
        if (index == this.numberOfPlayers)
            index = 0;
        if (players.get(index).getKey().getPlayerState() == PlayerState.DISCONNECTED) {
            int tmp = index;
            index++;
            if (index == this.numberOfPlayers)
                index = 0;
            while (players.get(index).getKey().getPlayerState() == PlayerState.DISCONNECTED && index != tmp) {
                index++;
                if (index == this.numberOfPlayers)
                    index = 0;
            }
            if (index == tmp)
                //Then we have gone through the whole list without finding any player who wasn't active
                //TODO: chiudiamo tutto (al massimo mettere uno stato che dice gioco è morto e nella substitue non aggiungiamo giocatori a giochi morti)
                GamesManagerSingleton.getInstance().deleteGame(this);
        }

        /*if(players.get(index).getKey().getPlayerState() == PlayerState.WBD _ )
            //send le informazioni particolari
        */
        activePlayer = players.get(index).getKey();
        //activePlayer.send(new GeneralInfoStringMessage("Now it's Your turn, Master " + activePlayer.getNickname()));
        this.updatesTurnAndSendInfo(index);

        //starts timer for new player
        startTurnTimer();
    }

    /*
    ###########################################################################################################
     GENERAL GETTERS
    ###########################################################################################################
     */

    /**
     * Checks if the specified player has discarded all the LeaderCards they are supposed to
     *
     * @param player a player in the game
     * @return true if the player has discarded all the leader cards they are supposed to, false otherwise
     * @throws IllegalArgumentException if the specified player isn't in this game
     */
    public boolean checkIfPlayerDiscardedLeaderCards(ClientHandler player) throws IllegalArgumentException {
        return this.mainBoard.checkIfPlayerDiscardedCards(this.getPlayerBoardOfPlayer(player));
    }

    /**
     * Returns the index the specified player (represented by their ClientHandler) has in the list of players of this game
     *
     * @param player the player whose index must be find
     * @return the index of the player if the player belongs to this game, -1 otherwise
     */
    private int getPlayerNumber(ClientHandler player) {
        for (int i = 0; i < this.numberOfPlayers; i++)
            if (players.get(i).getKey() == player)
                return i;
        return -1;
    }

    //this method should be private but for testing purpose is public
    public int getPlayerPositionInTurn(ClientHandler clientHandler) {
        int num = 1;
        for (int i = firstPlayer; i < players.size(); i++) {
            if (players.get(i).getKey().getNickname().equals(clientHandler.getNickname())) {
                return num;
            }
            num++;
        }

        for (int i = 0; i < firstPlayer; i++) {
            if (players.get(i).getKey().getNickname().equals(clientHandler.getNickname())) {
                return num;
            }
            num++;
        }
        return -1;
    }

    /**
     * Returns a list of all the players that are added to the game in the moment this method is invoked
     *
     * @return a list of the player's ClientHandler added to the game
     */
    public synchronized List<ClientHandler> getPlayersList() {
        List<ClientHandler> result = new ArrayList<>();
        for (Pair<ClientHandler, PlayerBoard> entry : players)
            result.add(entry.getKey());
        return result;
    }

    /**
     * Returns the number of players this game can hold
     *
     * @return the number of players for this game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }


    /**
     * Returns the reference to the player's PlayerBoard. The player is determined by the specified ClientHandler
     *
     * @param clientHandler the ClientHandler of the player
     * @return the PlayerBoard of the player if the player is present in the game, null otherwise
     * @throws IllegalArgumentException if the specified player isn't in the game
     */
    private PlayerBoard getPlayerBoardOfPlayer(ClientHandler clientHandler) throws IllegalArgumentException {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey() == clientHandler)
                return e.getValue();
        throw new IllegalArgumentException("The specified ClientHandler isn't in the game!");
    }

    /**
     * Returns the PlayerBoard whose nickname is specified as a parameter
     *
     * @param //nickname the nickname of a player
     * @return the PlayerBoard of the player if the player is present in the game, null otherwise
     */
    /*private PlayerBoard getPlayerBoardOfPlayer(String nickname){
        for(Pair<ClientHandler, PlayerBoard> e: players)
            if(e.getKey().getNickname().equals(nickname))
                return e.getValue();
        return null;
    }*/
    public MainBoard getModelCopy() {
        return this.modelCopy;
    }

    public MainBoard getMainBoard() {
        return this.mainBoard;
    }

    public List<ClientHandler> getClientHandlersCopy() {
        return this.clientHandlersCopy;
    }

    public GameState getGameStateCopy() {
        return this.gameStateCopy;
    }

    /**
     * Returns the ClientHandler whose player's nickname is specified as a parameter
     *
     * @param nickname the player's nickname the returned ClientHandler is associated with
     * @return a ClientHandler if there is in this game a ClientHandler with a nickname equals to the one specified, null otherwisef
     */
    public ClientHandler getClientHandlerFromNickname(String nickname) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey().getNickname().equals(nickname))
                return e.getKey();
        return null;
    }

    /**
     * Returns the index of the first player in the game (the first player is the one who will be the first to play)
     *
     * @return the index of the first player
     */
    public int getFirstPlayer() {
        return firstPlayer;
    }

    /*
    ###########################################################################################################
     TO CLIENT MESSAGES
    ###########################################################################################################
     */

    //Tested
    public boolean showLeaderCardAtBeginning() {
        mainBoard.giveLeaderCardsToPlayerAtGameBeginning();

        /*Game game = new Game();
        Player player;
        for (int i = 0; i < this.numberOfPlayers; i++) {
            player = new Player();
            player.setNickName(players.get(i).getKey().getNickname());
            player.setPlayerState(players.get(i).getKey().getPlayerState());
            player.setUnUsedLeaders(players.get(i).getValue().getNotPlayedLeaderCards());
            game.addPlayer(player);
        }
        //Sends the DevGrid and the Market for the first time
        Board board = new Board();
        board.setDevMatrix(mainBoard.getDevMatrix());
        board.setMarketMatrix(mainBoard.getMarketMatrixWithMarbleType());
        board.setMarbleOnSlide(mainBoard.getMarbleOnSlideWithMarbleType());
        game.setMainBoard(board);


        this.sendBroadcastUpdate(new ModelUpdate(game));*/

        this.sendBroadcastUpdate(this.getWholeMessageUpdateToClient());

        //TODO: controllare se va bene
        synchronized (this) {
            for (Pair<ClientHandler, PlayerBoard> e : players)
                if (e.getKey().getPlayerState() == PlayerState.DISCONNECTED)
                    this.disconnectedBeforeStarting.add(e.getKey());
        }

        return true;
    }

    //Tested
    public boolean sendNumExtraResBeginning() {
        //Randomly chooses a first player
        this.firstPlayer = mainBoard.getFirstPlayerRandomly();
        activePlayer = players.get(firstPlayer).getKey();

        try {
            mainBoard.giveExtraFaithPointAtBeginning(firstPlayer);
        } catch (LastVaticanReportException e) {
            //this.setLastTurn();
            System.out.println("FATAL ERROR");
        }

        //For each player in the game computes how many extra resources they get, how many leader they have to discard at the beginning, and their order in the game.
        //It sends this information back to all the players
        ExtraResAndLeadToDiscardBeginningMessage message;
        for (int i = 0; i < this.numberOfPlayers; i++) {
            message = new ExtraResAndLeadToDiscardBeginningMessage(mainBoard.getExtraResourcesAtBeginningForPlayer(firstPlayer, i), mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning(), mainBoard.getPlayerOder(firstPlayer, i));
            players.get(i).getKey().send(message);
        }
        return true;
    }

    public boolean sendNumExtraResBeginningToDisconnectedPlayer(ClientHandler usedToBeDisconnected) throws IllegalActionException {
        /*if (!(this.disconnectedBeforeStarting.contains(usedToBeDisconnected)))
            throw new IllegalActionException("The player has already given their beginning decisions!");*/
        synchronized (this) {
            boolean tmp = false;
            for (ClientHandler ch : disconnectedBeforeStarting)
                if (ch.getNickname().equals(usedToBeDisconnected.getNickname()))
                    tmp = true;
            if (!tmp)
                throw new IllegalActionException("The player has already given their beginning decisions!");
        }
        //For the specified player it computes how many extra resources they get, how many leader they have to discard at the beginning, and their order in the game.
        //It sends this information back to all the players
        int index = this.getPlayerNumber(usedToBeDisconnected);
        ExtraResAndLeadToDiscardBeginningMessage message = new ExtraResAndLeadToDiscardBeginningMessage(mainBoard.getExtraResourcesAtBeginningForPlayer(firstPlayer, index), mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning(), mainBoard.getPlayerOder(firstPlayer, index));
        usedToBeDisconnected.send(message);
        return true;
    }

    /*
    ###########################################################################################################
     FROM CLIENT MESSAGES
    ###########################################################################################################
     */

    //TODO: i numeri che il client passa degli indici NON sono da informatici (devono essere decrementati per accedere agli indici effettivi delle matrici, delle liste, etc.)

    //Tested
    public boolean discardLeaderAndExtraResBeginning(DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning, ClientHandler clientHandler) throws IllegalArgumentException, IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        for (Integer i : discardLeaderCardBeginning.getLeaderCard())
            if (i < 0)
                throw new IllegalArgumentException("The index of the LeaderCard must be a positive integer!");

        //Computes how many resources the Player has sent back to the GameController
        int total = 0;
        for (DepotParams dP : discardLeaderCardBeginning.getDepotRes())
            total = total + dP.getQt();

        //The number of quantity sent by the player must be equal to the amount of extra resources they are supposed to send back
        if (mainBoard.getExtraResourcesAtBeginningForPlayer(this.firstPlayer, mainBoard.getPlayerBoardIndex(playerBoard)) != total) {
            //System.out.println("COSA HA MANDATO: " + total + "Cosa doveva: " + mainBoard.getExtraResourcesAtBeginningForPlayer(this.firstPlayer, mainBoard.getPlayerBoardIndex(playerBoard)));
            throw new IllegalArgumentException("You must specify the right amount of extra resources!");
        }

        //The amount of cards sent back must be equal to the amount they are supposed to discard
        if (mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning() != discardLeaderCardBeginning.getLeaderCard().size())
            throw new IllegalArgumentException("You are supposed to discard " + mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning() + " cards!");

        List<LeaderCard> leaderCard = playerBoard.getNotPlayedLeaderCardsFromIndex(discardLeaderCardBeginning.getLeaderCard());

        try {
            this.saveState();

            playerBoard.discardLeaderCardsAtTheBeginning(leaderCard);

            for (DepotParams dP : discardLeaderCardBeginning.getDepotRes())
                playerBoard.addResourceToDepot(dP.getResourceType(), dP.getQt(), dP.getShelf());

        } catch (IllegalArgumentException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalActionException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        }

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        Game game = new Game();
        //Adds the three depots with what's inside for all the three elements
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(0);
        this.sendBroadcastUpdate(new ModelUpdate(game));
        synchronized (this) {
            if (this.disconnectedBeforeStarting.contains(clientHandler))
                this.disconnectedBeforeStarting.remove(clientHandler);
        }
        clientHandler.send(new BeggingDecisionsConfirmationMessage());
        //this.checkIfGameMustBegin();
        return true;
    }

    //Tested
    public boolean discardLeader(LeaderMessage discardLeader, ClientHandler clientHandler) throws IllegalArgumentException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        if (discardLeader.getLeader() < 0)
            throw new IllegalArgumentException("The index of the LeaderCard must be a positive integer!");

        LeaderCard leaderCard = playerBoard.getNoPlayedLeaderCardFromIndex(discardLeader.getLeader());

        try {
            this.saveState();
            boolean outcome = playerBoard.discardLeaderCard(leaderCard);
        } catch (IllegalArgumentException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (LastVaticanReportException e) {
            this.setLastTurn();
        }

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        //Since the FaithPoints of one player increased, a Vatican Report may have taken place therefore we must save the FaithPosition and the PopeTiles
        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        int vp = playerBoard.partialVictoryPoints();
        //int vp = e.getValue().calculateVictoryPoints();
        player.setVictoryPoints(vp);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        setOthersPlayersFaithInClientModel(game, clientHandler);
        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    //Tested
    public boolean activateLeader(LeaderMessage activateLeader, ClientHandler clientHandler) throws IllegalArgumentException, IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        if (activateLeader.getLeader() < 0)
            throw new IllegalArgumentException("The index of the LeaderCard must be a positive integer!");

        LeaderCard leaderCard = playerBoard.getNoPlayedLeaderCardFromIndex(activateLeader.getLeader());

        try {
            this.saveState();
            boolean outcome = playerBoard.activateLeaderCard(leaderCard);
        } catch (IllegalActionException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        }

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setUsedLeaders(playerBoard.getActiveLeaderCards());
        int vp = playerBoard.partialVictoryPoints();
        //int vp = e.getValue().calculateVictoryPoints();
        player.setVictoryPoints(vp);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    //Tested
    public boolean getResFromMkt(GetFromMatrixMessage resFromMkt, ClientHandler clientHandler) throws IllegalArgumentException {
        if (resFromMkt.getRow() != 0 && resFromMkt.getCol() != 0)
            throw new IllegalArgumentException("Specify only a column or row!");

        HashMap<ResourceType, Integer> result;
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        List<Effect> effects = playerBoard.getEffectsFromCards(resFromMkt.getLeaderList());

        //We check that the amount of indicated effects are at least as many as the number of WhiteMarble in the desired row or column
        /*if (mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(resFromMkt.getRow() - 1, resFromMkt.getCol() - 1) > effects.size())
            throw new IllegalArgumentException("There are not enough LeaderCards specified!");*/
        if (!effects.isEmpty()) {
            if (resFromMkt.getRow() != 0) {
                if (mainBoard.getNumberOfWhiteMarbleInMarketRow(resFromMkt.getRow() - 1) > effects.size())
                    throw new IllegalArgumentException("There are not enough LeaderCards specified!");
            } else {
                if (mainBoard.getNumberOfWhiteMarbleInTheColumn(resFromMkt.getCol() - 1) > effects.size())
                    throw new IllegalArgumentException("There are not enough LeaderCards specified!");
            }
        } else {
            if (playerBoard.ctrlIfWhiteMarbleLeaderCardPresent())
                throw new IllegalArgumentException("You must specify your LeaderCard with a WhiteMarble effect!");
            else {
                if (resFromMkt.getCol() != 0) {
                    for (int j = 0; j < mainBoard.getNumberOfWhiteMarbleInTheColumn(resFromMkt.getCol() - 1); j++)
                        effects.add(new Effect());
                } else {
                    for (int j = 0; j < mainBoard.getNumberOfWhiteMarbleInMarketRow(resFromMkt.getRow() - 1); j++)
                        effects.add(new Effect());
                }
            }

        }
        //Hai carta white marble attiva e non me la passi: wrong
        //Se la lista è vuota allora la riempio a patto che non hai carte white marble attive


        if (resFromMkt.getCol() != 0)
            result = mainBoard.getResourcesFromColumnInMarket(resFromMkt.getCol() - 1, effects);
        else //if(resFromMkt.getRow() != 0)
            result = mainBoard.getResourcesFromRowInMarket(resFromMkt.getRow() - 1, effects);

        System.out.println("MESSAGE FOR CLIENT: ");
        for (Map.Entry<ResourceType, Integer> e : result.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        clientHandler.send(new HashMapResFromMarketMessage(result));
        return true;
    }

    //Tested
    public boolean buyFromMarket(BuyFromMarketMessage buyFromMarket, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        System.out.println(buyFromMarket.toString());


        System.out.println("MARKET BEFORE\n" + mainBoard.getMarket().toString());

        if (buyFromMarket.getRow() != 0 && buyFromMarket.getCol() != 0)
            throw new IllegalArgumentException("Specify only a column or row!");

        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);

        try {
            //We save the inner state of the game
            this.saveState();


            List<Effect> effects = playerBoard.getEffectsFromCards(buyFromMarket.getLeaderList());

            if (!effects.isEmpty()) {
                if (buyFromMarket.getRow() != 0) {
                    if (mainBoard.getNumberOfWhiteMarbleInMarketRow(buyFromMarket.getRow() - 1) > effects.size())
                        throw new IllegalArgumentException("There are not enough LeaderCards specified!");
                } else {
                    if (mainBoard.getNumberOfWhiteMarbleInTheColumn(buyFromMarket.getCol() - 1) > effects.size())
                        throw new IllegalArgumentException("There are not enough LeaderCards specified!");
                }
            } else {
                if (playerBoard.ctrlIfWhiteMarbleLeaderCardPresent())
                    throw new IllegalArgumentException("You must specify your LeaderCard with a WhiteMarble effect!");
                else {
                    if (buyFromMarket.getCol() != 0) {
                        for (int j = 0; j < mainBoard.getNumberOfWhiteMarbleInTheColumn(buyFromMarket.getCol() - 1); j++)
                            effects.add(new Effect());
                    } else {
                        for (int j = 0; j < mainBoard.getNumberOfWhiteMarbleInMarketRow(buyFromMarket.getRow() - 1); j++)
                            effects.add(new Effect());
                    }
                }

            }

            HashMap<ResourceType, Integer> res;
            if (buyFromMarket.getCol() != 0)
                res = mainBoard.moveColumnInMarket(buyFromMarket.getCol() - 1, effects);
            else
                res = mainBoard.moveRowInMarket(buyFromMarket.getRow() - 1, effects);

            List<DepotParams> depotRes = buyFromMarket.getDepotRes();

            String result = "FROM MARKET\n";
            for (Map.Entry<ResourceType, Integer> e : res.entrySet())
                result += e.getKey() + " " + e.getValue() + "\n";
            System.out.println(result);

            //Let's check if the description the player gives in the message is valid: all the resources they put are present in the computed market
            //output resources (we check both if the indicated ResourceType is present and if it is present with the right quantity)
            for (DepotParams e : depotRes) {
                if (e.getResourceType() != ResourceType.FAITHPOINT && (res.get(e.getResourceType()) == null || res.get(e.getResourceType()) < e.getQt())) {
                    //this.rollbackState();
                    throw new IllegalArgumentException("The given input parameters for the Depot don't match the result!");
                }

                //If the thread arrives here, then the current <ResType, Integer> is fine because at least it is coherent to what has been computed
                res.put(e.getResourceType(), res.get(e.getResourceType()) - e.getQt()); //Updates the res map
                playerBoard.addResourceToDepot(e.getResourceType(), e.getQt(), e.getShelf());
            }

            Map<ResourceType, Integer> resToLeader = buyFromMarket.getLeaderRes();
            for (Map.Entry<ResourceType, Integer> e : resToLeader.entrySet()) {
                //Let's check if the description the player gives in the message is valid: all the resources they put are present in the computed market
                //output resources (we check both if the indicated ResourceType is present and if it is present with the right quantity)
                if (e.getKey() != ResourceType.FAITHPOINT && (res.get(e.getKey()) == null || res.get(e.getKey()) < e.getValue())) {
                    //this.rollbackState();
                    throw new IllegalArgumentException("The given input parameters for the LeaderDepot don't match the result!");
                }

                //If the thread arrives here, then the current <ResType, Integer> is fine because at least it is coherent to what has been computed
                res.put(e.getKey(), res.get(e.getKey()) - e.getValue()); //Updates the res map
                playerBoard.addResourceToLeader(e.getKey(), e.getValue());
            }

            //Let's check if the description the player gives in the message is valid: all the resources they put are present in the computed market
            //output resources (we check both if the indicated ResourceType is present and if it is present with the right quantity which in this case
            //means that the need to equal to the remaining resources because all resources coming from the market must be dealt with)!
            for (Map.Entry<ResourceType, Integer> e : buyFromMarket.getDiscardRes().entrySet())
                if (e.getKey() != ResourceType.FAITHPOINT && (res.get(e.getKey()) == null || !res.get(e.getKey()).equals(e.getValue())))
                    throw new IllegalArgumentException("The given input parameters for the discarded resources don't match the result!");
                else
                    res.put(e.getKey(), res.get(e.getKey()) - e.getValue()); //Updates the res map

            //Discards the Extra resources
            try {
                mainBoard.discardResources(buyFromMarket.getDiscardRes(), playerBoard);
            } catch (LastVaticanReportException e) {
                this.setLastTurn();
            }

            //Checks if there are still some resources coming from the market which have not been dealt with: if this happens, the player hasn't where to put all the
            //resources they are supposed to (this is particular useful to check the case in which the player doesn't specify any parameter: while they don't gain any resource
            //they still have changed the market composition even if they weren't supposed to).
            for (Map.Entry<ResourceType, Integer> e : res.entrySet())
                if (e.getKey() != ResourceType.FAITHPOINT && e.getValue() != 0)
                    throw new IllegalArgumentException("The given input parameters don't match the result: you are missing out some resources!");

            //If we are here, then everything went fine and the player can get their extra FaithPoints
            try {
                if (res.get(ResourceType.FAITHPOINT) != null)
                    playerBoard.moveForwardOnFaithTrack(res.get(ResourceType.FAITHPOINT));
            } catch (LastVaticanReportException e) {
                this.setLastTurn();
            }


        } catch (IllegalActionException e) {
            this.rollbackState();
            System.out.println("MARKET AFTER ROLLBACK\n" + mainBoard.getMarket().toString());
            throw new IllegalActionException(e.getMessage());
        } catch (IllegalArgumentException e) {
            this.rollbackState();
            System.out.println("MARKET AFTER ROLLBACK\n" + mainBoard.getMarket().toString());
            throw new IllegalArgumentException(e.getMessage());
        }

        System.out.println("MARKET AFTER\n" + mainBoard.getMarket().toString());

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        Game game = new Game();
        Board board = new Board();
        board.setMarketMatrix(mainBoard.getMarket().getMarketMatrixWithMarbleType());
        board.setMarbleOnSlide(mainBoard.getMarket().getMarbleOnSlideWithMarbleType());
        game.setMainBoard(board);
        game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        setDepotInClientModel(player, playerBoard);
        //We get the PopeTiles of all players because a Vatican Report may have occurred
        player.setPopeTiles(playerBoard.getPopeTile());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        //int vp = e.getValue().calculateVictoryPoints();
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        setOthersPlayersFaithInClientModel(game, clientHandler);

        /*for (Pair<ClientHandler, PlayerBoard> e : players)
            if (!(e.getKey().getNickname().equals(clientHandler.getNickname()))) {
                Player tmp = new Player();
                tmp.setNickName(e.getKey().getNickname());
                tmp.setFaithPosition(e.getValue().getPositionOnFaithTrack());
                tmp.setPopeTiles(e.getValue().getPopeTile());
                game.addPlayer(tmp);
            }*/

        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    //SATTOOOO's methods

    //Tested
    public boolean getCardCost(GetFromMatrixMessage devCardMessage, ClientHandler clientHandler) throws IllegalArgumentException {
        DevCard devCard;
        HashMap<ResourceType, Integer> cost;

        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        List<Effect> effects = playerBoard.getEffectsFromCards(devCardMessage.getLeaderList());

        //TODO: da controllare il -1 perchè dipende dal come passiamo il valore nel messaggio
        devCard = mainBoard.getDevCardFromDeckInDevGrid(devCardMessage.getRow() - 1, devCardMessage.getCol() - 1);
        cost = mainBoard.applyDiscountToDevCard(devCard, effects);

        //send message only to the client that sent the message
        //TODO: nel messaggio io metterei anche il risultato dell'azione(status) per dire se è andata bene o no
        clientHandler.send(new HashMapResFromDevGridMessage(cost));
        return true;
    }

    public boolean buyDevCard(BuyDevCardMessage buyDevCard, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        DevCard devCard;
        HashMap<ResourceType, Integer> cost;

        //saves the state of the model
        this.saveState();

        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        List<Effect> effects = playerBoard.getEffectsFromCards(buyDevCard.getLeaders());
        try {
            //TODO: da controllare il -1 perchè dipende dal come passiamo il valore nel messaggio
            devCard = mainBoard.drawDevCardFromDeckInDevGrid(buyDevCard.getRow() - 1, buyDevCard.getCol() - 1);
            cost = mainBoard.applyDiscountToDevCard(devCard, effects);
        } catch (IllegalArgumentException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalActionException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        }

        List<DepotParams> depotRes = buyDevCard.getDepotRes();
        HashMap<ResourceType, Integer> resToLeader = buyDevCard.getLeaderRes();
        HashMap<ResourceType, Integer> strongboxRes = buyDevCard.getStrongboxRes();

        removeSelectedResources(cost, playerBoard, depotRes, resToLeader, strongboxRes);

        //TODO: da controllare il -1 perchè dipende dal come passiamo il valore nel messaggio
        try {
            playerBoard.addCardToDevSlot(buyDevCard.getDevSlot() - 1, devCard);
        } catch (EndOfGameException e) {
            this.setLastTurn();
        }

        Game game = new Game();
        Board board = new Board();
        board.setDevMatrix(this.mainBoard.getDevMatrix());
        game.setMainBoard(board);
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        player.setDevSlots(playerBoard.getDevSlots());
        setDepotInClientModel(player, playerBoard);
        player.setStrongBox(playerBoard.getStrongboxMap());
        int vp = playerBoard.partialVictoryPoints();
        //int vp = e.getValue().calculateVictoryPoints();
        player.setVictoryPoints(vp);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    public boolean moveResourcesBetweenShelves(MoveBetweenShelvesMessage moveBtwShelvesMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveBetweenShelves(moveBtwShelvesMessage.getSourceShelf(), moveBtwShelvesMessage.getDestShelf());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    public boolean moveResourcesToLeader(MoveShelfToLeaderMessage moveShelfToLeaderMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromShelfToLeader(moveShelfToLeaderMessage.getNumShelf(), moveShelfToLeaderMessage.getQuantity());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    public boolean moveResourcesToShelf(MoveLeaderToShelfMessage moveLeaderToShelfMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromLeaderToShelf(moveLeaderToShelfMessage.getRes(), moveLeaderToShelfMessage.getQuantity(), moveLeaderToShelfMessage.getDestShelf());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);

        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    //Tested
    public boolean getProductionCost(GetProductionCostMessage getProductionCostMessage, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        HashMap<ResourceType, Integer> prodCost;
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        BaseProductionParams baseProductionParams = getProductionCostMessage.getBaseProd();

        if (baseProductionParams.isActivated()) {
            playerBoard.setBaseProduction(baseProductionParams.getBaseInput(), baseProductionParams.getBaseOutput());
        }

        //get devCard from devSlot index
        List<DevCard> devList = new ArrayList<>();
        for (int index : getProductionCostMessage.getDevCards())
            devList.add(playerBoard.getUsableDevCardFromDevSlotIndex(index));

        //get leaderCard from index
        List<LeaderCard> leaderList = new ArrayList<>();
        for (int index : getProductionCostMessage.getLeaders())
            leaderList.add(playerBoard.getActiveLeaderCards().get(index));

        prodCost = playerBoard.getProductionCost(devList, leaderList, baseProductionParams.isActivated());

        //send message only to the client that sent the message
        clientHandler.send(new HashMapResFromProdCostMessage(prodCost));
        return true;
    }

    public boolean activateProduction(ActivateProductionMessage activateProductionMessage, ClientHandler clientHandler) throws IllegalArgumentException, IllegalActionException {
        HashMap<ResourceType, Integer> prodCost;
        HashMap<LeaderCard, ResourceType> leaderMap = new HashMap<>();
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        BaseProductionParams baseProductionParams = activateProductionMessage.getBaseProduction();

        //save state of the model
        this.saveState();

        //retrieve info from message
        if (baseProductionParams.isActivated()) {
            try {
                playerBoard.setBaseProduction(baseProductionParams.getBaseInput(), baseProductionParams.getBaseOutput());
            } catch (IllegalArgumentException | NullPointerException e) {
                this.rollbackState();
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        //get devCard from devSlot index
        List<DevCard> devList = new ArrayList<>();
        for (int index : activateProductionMessage.getDevCards())
            devList.add(playerBoard.getUsableDevCardFromDevSlotIndex(index));

        //getMap for leaderCard-output resource and leaderCard list
        LeaderCard leaderCard;
        List<LeaderCard> leaderList = new ArrayList<>();
        for (Map.Entry<Integer, ResourceType> index : activateProductionMessage.getLeaders().entrySet()) {
            leaderCard = playerBoard.getActiveLeaderCards().get(index.getKey());
            leaderMap.put(leaderCard, index.getValue());
            leaderList.add(leaderCard);
        }

        try {
            prodCost = playerBoard.getProductionCost(devList, leaderList, baseProductionParams.isActivated());
        } catch (IllegalActionException | NullPointerException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        }

        List<DepotParams> depotRes = activateProductionMessage.getDepotInputRes();
        HashMap<ResourceType, Integer> resToLeader = activateProductionMessage.getLeaderSlotRes();
        HashMap<ResourceType, Integer> strongboxRes = activateProductionMessage.getStrongboxInputRes();

        removeSelectedResources(prodCost, playerBoard, depotRes, resToLeader, strongboxRes);

        try {
            playerBoard.activateProduction(devList, leaderMap, baseProductionParams.isActivated());
        } catch (IllegalArgumentException | NullPointerException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (LastVaticanReportException e) {
            this.setLastTurn();
        }

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setPlayerState(clientHandler.getPlayerState());
        setDepotInClientModel(player, playerBoard);
        player.setStrongBox(playerBoard.getStrongboxMap());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        int vp = playerBoard.partialVictoryPoints();
        //int vp = e.getValue().calculateVictoryPoints();
        game.addPlayer(player);
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        setOthersPlayersFaithInClientModel(game, clientHandler);
        this.sendBroadcastUpdate(new ModelUpdate(game));
        return true;
    }

    private void removeSelectedResources(HashMap<ResourceType, Integer> cost, PlayerBoard playerBoard, List<DepotParams> depotRes, HashMap<ResourceType, Integer> resToLeader, HashMap<ResourceType, Integer> strongboxRes) throws IllegalActionException {
        //check if depot params are correct
        for (DepotParams e : depotRes) {
            if (cost.get(e.getResourceType()) == null || cost.get(e.getResourceType()) < e.getQt()) {
                this.rollbackState();
                throw new IllegalArgumentException("Error with resource quantity in depot");
            }

            cost.put(e.getResourceType(), cost.get(e.getResourceType()) - e.getQt()); //Updates the cost map
            playerBoard.removeResourceFromDepot(e.getQt(), e.getShelf());
        }

        //check if leaderSlot params are correct
        for (Map.Entry<ResourceType, Integer> e : resToLeader.entrySet()) {
            if (cost.get(e.getKey()) == null || cost.get(e.getKey()) < e.getValue()) {
                this.rollbackState();
                throw new IllegalArgumentException("Error with resource quantity in leader depot");
            }

            cost.put(e.getKey(), cost.get(e.getKey()) - e.getValue()); //Updates the cost map
            playerBoard.removeResourceFromLeader(e.getKey(), e.getValue());
        }

        //check if strongbox params are correct
        for (Map.Entry<ResourceType, Integer> e : strongboxRes.entrySet()) {
            if (cost.get(e.getKey()) == null || cost.get(e.getKey()) < e.getValue()) {
                this.rollbackState();
                throw new IllegalArgumentException("Error with resource quantity in strongbox");
            }

            cost.put(e.getKey(), cost.get(e.getKey()) - e.getValue()); //Updates the cost map
            playerBoard.removeResourcesFromStrongbox(strongboxRes);
        }

        //final check
        for (Map.Entry<ResourceType, Integer> e : cost.entrySet()) {
            if (cost.get(e.getKey()) != 0) {
                this.rollbackState();
                throw new IllegalArgumentException("Error with resource quantity selected");
            }
        }
    }

    private void setDepotInClientModel(Player player, PlayerBoard playerBoard) {
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(1), playerBoard.getNumberOfResInShelf(1)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(2), playerBoard.getNumberOfResInShelf(2)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(3), playerBoard.getNumberOfResInShelf(3)));
        player.setLeaderSlots(playerBoard.getLeaderDepot());
    }

    private void setOthersPlayersFaithInClientModel(Game game, ClientHandler clientHandler) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (!(e.getKey().getNickname().equals(clientHandler.getNickname()))) {
                Player tmp = new Player();
                tmp.setNickName(e.getKey().getNickname());
                tmp.setPlayerState(e.getKey().getPlayerState());
                tmp.setFaithPosition(e.getValue().getPositionOnFaithTrack());
                tmp.setPopeTiles(e.getValue().getPopeTile());
                int vp = e.getValue().partialVictoryPoints();
                //int vp = e.getValue().calculateVictoryPoints();
                tmp.setVictoryPoints(vp);
                game.addPlayer(tmp);
            }
    }

    /*
    ###########################################################################################################
     SOLO BOARD ACTIONS
    ###########################################################################################################
     */

    public void drawSoloToken(ClientHandler clientHandler) {
        SoloBoard soloBoard = (SoloBoard) mainBoard;
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);

        try {
            SoloActionToken token = soloBoard.drawSoloToken();
            clientHandler.send(new LorenzosActionMessage(token));
        } catch (LastVaticanReportException | EmptyDevColumnException e) {
            e.printStackTrace();
            this.setLastTurn();
        } finally {
            Game game = new Game();
            Board board = new Board();
            Player player = new Player();

            board.setDevMatrix(mainBoard.getDevMatrix());
            game.setMainBoard(board);
            player.setNickName(clientHandler.getNickname());
            player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
            player.setPopeTiles(playerBoard.getPopeTile());
            player.setPlayerState(PlayerState.PLAYING);
            game.addPlayer(player);
            game.setLorenzosPosition(soloBoard.getLorenzoFaithTrackPosition());

            clientHandler.send(new ModelUpdate(game));
        }
    }



     /*
    ###########################################################################################################
     RESPONSE METHODS
    ###########################################################################################################
     */

    /**
     * Updates all the player that the specified player has been disconnected
     *
     * //@param disconnectedPlayer
     */
    /*@Deprecated
    public void updatesAfterDisconnection(ClientHandler disconnectedPlayer) {
        int index = this.getPlayerNumber(disconnectedPlayer);

        Game game = new Game();
        for (int i = 0; i < this.numberOfPlayers; i++) {
            if (i != index) {
                this.players.get(i).getKey().setPlayerState(PlayerState.WAITING4TURN);
            }
            Player player = new Player();
            player.setNickName(this.players.get(i).getKey().getNickname());
            player.setPlayerState(this.players.get(i).getKey().getPlayerState());
            game.addPlayer(player);
        }

        this.sendBroadcastUpdate(new ModelUpdate(game), disconnectedPlayer);
    }
    */

    /**
     * Updates all the player that the specified player has been disconnected or reconnected
     *
     * @param disconnectedPlayer the player that disconnected/reconnected from the game
     */
    public synchronized void updatesAfterDisconnection(ClientHandler disconnectedPlayer) {
        /*int index = this.getPlayerNumber(disconnectedPlayer);

        Game game = new Game();
        Player player = new Player();
        player.setNickName(this.players.get(index).getKey().getNickname());
        player.setPlayerState(this.players.get(index).getKey().getPlayerState());
        game.addPlayer(player);

        this.sendBroadcastUpdate(new ModelUpdate(game), disconnectedPlayer);*/


        List<ClientHandler> playerList = getPlayersList();
        Game game = new Game();
        for (ClientHandler playerInGame : playerList) {
            Player player = new Player();
            player.setNickName(playerInGame.getNickname());
            player.setPlayerState(playerInGame.getPlayerState());
            game.addPlayer(player);
        }
        this.sendBroadcastUpdate(new PlayerConnectionsUpdate(game, disconnectedPlayer.getNickname()), disconnectedPlayer);
    }

    public synchronized void updatesPlayersStates() {
        List<ClientHandler> playerList = getPlayersList();
        Game game = new Game();
        for (ClientHandler playerInGame : playerList) {
            Player player = new Player();
            player.setNickName(playerInGame.getNickname());
            player.setPlayerState(playerInGame.getPlayerState());
            game.addPlayer(player);
        }
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        this.sendBroadcastUpdate(new ModelUpdate(game));
    }


    /**
     * Updates the player's state by setting the specified player as the one who can play their turn and by setting the former player as in
     * waiting for their turn
     *
     * @param playerToBecomeActive the playing who is about to be play
     */
    private synchronized void updatesTurnAndSendInfo(int playerToBecomeActive) {
        Game game = new Game();

        for (int i = 0; i < this.numberOfPlayers; i++) {
            if (players.get(i).getKey().getPlayerState() != PlayerState.DISCONNECTED) {
                /*if (i != playerToBecomeActive) {
                    this.players.get(i).getKey().setPlayerState(PlayerState.WAITING4TURN);
                } else {
                    this.players.get(i).getKey().setPlayerState(PlayerState.PLAYING);
                }*/
                if (!players.get(i).getKey().isBeginningActionDone())
                    players.get(i).getKey().setPlayerState(PlayerState.WAITING4BEGINNINGDECISIONS);
                else if (state != GameState.LASTTURN && (players.get(i).getKey().getPlayerState() == PlayerState.PLAYING || players.get(i).getKey().getPlayerState() == PlayerState.PLAYINGBEGINNINGDECISIONS))
                    players.get(i).getKey().setPlayerState(PlayerState.WAITING4TURN);
                else if (state == GameState.LASTTURN && (players.get(i).getKey().getPlayerState() == PlayerState.PLAYINGLASTTURN || players.get(i).getKey().getPlayerState() == PlayerState.PLAYING || players.get(i).getKey().getPlayerState() == PlayerState.PLAYINGBEGINNINGDECISIONS))
                    players.get(i).getKey().setPlayerState(PlayerState.WAITING4GAMEEND);
                if (i == playerToBecomeActive)
                    if (players.get(i).getKey().getPlayerState() == PlayerState.WAITING4GAMEEND)
                        this.endGame();
                    else if (players.get(i).getKey().getPlayerState() == PlayerState.WAITING4BEGINNINGDECISIONS)
                        players.get(i).getKey().setPlayerState(PlayerState.PLAYINGBEGINNINGDECISIONS);
                    else if (state == GameState.LASTTURN)
                        players.get(i).getKey().setPlayerState(PlayerState.PLAYINGLASTTURN);
                    else
                        players.get(i).getKey().setPlayerState(PlayerState.PLAYING);
            }
            Player player = new Player();
            player.setNickName(this.players.get(i).getKey().getNickname());
            player.setPlayerState(this.players.get(i).getKey().getPlayerState());
            game.addPlayer(player);
            if (numberOfPlayers == 1)
                game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());
        }

        if (numOfTurn == numberOfPlayers)
            this.state = GameState.INSESSION;

        /*//If the next player is supposed to be in PLAYING state when the game is still in STARTED state then all the active players have given their beginning
        //decisions and therefore the game can normally function
        if (this.state == GameState.STARTED && players.get(playerToBecomeActive).getKey().getPlayerState() == PlayerState.PLAYING)
            this.state = GameState.INSESSION;*/

        if (!hasGameEnded())
            this.sendBroadcastUpdate(new ModelUpdate(game));
        else
            this.endGame();
    }

    public void sendUpdateSolo(ClientHandler soloPlayer) {
        Game game = new Game();
        Player player = new Player();
        player.setNickName(soloPlayer.getNickname());
        player.setPlayerState(soloPlayer.getPlayerState());
        game.addPlayer(player);
        game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        soloPlayer.send(new ModelUpdate(game));

    }


    private synchronized void endGame() {
        this.distributeFinalPoints();
        GamesManagerSingleton.getInstance().deleteGame(this);
        //TODO: ci sarà da chiudere le socket o tanto quando viene mandato la fine del gioco il Client non fa più mandare niente di altro?
    }

    private void endGameSolo(){
        final int faithCells = 24;
        final int devCardNumber = 7;
        SoloBoard soloBoard = (SoloBoard) mainBoard;
        SoloGameResultMessage soloGameResultMessage;

        PlayerBoard playerBoard = null;
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if(e.getKey().getNickname().equals(activePlayer.getNickname()))
                playerBoard = e.getValue();

        //check if one of the column in dev grid is empty
        for(DevCardColour color : DevCardColour.values()){
            if(soloBoard.isDevCardColumnEmpty(color)){
                soloGameResultMessage = new SoloGameResultMessage(false, "You lost! Lorenzo bought an entire column of dev cards!");
                activePlayer.send(soloGameResultMessage);
                return;
            }
        }

        if(soloBoard.getLorenzoFaithTrackPosition() == faithCells) {
            soloGameResultMessage = new SoloGameResultMessage(false, "You lost! Lorenzo made his last vatican report!");
            activePlayer.send(soloGameResultMessage);
            return;
        }

        if (playerBoard.getPositionOnFaithTrack() == faithCells || playerBoard.getDevSlots().getAllDevCards().size() == devCardNumber){
            soloGameResultMessage = new SoloGameResultMessage(true, "You won against Lorenzo the Magnificent! Your score is: " + playerBoard.calculateVictoryPoints() + " points!");
            activePlayer.send(soloGameResultMessage);
        }

        GamesManagerSingleton.getInstance().deleteGame(this);
        //TODO: ci sarà da chiudere le socket o tanto quando viene mandato la fine del gioco il Client non fa più mandare niente di altro?
    }

    private void distributeFinalPoints() {
        FinalScoresMessage message = new FinalScoresMessage();
        for (Pair<ClientHandler, PlayerBoard> e : players)
            message.addScore(e.getKey().getNickname(), e.getValue().calculateVictoryPoints());
        this.sendBroadcastUpdate(message);
    }

    private boolean hasGameEnded() {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (!(e.getKey().getPlayerState() == PlayerState.DISCONNECTED || e.getKey().getPlayerState() == PlayerState.WAITING4GAMEEND))
                return false;
        return true;
    }


    @Deprecated
    private void sendBroadcastStringMessage(String message) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            e.getKey().send(message);
    }

    public ModelUpdate getWholeMessageUpdateToClient() {
        Game game = new Game();
        Board board = new Board();
        board.setMarketMatrix(this.mainBoard.getMarketMatrixWithMarbleType());
        board.setMarbleOnSlide(this.mainBoard.getMarbleOnSlideWithMarbleType());
        board.setDevMatrix(this.mainBoard.getDevMatrix());
        game.setMainBoard(board);
        for (int i = 0; i < this.numberOfPlayers; i++) {
            PlayerBoard playerBoard = players.get(i).getValue();
            ClientHandler clientHandler = players.get(i).getKey();
            Player player = new Player();
            player.setNickName(clientHandler.getNickname());
            player.setPlayerState(clientHandler.getPlayerState());
            player.setDevSlots(playerBoard.getDevSlots());
            player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
            player.setUsedLeaders(playerBoard.getActiveLeaderCards());
            player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
            player.setBaseProductionInput(playerBoard.getBaseProductionInput());
            player.setBaseProductionOutput(playerBoard.getBaseProductionOutput());
            this.setDepotInClientModel(player, playerBoard);
            player.setStrongBox(playerBoard.getStrongboxMap());
            player.setLeaderSlots(playerBoard.getLeaderDepot());
            player.setVictoryPoints(playerBoard.calculateVictoryPoints());
            player.setPopeTiles(playerBoard.getPopeTile());
            game.addPlayer(player);
        }
        if (numberOfPlayers == 1)
            game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        return new ModelUpdate(game);
    }

    //Is public only for testing purposes
    public synchronized void setLastTurn() {
        //TODO: se è stato attivato l'ultimo turno e il giocatore ha già giocato, va in WAITEND, se non ha ancora giocato va in WAITLAST
        this.state = GameState.LASTTURN;

        if (numberOfPlayers == 1) {
            this.endGameSolo();
        } else {
            for (Pair<ClientHandler, PlayerBoard> e : players) {
                if (e.getKey().getPlayerState() != PlayerState.DISCONNECTED && e.getKey().getPlayerState() != PlayerState.WAITING4BEGINNINGDECISIONS && e.getKey().getPlayerState() != PlayerState.PLAYING && e.getKey().getPlayerState() != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                    if (willPlayInThisTurn(e.getKey()))
                        e.getKey().setPlayerState(PlayerState.WAITING4LASTTURN);
                    else
                        e.getKey().setPlayerState(PlayerState.WAITING4GAMEEND);

                }
            }
        }


        /*//TODO: SISTEMARRE SECONDO SATTO'S WAY
        this.state = GameState.LASTTURN;
        int i = this.getPlayerNumber(currentPlayer);
        i++;
        if (i == this.numberOfPlayers)
            i = 0;
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey().getPlayerState() != PlayerState.DISCONNECTED && players.get(i).getKey().getPlayerState() != PlayerState.WAITING4BEGINNINGDECISIONS)
                e.getKey().setPlayerState(PlayerState.WAITING4GAMEEND);
        while (i != this.firstPlayer) {
            if (players.get(i).getKey().getPlayerState() != PlayerState.DISCONNECTED && players.get(i).getKey().getPlayerState() != PlayerState.WAITING4BEGINNINGDECISIONS)
                players.get(i).getKey().setPlayerState(PlayerState.WAITING4LASTTURN);
            i++;
            if (i == this.numberOfPlayers)
                i = 0;
        }*/
    }

    /**
     * Sends an update message to all the players in the game
     *
     * @param broadcastMessage the message to be sent to every player in the game
     */
    private void sendBroadcastUpdate(ResponseInterface broadcastMessage) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            e.getKey().send(broadcastMessage);
    }

    /**
     * Sends an update message to every player in the game but the one specified because they have been found disconnected
     *
     * @param broadcastMessage   the message to be sent to every player in the game
     * @param disconnectedPlayer the player who has been found disconnected
     */
    private void sendBroadcastUpdate(ResponseInterface broadcastMessage, ClientHandler disconnectedPlayer) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey() != disconnectedPlayer)
                e.getKey().send(broadcastMessage);
    }

    /*private boolean sendErrorToClientHandler(ClientHandler clientHandler, String message) {
        //TODO: mandare un messaggio al client handler in questione passandogli il messaggio d'errore
        return false;
    }*/

    /**
     * Saves the inner state of the Model by saving a copy of the MainBoard (and, therefore, a copy of all the PlayerBoards).
     */
    private synchronized void saveState() {
        //Saves a copy of the MainBoard
        //this.modelCopy = new MainBoard(mainBoard);
        this.modelCopy = mainBoard.getClone();


        //Saves a partial copy of the players in the game keeping the same order as the one stored in this GameController
        this.clientHandlersCopy = new ArrayList<>();
        for (Pair<ClientHandler, PlayerBoard> e : players)
            this.clientHandlersCopy.add(ClientHandler.getPartialCopy(e.getKey()));

        //Saves the state of the GameController
        this.gameStateCopy = this.state;
    }

    /**
     * Rollbacks the current changes by restoring the previous inner state.
     */
    private synchronized void rollbackState() {
        //Reinstates the MainBoard
        this.mainBoard = modelCopy;
        int i = 0;
        for (Pair<ClientHandler, PlayerBoard> e : players) {
            e.setValue(mainBoard.getPlayerBoard(i));
            i++;
        }

        //Changes back the values of the players
        i = 0;
        for (Pair<ClientHandler, PlayerBoard> e : players) {
            e.getKey().refreshState(this.clientHandlersCopy.get(i));
            i++;
        }

        //Changes back the GameState
        this.state = this.gameStateCopy;
    }

        /*
    ###########################################################################################################
     TESTING METHODS: These methods were used for testing purposes
    ###########################################################################################################
     */

    public void setFirstPlayer(int firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    //This method was used for testing purposes
    public void doSaveState() {
        this.saveState();
    }

    //This method was used for testing purposes
    public void doRollbackState() {
        this.rollbackState();
    }

    /*
    ###########################################################################################################
     TO BE USED....
    ###########################################################################################################
     */

    public ClientHandler getActivePlayer() {
        return activePlayer;
    }

    //used only for testing
    public void setActivePlayer(ClientHandler activePlayer) {
        this.activePlayer = activePlayer;
    }

    //used only for testing
    public void addDisconnectedBeforeStart(ClientHandler clientHandler) {
        disconnectedBeforeStarting.add(clientHandler);
    }

    //used only for testing
    public List<Pair<ClientHandler, PlayerBoard>> getPlayers() {
        return players;
    }

    private boolean findClientHandler(ClientHandler clientHandler) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey() == clientHandler)
                return true;
        return false;
    }

}
