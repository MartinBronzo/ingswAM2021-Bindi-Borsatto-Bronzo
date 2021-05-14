package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.soloGame.SoloBoard;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.ExtraResAndLeadToDiscardBeginningMessage;
import it.polimi.ingsw.network.messages.sendToClient.HashMapResources;
import it.polimi.ingsw.network.messages.sendToClient.ResponseType;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameController {
    private ClientHandler activePlayer;
    private MainBoard mainBoard;
    private List<Pair<ClientHandler, PlayerBoard>> players;
    private int numberOfPlayers;
    private int maxPlayersNum;
    private GameState state;
    private MainBoard modelCopy;
    private int firstPlayer;
    private Gson gson;
    private Integer howManyPlayersReady;


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
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Substitutes a ClientHandler with the one specified. This method is used when a player loses their connection and they reconnect to the server (the new
     * ClientHandler they get is the one specified as a parameter) and their PlayerState is set to WAITING4TURN because they are back in the game
     *
     * @param newClientHandler the ClientHandler which is going to substitute the one ClientHandler present in the game which has the same nickname as the one stored inside the
     *                         specified object
     * @return true if the substitution took place correctly, false if there was no substitution because the client associated with the specified ClientHandler is not in this game
     */
    public boolean substitutesClient(ClientHandler newClientHandler) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey().getNickname().equals(newClientHandler.getNickname())) {
                newClientHandler.setPlayerSate(PlayerState.WAITING4TURN);
                e.setKey(newClientHandler);
                return true;
            }

        return false;
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
        this.gson = new Gson();
        this.howManyPlayersReady = 0;
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
                //throw new IllegalActionException("Error in SoloBoardCreation");
            }
        }else {
            try {
                this.mainBoard = new MainBoard(numberOfPlayers);
            } catch (Exception e) {
                //System.out.println("Can't create the MainBoard because there are problems with the configuration files!");
                e.printStackTrace();
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
    public boolean setPlayerOld(ClientHandler player) throws IllegalActionException {
        //We can't add more players than the one given by the numberOfPlayers number
        if (this.players.size() == this.numberOfPlayers)
            throw new IllegalActionException("You can't be added to this game!");
        //We can't add an already added player
        //if(this.findClientHandler(player))
        if (this.getPlayerBoardOfPlayer(player) != null)
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
        //if(this.findClientHandler(player))
        if (this.getPlayerBoardOfPlayer(player) != null)
            return false;
        PlayerBoard playerBoard = this.mainBoard.getPlayerBoard(this.players.size());
        players.add(new Pair<>(player, playerBoard));
        //We added the last player: the game must begin
        /*if(players.size() == this.numberOfPlayers)
            this.startGame();
            //TODO: aggiungere questo if commentato causa ai test del GamesMangerSingletonTest di fallire
        */
        return true;
    }

    private void startGame(){
        this.state = GameState.STARTED;
        this.showLeaderCardAtBeginning();
        this.sendNumExtraResBeginning();
    }

    private void checkIfGameMustBegin(){
        synchronized (this.howManyPlayersReady) {
            this.howManyPlayersReady++;
            if (this.howManyPlayersReady == this.numberOfPlayers){
                this.state = GameState.INSESSION;//This player is the last one who's adding stuff so the players can play in turns
                this.sendBroadcastStringMessage("Now turns will begin!");
                this.updatesTurnAndSendInfo(this.firstPlayer);
            }
        }
    }

    public void specifyNextPlayer(ClientHandler currentPlayer){
        //TODO: deal with turns
        if(currentPlayer.getPlayerSate() != PlayerState.PLAYING)
            return;
        //Retrieves this player's index
        int index = this.getPlayerNumber(currentPlayer);
        if(index < 0)
            throw new IllegalArgumentException("The specified ClientHandler isn't in this game!");
        //The next active player is the next one in the list of players and the list must be cyclically covered
        index++;
        if(index == this.numberOfPlayers)
            index = 0;
        this.updatesTurnAndSendInfo(index);
    }

    /*
    ###########################################################################################################
     GENERAL GETTERS
    ###########################################################################################################
     */

    /**
     * Returns the index the specified player (represented by their ClientHandler) has in the list of players of this game
     * @param player the player whose index must be find
     * @return the index of the player if the player belongs to this game, -1 otherwise
     */
    private int getPlayerNumber(ClientHandler player){
        for(int i = 0; i < this.numberOfPlayers; i++)
            if(players.get(i).getKey() == player)
                return i;
        return -1;
    }

    /**
     * Returns a list of all the players that are added to the game in the moment this method is invoked
     *
     * @return a list of the player's ClientHandler added to the game
     */
    public List<ClientHandler> getPlayersList() {
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
     */
    private PlayerBoard getPlayerBoardOfPlayer(ClientHandler clientHandler) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey() == clientHandler)
                return e.getValue();
        return null;
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
    public boolean showLeaderCardAtBeginning(){
        mainBoard.giveLeaderCardsToPlayerAtGameBeginning();

        Game game = new Game();
        Player player;
        for(int i = 0; i < this.numberOfPlayers; i++){
            players.get(i).getKey().setPlayerSate(PlayerState.WAITING4BEGINNINGDECISIONS);
            player = new Player();
            player.setNickName(players.get(i).getKey().getNickname());
            player.setUnUsedLeaders(players.get(i).getValue().getNotPlayedLeaderCards());
            game.addPlayer(player);
        }

        this.sendBroadcastUpdate(game);

        return true;
    }

    //Tested
    public boolean sendNumExtraResBeginning() {
        //Randomly chooses a first player
        this.firstPlayer = mainBoard.getFirstPlayerRandomly();

        try {
            mainBoard.giveExtraFaithPointAtBeginning(firstPlayer);
        } catch (LastVaticanReportException e) {
            this.setLastTurn();
        }

        //For each player in the game computes how many extra resources they get, how many leader they have to discard at the beginning, and their order in the game.
        //It sends this information back to all the players
        ExtraResAndLeadToDiscardBeginningMessage message;
        for (int i = 0; i < this.numberOfPlayers; i++) {
            message = new ExtraResAndLeadToDiscardBeginningMessage(mainBoard.getExtraResourcesAtBeginningForPlayer(firstPlayer, i), mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning(), mainBoard.getPlayerOder(firstPlayer, i));
            players.get(i).getKey().send(gson.toJson(message));
        }
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
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        //Adds the three depots with what's inside for all the three elements
        /*player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(1), playerBoard.getNumberOfResInShelf(1)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(2), playerBoard.getNumberOfResInShelf(2)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(3), playerBoard.getNumberOfResInShelf(3)));
        */
        Game game = new Game();
        setDepotInClientModel(player, playerBoard); //TODO: COSA NE PENSI LUDO DI UNA COSA DEL GENERE?
        game.addPlayer(player);
        this.sendBroadcastUpdate(game);
        this.checkIfGameMustBegin();
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
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        game.addPlayer(player);
        /*for(Pair<ClientHandler, PlayerBoard> e: players)
            if(!(e.getKey().getNickname().equals(clientHandler.getNickname()))){
                Player tmp = new Player();
                tmp.setNickName(e.getKey().getNickname());
                tmp.setFaithPosition(e.getValue().getPositionOnFaithTrack());
                tmp.setPopeTiles(e.getValue().getPopeTile());
                game.addPlayer(tmp);
            }*/
        setOthersPlayersFaithInClientModel(game, clientHandler); //TODO: COSA NE PENSI LUDO DI UNA COSA DEL GENERE?
        this.sendBroadcastUpdate(game);
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
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setUsedLeaders(playerBoard.getActiveLeaderCards());
        game.addPlayer(player);
        this.sendBroadcastUpdate(game);
        return true;
    }

    //Tested
    public boolean getResFromMkt(GetFromMatrixMessage resFromMkt, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        if (resFromMkt.getRow() != 0 && resFromMkt.getCol() != 0)
            throw new IllegalArgumentException("Specify only a column or row!");

        HashMap<ResourceType, Integer> result = null;
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        List<Effect> effects = playerBoard.getEffectsFromCards(resFromMkt.getLeaderList());

        //We check that the amount of indicated effects are at least as many as the number of WhiteMarble in the desired row or column
        /*if (mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(resFromMkt.getRow() - 1, resFromMkt.getCol() - 1) > effects.size())
            throw new IllegalArgumentException("There are not enough LeaderCards specified!");*/
        if (resFromMkt.getRow() != 0) {
            if (mainBoard.getNumberOfWhiteMarbleInMarketRow(resFromMkt.getRow() - 1) > effects.size())
                throw new IllegalArgumentException("There are not enough LeaderCards specified!");
        }else {
            if (mainBoard.getNumberOfWhiteMarbleInTheColumn(resFromMkt.getCol() - 1) > effects.size())
                throw new IllegalArgumentException("There are not enough LeaderCards specified!");
        }

        if (resFromMkt.getCol() != 0)
            result = mainBoard.getResourcesFromColumnInMarket(resFromMkt.getCol() - 1, effects);
        else //if(resFromMkt.getRow() != 0)
            result = mainBoard.getResourcesFromRowInMarket(resFromMkt.getRow() - 1, effects);

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        clientHandler.send(gson.toJson(new HashMapResources(result)));
        return true;
    }

    //Tested
    public boolean buyFromMarket(BuyFromMarketMessage buyFromMarket, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        if (buyFromMarket.getRow() != 0 && buyFromMarket.getCol() != 0)
            throw new IllegalArgumentException("Specify only a column or row!");

        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);

        try {
            //We save the inner state of the game
            this.saveState();


            List<Effect> effects = playerBoard.getEffectsFromCards(buyFromMarket.getLeaderList());

            //We check that the amount of indicated effects are at least as many as the number of WhiteMarble in the desired row or column
            /*if (mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(resFromMkt.getRow() - 1, resFromMkt.getCol() - 1) > effects.size())
            throw new IllegalArgumentException("There are not enough LeaderCards specified!");*/
            if (buyFromMarket.getRow() != 0) {
                if (mainBoard.getNumberOfWhiteMarbleInMarketRow(buyFromMarket.getRow() - 1) > effects.size())
                    throw new IllegalArgumentException("There are not enough LeaderCards specified!");
            }else {
                if (mainBoard.getNumberOfWhiteMarbleInTheColumn(buyFromMarket.getCol() - 1) > effects.size())
                    throw new IllegalArgumentException("There are not enough LeaderCards specified!");
            }

            HashMap<ResourceType, Integer> res;
            if (buyFromMarket.getCol() != 0)
                res = mainBoard.moveColumnInMarket(buyFromMarket.getCol() - 1, effects);
            else
                res = mainBoard.moveRowInMarket(buyFromMarket.getRow() - 1, effects);

            List<DepotParams> depotRes = buyFromMarket.getDepotRes();

            //Let's check if the description the player gives in the message is valid: all the resources they put are present in the computed market
            //output resources (we check both if the indicated ResourceType is present and if it is present with the right quantity)
            for (DepotParams e : depotRes) {
                if (res.get(e.getResourceType()) == null || res.get(e.getResourceType()) < e.getQt()) {
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
                if (res.get(e.getKey()) == null || res.get(e.getKey()) < e.getValue()) {
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
            for(Map.Entry<ResourceType, Integer> e: buyFromMarket.getDiscardRes().entrySet())
                if (res.get(e.getKey()) == null || res.get(e.getKey()) != e.getValue())
                    throw new IllegalArgumentException("The given input parameters for the discarded resources don't match the result!");
                else
                    res.put(e.getKey(), res.get(e.getKey()) - e.getValue()); //Updates the res map

            //Discards the Extra resources
            mainBoard.discardResources(buyFromMarket.getDiscardRes(), playerBoard);

            //Checks if there are still some resources coming from the market which have not been dealt with: if this happens, the player hasn't where to put all the
            //resources they are supposed to (this is particular useful to check the case in which the player doesn't specify any parameter: while they don't gain any resource
            //they still have changed the market composition even if they weren't supposed to).
            for(Map.Entry<ResourceType, Integer> e: res.entrySet())
                if(e.getValue() != 0)
                    throw new IllegalArgumentException("The given input parameters don't match the result: you are missing out some resources!");

        } catch (IllegalActionException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        } catch (IllegalArgumentException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (LastVaticanReportException e) {
            this.setLastTurn();
        }

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        Game game = new Game();
        Board board = new Board();
        board.setMarketMatrix(mainBoard.getMarket().getMarketMatrixWithMarbleType());
        board.setMarbleOnSlide(mainBoard.getMarket().getMarbleOnSlideWithMarbleType());
        game.setMainBoard(board);
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        /*player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(1), playerBoard.getNumberOfResInShelf(1)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(2), playerBoard.getNumberOfResInShelf(2)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(3), playerBoard.getNumberOfResInShelf(3)));
        player.setLeaderSlots(playerBoard.getLeaderDepot());*/
        setDepotInClientModel(player, playerBoard); //TODO: COSA NE PENSI LUDO DI UNA COSA DEL GENERE?
        //We get the PopeTiles of all players because a Vatican Report may have occurred
        player.setPopeTiles(playerBoard.getPopeTile());
        game.addPlayer(player);
        for(Pair<ClientHandler, PlayerBoard> e: players)
            if(!(e.getKey().getNickname().equals(clientHandler.getNickname()))){
                Player tmp = new Player();
                tmp.setNickName(e.getKey().getNickname());
                tmp.setFaithPosition(e.getValue().getPositionOnFaithTrack());
                tmp.setPopeTiles(e.getValue().getPopeTile());
                game.addPlayer(tmp);
            }
        this.sendBroadcastUpdate(game);
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
        clientHandler.send(gson.toJson(new HashMapResources(cost)));
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
        board.setDevGrid(mainBoard.getDevGrid());
        game.setMainBoard(board);
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        player.setDevSlots(playerBoard.getDevSlots());
        setDepotInClientModel(player, playerBoard);
        player.setStrongBox(playerBoard.getStrongboxMap());
        game.addPlayer(player);

        this.sendBroadcastUpdate(game);
        return true;
    }

    public boolean moveResourcesBetweenShelves(MoveBetweenShelvesMessage moveBtwShelvesMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveBetweenShelves(moveBtwShelvesMessage.getSourceShelf(), moveBtwShelvesMessage.getDestShelf());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);

        this.sendBroadcastUpdate(game);
        return true;
    }

    public boolean moveResourcesToLeader(MoveShelfToLeaderMessage moveShelfToLeaderMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromShelfToLeader(moveShelfToLeaderMessage.getNumShelf(), moveShelfToLeaderMessage.getQuantity());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);

        this.sendBroadcastUpdate(game);
        return true;
    }

    public boolean moveResourcesToShelf(MoveLeaderToShelfMessage moveLeaderToShelfMessage, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromLeaderToShelf(moveLeaderToShelfMessage.getRes(), moveLeaderToShelfMessage.getQuantity(), moveLeaderToShelfMessage.getDestShelf());

        Game game = new Game();
        Player player = new Player();
        player.setNickName(clientHandler.getNickname());
        setDepotInClientModel(player, playerBoard);
        game.addPlayer(player);

        this.sendBroadcastUpdate(game);
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
        clientHandler.send(gson.toJson(new HashMapResources(prodCost)));
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
        setDepotInClientModel(player, playerBoard);
        player.setStrongBox(playerBoard.getStrongboxMap());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        game.addPlayer(player);
        setOthersPlayersFaithInClientModel(game, clientHandler);

        this.sendBroadcastUpdate(game);
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

    private void setDepotInClientModel(Player player, PlayerBoard playerBoard){
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(1), playerBoard.getNumberOfResInShelf(1)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(2), playerBoard.getNumberOfResInShelf(2)));
        player.addDepotShelf(new DepotShelf(playerBoard.getResourceTypeFromShelf(3), playerBoard.getNumberOfResInShelf(3)));
        player.setLeaderSlots(playerBoard.getLeaderDepot());
    }

    private void setOthersPlayersFaithInClientModel(Game game, ClientHandler clientHandler){
        for(Pair<ClientHandler, PlayerBoard> e: players)
            if(!(e.getKey().getNickname().equals(clientHandler.getNickname()))){
                Player tmp = new Player();
                tmp.setNickName(e.getKey().getNickname());
                tmp.setFaithPosition(e.getValue().getPositionOnFaithTrack());
                tmp.setPopeTiles(e.getValue().getPopeTile());
                game.addPlayer(tmp);
            }
    }

     /*
    ###########################################################################################################
     RESPONSE PRIVATE METHODS
    ###########################################################################################################
     */

    /**
     * Updates the player's state by setting the specified player as the one who can play their turn and by setting the former player as in
     * waiting for their turn
     * @param playerToBecomeActive
     */
    private void updatesTurnAndSendInfo(int playerToBecomeActive){
        TurnInfoMessage message;
        ResponseMessage responseMessage;

        //The specified player becomes the active player
        this.players.get(playerToBecomeActive).getKey().setPlayerSate(PlayerState.PLAYING);
        message = new TurnInfoMessage(true);
        responseMessage = new ResponseMessage(ResponseType.TURNINFO, this.gson.toJson(message));
        this.players.get(playerToBecomeActive).getKey().send(this.gson.toJson(responseMessage));

        message = new TurnInfoMessage(false);
        responseMessage = new ResponseMessage(ResponseType.TURNINFO, this.gson.toJson(message));
        String toBeSent = this.gson.toJson(responseMessage);
        //All the others players are waiting for their turn
        for(int i = 0; i < this.numberOfPlayers; i++)
            if(i != playerToBecomeActive) {
                this.players.get(i).getKey().setPlayerSate(PlayerState.WAITING4TURN);
                this.players.get(i).getKey().send(toBeSent);
            }
    }

    private void sendBroadcastStringMessage(String message){
        GeneralInfoStringMessage messageObject = new GeneralInfoStringMessage(message);
        ResponseMessage responseMessage = new ResponseMessage(ResponseType.INFOSTRING, this.gson.toJson(messageObject));
        for(Pair<ClientHandler, PlayerBoard> e: players)
            e.getKey().send(this.gson.toJson(responseMessage));
    }

    public Game getWholeUpdateToClient(){
        Game game = new Game();
        Board board = new Board();
        board.setMarketMatrix(this.mainBoard.getMarketMatrixWithMarbleType());
        board.setMarbleOnSlide(this.mainBoard.getMarbleOnSlideWithMarbleType());
        board.setDevGrid(this.mainBoard.getDevGrid());
        game.setMainBoard(board);
        for(int i = 0; i < this.numberOfPlayers; i++){
            PlayerBoard playerBoard = players.get(i).getValue();
            ClientHandler clientHandler = players.get(i).getKey();
            Player player = new Player();
            player.setNickName(clientHandler.getNickname());
            player.setPlayerState(clientHandler.getState());
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

        game.setLorenzosPosition(mainBoard.getLorenzoFaithTrackPosition());

        return game;
    }

    private void setLastTurn() {
        this.state = GameState.LASTTURN;
    }

    /**
     * Sends an update message to all the players in the game
     *
     * @param game the read-only model which contains the updates
     * @return true if the messages where correctly sent to all the players
     */
    private boolean sendBroadcastUpdate(Game game) {
        //TODO: in realtà ci sarà da inviare un messaggio che contiene game come con i command per i messaggi nel package fromClient!
        for (Pair<ClientHandler, PlayerBoard> e : players)
            e.getKey().send(gson.toJson(game));
        return true;
    }

    /*private boolean sendErrorToClientHandler(ClientHandler clientHandler, String message) {
        //TODO: mandare un messaggio al client handler in questione passandogli il messaggio d'errore
        return false;
    }*/

    /**
     * Saves the inner state of the Model by saving a copy of the MainBoard (and, therefore, a copy of all the PlayerBoards).
     */
    private void saveState() {
        this.modelCopy = new MainBoard(mainBoard);
    }

    /**
     * Rollbacks the current changes by restoring the previous inner state.
     */
    private void rollbackState() {
        this.mainBoard = modelCopy;
        int i = 0;
        for (Pair<ClientHandler, PlayerBoard> e : players) {
            e.setValue(mainBoard.getPlayerBoard(i));
            i++;
        }
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

    private boolean findClientHandler(ClientHandler clientHandler) {
        for (Pair<ClientHandler, PlayerBoard> e : players)
            if (e.getKey() == clientHandler)
                return true;
        return false;
    }

}
