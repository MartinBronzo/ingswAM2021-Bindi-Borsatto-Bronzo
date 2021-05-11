package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.readOnlyModel.Game;
import it.polimi.ingsw.client.readOnlyModel.Player;
import it.polimi.ingsw.client.readOnlyModel.player.DepotShelf;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
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
import it.polimi.ingsw.network.messages.sendToClient.ResGottenFromMarket;

import javax.security.auth.login.AccountLockedException;
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
     GENERAL SETTERS
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
        try {
            this.mainBoard = new MainBoard(numberOfPlayers);
        } catch (Exception e) {
            //System.out.println("Can't create the MainBoard because there are problems with the configuration files!");
            e.printStackTrace();
        }
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Adds a player represented by the specified ClientHandler to the game if the player hasn't been added, yet, and if the game hasn't reach its maximum
     * capacity, yet.
     *
     * @param player the ClientHandler of the player to be added at the game
     */
    public boolean setPlayer(ClientHandler player) {
        //We can't add more players than the one given by the numberOfPlayers number
        if (this.players.size() == this.numberOfPlayers)
            return false;
        //We can't add an already added player
        //if(this.findClientHandler(player))
        if (this.getPlayerBoardOfPlayer(player) != null)
            return false;
        PlayerBoard playerBoard = this.mainBoard.getPlayerBoard(this.players.size());
        players.add(new Pair<>(player, playerBoard));
        return true;
    }

    /*
    ###########################################################################################################
     GENERAL GETTERS
    ###########################################################################################################
     */

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

    /*
    ###########################################################################################################
     TO CLIENT MESSAGES
    ###########################################################################################################
     */

    public boolean sendNumExtraResBeginning(){
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
        for(int i = 0; i < this.numberOfPlayers; i++){
            message = new ExtraResAndLeadToDiscardBeginningMessage(mainBoard.getExtraResourcesAtBeginningForPlayer(firstPlayer, i), mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning(), mainBoard.getPlayerOder(firstPlayer, i));
            players.get(i).getKey().send(gson.toJson(message));
        }
        //TODO: in un primo momento bisognerà mandare un messaggio separato ai giocatori sullo status del model che hanno (le carte leader che gli sono state mandate)
        return true;
    }

    /*
    ###########################################################################################################
     FROM CLIENT MESSAGES
    ###########################################################################################################
     */

    //TODO: i numeri che il client passa degli indici NON sono da informatici (devono essere decrementati per accedere agli indici effettivi delle matrici, delle liste, etc.)

    public boolean discardLeaderAndExtraResBeginning(DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning, ClientHandler clientHandler) throws IllegalArgumentException, IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        for(Integer i: discardLeaderCardBeginning.getLeaderCard())
            if(i < 0)
                throw new IllegalArgumentException("The index of the LeaderCard must be a positive integer!");

        //Computes how many resources the Player has sent back to the GameController
        int total = 0;
        for(DepotParams dP: discardLeaderCardBeginning.getDepotRes())
            total = total + dP.getQt();

        //The number of quantity sent by the player must be equal to the amount of extra resources they are supposed to send back
        if(mainBoard.getExtraResourcesAtBeginningForPlayer(this.firstPlayer, mainBoard.getPlayerBoardIndex(playerBoard)) != total){
            System.out.println("COSA HA MANDATO: " + total + "Cosa doveva: " + mainBoard.getExtraResourcesAtBeginningForPlayer(this.firstPlayer, mainBoard.getPlayerBoardIndex(playerBoard)));
            throw new IllegalArgumentException("You must specify the right amount of extra resources!");
        }

        //The amount of cards sent back must be equal to the amount they are supposed to discard
        if(mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning() != discardLeaderCardBeginning.getLeaderCard().size())
            throw new IllegalArgumentException("You are supposed to discard " + mainBoard.getNumberOfLeaderCardsToDiscardAtBeginning() + " cards!");

        List<LeaderCard> leaderCard = playerBoard.getNotPlayedLeaderCardsFromIndex(discardLeaderCardBeginning.getLeaderCard());

        try {
            this.saveState();

            playerBoard.discardLeaderCardsAtTheBeginning(leaderCard);

            for(DepotParams dP: discardLeaderCardBeginning.getDepotRes())
                playerBoard.addResourceToDepot(dP.getResourceType(), dP.getQt(), dP.getShelf());

        } catch (IllegalArgumentException e){
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalActionException e) {
            this.rollbackState();
            throw new IllegalActionException(e.getMessage());
        }

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        Player player = new Player();
        player.setUnUsedLeaders(playerBoard.getNotPlayedLeaderCards());
        player.setFaithPosition(playerBoard.getPositionOnFaithTrack());
        player.setPopeTiles(playerBoard.getPopeTile());
        //Adds the three depots
        //player.addDepotShelf(new DepotShelf(playerBoard.get));
        Game game = new Game();
        game.addPlayer(player);
        this.sendBroadcastUpdate(game);
        return true;
    }

    public boolean discardLeader(LeaderMessage discardLeader, ClientHandler clientHandler) throws IllegalArgumentException{
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
        //TODO: creare il game model
        Game game = new Game();
        this.sendBroadcastUpdate(game);
        return true;
    }

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
        //TODO: creare il game model
        Game game = new Game();
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
        if(resFromMkt.getRow() == 0)
            if(mainBoard.getNumberOfWhiteMarbleInMarketRow(resFromMkt.getRow() - 1) > effects.size())
                throw new IllegalArgumentException("There are not enough LeaderCards specified!");
        else
            if(mainBoard.getNumberOfWhiteMarbleInTheColumn(resFromMkt.getCol() - 1) > effects.size())
                throw new IllegalArgumentException("There are not enough LeaderCards specified!");

        if (resFromMkt.getCol() != 0)
            result = mainBoard.getResourcesFromColumnInMarket(resFromMkt.getCol() -1, effects);
        else //if(resFromMkt.getRow() != 0)
            result = mainBoard.getResourcesFromRowInMarket(resFromMkt.getRow() - 1, effects);

        //If we are here, then everything is going fine so result is containing something useful and must returned to the client
        clientHandler.send(gson.toJson(new ResGottenFromMarket(result)));
        return true;
    }

    public boolean buyFromMarket(BuyFromMarketMessage buyFromMarket, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        if (buyFromMarket.getRow() != 0 && buyFromMarket.getCol() != 0)
            throw new IllegalArgumentException("Specify only a column or row!");

        try {
            //We save the inner state of the game
            this.saveState();

            PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
            List<Effect> effects = playerBoard.getEffectsFromCards(buyFromMarket.getLeaderList());

            //We check that the amount of indicated effects are at least as many as the number of WhiteMarble in the desired row or column
            if (mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(buyFromMarket.getRow(), buyFromMarket.getCol()) > effects.size())
                throw new IllegalArgumentException("There are not enough LeaderCards specified!");

            HashMap<ResourceType, Integer> res;
            if (buyFromMarket.getCol() != 0)
                res = mainBoard.moveColumnInMarket(buyFromMarket.getCol(), effects);
            else
                res = mainBoard.moveRowInMarket(buyFromMarket.getRow(), effects);

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

            //Discards the Extra resources
            mainBoard.discardResources(buyFromMarket.getDiscardRes(), playerBoard);

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
        //TODO: creare il game model
        Game game = new Game();
        this.sendBroadcastUpdate(game);
        return true;
    }

    //SATTOOOO's methods

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
        Gson gson = new Gson();
        //clientHandler.send(gson.toJson(...));
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

        //TODO: lancia EndOfGameException
        //TODO: da controllare il -1 perchè dipende dal come passiamo il valore nel messaggio
        try {
            playerBoard.addCardToDevSlot(buyDevCard.getDevSlot() - 1, devCard);
        } catch (EndOfGameException e) {
            //TODO: gestire eccezione
        }

        //TODO: mandare broadcast a tutti i client
        //TODO: creare messaggio di update
        for (Pair<ClientHandler, PlayerBoard> user : players) {
            //user.getKey().send(update);
        }
        return true;
    }

    public boolean moveResourcesBetweenShelves(MoveBetweenShelvesMessage moveBtwShelvesMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveBetweenShelves(moveBtwShelvesMessage.getSourceShelf(), moveBtwShelvesMessage.getDestShelf());

        //TODO: mandare broadcast a tutti i client
        //TODO: creare messaggio di update
        for (Pair<ClientHandler, PlayerBoard> user : players) {
            //user.getKey().send(update);
        }
        return true;
    }

    public boolean moveResourcesToLeader(MoveShelfToLeaderMessage moveShelfToLeaderMessage, ClientHandler clientHandler) throws IllegalActionException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromShelfToLeader(moveShelfToLeaderMessage.getNumShelf(), moveShelfToLeaderMessage.getQuantity());

        //TODO: mandare broadcast a tutti i client
        //TODO: creare messaggio di update
        for (Pair<ClientHandler, PlayerBoard> user : players) {
            //user.getKey().send(update);
        }
        return true;
    }

    public boolean moveResourcesToShelf(MoveLeaderToShelfMessage moveLeaderToShelfMessage, ClientHandler clientHandler) throws IllegalActionException, IllegalArgumentException {
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        playerBoard.moveFromLeaderToShelf(moveLeaderToShelfMessage.getRes(), moveLeaderToShelfMessage.getQuantity(), moveLeaderToShelfMessage.getDestShelf());

        //TODO: mandare broadcast a tutti i client
        //TODO: creare messaggio di update
        for (Pair<ClientHandler, PlayerBoard> user : players) {
            //user.getKey().send(update);
        }

        return true;
    }

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
        //TODO: nel messaggio io metterei anche il risultato dell'azione(status) per dire se è andata bene o no
        Gson gson = new Gson();
        //clientHandler.send(gson.toJson(...));
        return true;
    }

    public boolean activateProduction(ActivateProductionMessage activateProductionMessage, ClientHandler clientHandler) throws  IllegalArgumentException{
        HashMap<LeaderCard, ResourceType> leaderMap = new HashMap<>();
        PlayerBoard playerBoard = this.getPlayerBoardOfPlayer(clientHandler);
        BaseProductionParams baseProductionParams = activateProductionMessage.getBaseProduction();

        //save state of the model
        this.saveState();

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

        //getMap for leaderCard-output resource
        LeaderCard leaderCard;
        for (Map.Entry<Integer, ResourceType> index : activateProductionMessage.getLeaders().entrySet()) {
            leaderCard = playerBoard.getActiveLeaderCards().get(index.getKey());
            leaderMap.put(leaderCard, index.getValue());
        }

        try {
            playerBoard.activateProduction(devList, leaderMap, baseProductionParams.isActivated());
        } catch (IllegalArgumentException | NullPointerException e) {
            this.rollbackState();
            throw new IllegalArgumentException(e.getMessage());
        } catch (LastVaticanReportException e) {
            //TODO: gestire eccezione
        }

        return true;
    }





     /*
    ###########################################################################################################
     RESPONSE PRIVATE METHODS
    ###########################################################################################################
     */

    public void distributeLeaderCards(){
        mainBoard.giveLeaderCardsToPlayerAtGameBeginning();
    }

    private void setLastTurn(){
        this.state = GameState.LASTTURN;
    }

    /**
     * Sends an update message to all the players in the game
     * @param game the read-only model which contains the updates
     * @return true if the messages where correctly sent to all the players
     */
    private boolean sendBroadcastUpdate(Game game){
        for(Pair<ClientHandler, PlayerBoard> e: players)
            e.getKey().send(gson.toJson(game));
        return true;
    }

    private boolean sendErrorToClientHandler(ClientHandler clientHandler, String message) {
        //TODO: mandare un messaggio al client handler in questione passandogli il messaggio d'errore
        return false;
    }

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
