package it.polimi.ingsw.view.gui.mainViews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.network.messages.fromClient.CheatMessage;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.fromClient.GetProductionCostMessage;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.mainViews.panels.DevCardGetInfo;
import it.polimi.ingsw.view.gui.mainViews.panels.DevGridContainer;
import it.polimi.ingsw.view.gui.mainViews.panels.BuyFromMarketPanel;
import it.polimi.ingsw.view.gui.mainViews.dialogs.*;
import it.polimi.ingsw.view.gui.mainViews.panels.*;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class manages panels and dialogs, this class can be instantiated once
 */
public final class PanelManager {
    private final GuiClient gui;
    private final Gson gson;
    private static PanelManager instance;

    private final ExecutorService visualizer;

    private JFrame gameFrame;

    //add here dialogs
    private LoginDialog loginDialog;
    private ErrorDialog errorDialog;
    private InfoDialog infoDialog;
    private LorenzoDialog lorenzoDialog;
    private SetNumOfPlayersDialog configureGameDialog;
    private FinalResultsDialog resultsDialog;

    //add here panels created for each view
    private EntryViewPanel entryPanel;
    private WaitingRoomPanel waitingRoomPanel;
    private BeginningDecisionsPanel beginningDecisionsPanel;
    private MainPanel mainPanel;
    private DevCardGetInfo devCardCostPanel;
    private DevGridContainer devGridContainer;
    private BuyFromMarketPanel buyFromMarketPanel;
    private MoveResourceChoice moveResourceChoice;
    private MarketPlacingResources marketPlacingResources;
    private DevGridPayingCost devGridPayingCost;
    private ProductionGetResources productionGetResources;
    private ProductionGetInfo productionGetInfo;

    //add here attributes used in panels
    private String nickname;
    private Game gameModel;
    private Player player;
    private HashMap<ResourceType, Integer> resourcesMap;
    private String mapDescription;
    private int nLeadersToDiscard;
    private int resourcesToTake;
    private Map<String, Integer> results;
    private SoloActionToken lorenzoToken;
    private List<Integer> lastSelectedDevCards;
    private List<Integer> lastSelectedLeaderList;
    private int lastSelectedRow;
    private int lastSelectedCol;
    private BaseProductionParams lastSelectedBaseProdParams;

    private PanelManager(GuiClient gui) {
        this.gameModel = null;
        this.gui = gui;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement");
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect");
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken");
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");


        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();

        this.visualizer = Executors.newCachedThreadPool();
    }

    /**
     * get the instance of the previously created panelManager
     *
     * @return the instance of the created panelManager
     * @throws IllegalStateException if the panel manager is not yet instantiated, try using createInstance
     */
    public static PanelManager getInstance() throws IllegalStateException {
        if (instance == null) {
            synchronized (PanelManager.class) {
                if (instance == null)
                    throw new IllegalStateException("Panel manager instance is null");
            }
        }
        return instance;
    }


    /**
     * Instances a new Panel Manager
     *
     * @param gui used to send commands to the server
     * @return the panel manager instantiated
     * @throws IllegalStateException if this method is called more than once, try using getInstance
     */
    public static PanelManager createInstance(GuiClient gui) throws IllegalStateException {
        if (instance == null) {
            synchronized (PanelManager.class) {
                if (instance == null) {
                    instance = new PanelManager(gui);
                    return instance;
                }
            }
        }
        throw new IllegalStateException("Panel manager instance is not null");
    }

    /**
     * usedOnly for Test Purpose
     */
    @Deprecated
    public void initTest() {
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(600, 600);
        gameFrame.setVisible(false);
    }

    /**
     * Initialize panels and dialogs, set up the view to start a new game
     */
    public void init() {
        /*Initializing frame, panels....*/
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //gameFrame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame.setSize(screenSize.width, screenSize.height - 100);

        //add here dialog with gameframe owner
        loginDialog = new LoginDialog(gameFrame);
        configureGameDialog = new SetNumOfPlayersDialog(gameFrame);
        infoDialog = new InfoDialog(gameFrame);
        errorDialog = new ErrorDialog(gameFrame);
        lorenzoDialog = new LorenzoDialog(gameFrame);
        resultsDialog = new FinalResultsDialog(gameFrame);

        //panels for the frame
        entryPanel = new EntryViewPanel();
        gameFrame.add(entryPanel);

        waitingRoomPanel = new WaitingRoomPanel();
        gameFrame.add(waitingRoomPanel);

        mainPanel = new MainPanel();
        gameFrame.add(mainPanel);

        devCardCostPanel = new DevCardGetInfo();
        gameFrame.add(devCardCostPanel);
        devCardCostPanel.setVisible(false);

        buyFromMarketPanel = new BuyFromMarketPanel();
        gameFrame.add(buyFromMarketPanel);
        buyFromMarketPanel.setVisible(false);

        moveResourceChoice = new MoveResourceChoice(true);
        gameFrame.add(moveResourceChoice);

        marketPlacingResources = new MarketPlacingResources(true);

        devGridPayingCost = new DevGridPayingCost();

        gameFrame.validate();

        gameFrame.setVisible(true);
        entryPanel.setVisible(true);

    }

    /**
     * makes login dialog visible
     */
    public void seeLoginPopup() {
        loginDialog.setVisible(true);
    }


    /**
     * Writes a new message to the server through gui
     */
    public void writeMessage(Command command) {
        gui.sendMessage(command);
    }

    /**
     * Makes logout message visible, Stops th communication with the server.
     *
     * @param logoutMessage the string to be displayed
     */
    public void printLogout(String logoutMessage) {
        infoDialog.setInfoMessage(logoutMessage);
        infoDialog.setVisible(true);
        visualizer.shutdown();
        gameFrame.dispose();
    }

    /**
     * method to be called when in a panel is called the quitCommand or the game ends*
     */
    public void manageLogoutCommand() {
        writeMessage(new Command("quit"));
        gui.quitCommand();
        //writeMessage(new Command("quit"));
    }

    /**
     * Method called when a cui related message from the server is received
     *
     * @param responseMessage the message received
     */
    public void readMessage(ResponseMessage responseMessage) {
        String responseContent = responseMessage.getResponseContent();
        switch (responseMessage.getResponseType()) {
            case UPDATE:
                this.manageUpdate(responseContent);
                break;
            case ERROR:
                this.manageError(responseContent);
                break;
            case INFOSTRING:
                this.manageInfo(responseContent);
                break;
            case EXTRARESOURCEANDLEADERCARDBEGINNING:
                this.manageStart(responseContent);
                break;
            case HASHMAPRESOURCESFROMDEVGRID:
                this.manageCardCost(responseContent);
                break;
            case HASHMAPRESOURCESFROMMARKET:
                this.manageMarketResources(responseContent);
                break;
            case HASHMAPRESOURCESFROMPRODCOST:
                this.manageProductionResources(responseContent);
                break;
            case ASKFORNUMPLAYERS:
                this.manageGameConfiguration();
                break;
            case SETNICK:
                this.manageNickSetting(responseContent);
                break;
            case SETBEGINNINGDECISIONS:
                this.managePostStart();
                break;
            case FINALSCORES:
                this.manageleaderboard(responseContent);
                break;
            case SETNUMPLAYERCONF:
                this.manageConfigurationDone();
                break;
            case LORENZOSACTION:
                this.manageLorenzoAction(responseContent);
                break;
            case PLAYERCONNECTIONUPDATE:
                this.manageConnectionUpdate(responseContent);
                break;
            case SOLOGAMERESULT:
                this.manageSoloGameResult(responseContent);
                break;
            default:
                System.out.println("unmanaged respone:\t" + responseMessage);
                System.exit(1);
        }
    }

    /**
     * Shows the dialog with the result for solo game
     *
     * @param responseContent the result sent by the server
     */
    private void manageSoloGameResult(String responseContent) {
        SoloGameResultMessage soloGameResultMessage = gson.fromJson(responseContent, SoloGameResultMessage.class);
        visualizer.submit(() -> {
            resultsDialog.setMultilineText(soloGameResultMessage.getMessage());
            resultsDialog.setVisible(true);

        });
    }

    /**
     * Manages the re-connection of a disconnected player: it shows the correct panel based on the previous state of the player
     *
     * @param responseContent the message sent by the server to signal the re-connection of the player
     */
    private void manageConnectionUpdate(String responseContent) {
        synchronized (this) {
            PlayerConnectionsUpdate playerConnectionsUpdate = gson.fromJson(responseContent, PlayerConnectionsUpdate.class);
            Game update = playerConnectionsUpdate.getUpdate();
            if (this.gameModel == null) {
                gameModel = update;
                this.setPlayerAndViews(gameModel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().get());
            } else
                gameModel.merge(update);

            if (playerConnectionsUpdate.getChangedPlayer().equals(this.nickname)) {
                if (this.player.getPlayerState() == PlayerState.WAITING4TURN || this.player.getPlayerState() == PlayerState.WAITING4LASTTURN || this.player.getPlayerState() == PlayerState.WAITING4GAMEEND) {
                    waitingRoomPanel.setVisible(false);
                    managePostStart();
                }
            }
        }
    }

    /**
     * Shows a dialog with the Lorenzo's action
     *
     * @param responseContent the message sent by the server with the Lorenzo's action
     */
    private void manageLorenzoAction(String responseContent) {
        synchronized (this) {
            LorenzosActionMessage lorenzosActionMessage = gson.fromJson(responseContent, LorenzosActionMessage.class);
            lorenzoToken = lorenzosActionMessage.getSoloActionToken();
        }
        printLorenzosAction(lorenzoToken.getName());
    }

    /**
     * Prints the info for the Lorenzo's action in the dialog, such as the image of the drew token
     *
     * @param path the image of the drew token
     */
    private void printLorenzosAction(String path) {
        visualizer.submit(() -> {
            lorenzoDialog.setInfoMessage("Lorenzo's action");
            lorenzoDialog.setTokenImage(path);
            lorenzoDialog.setVisible(true);
        });
    }

    /**
     * Shows the dialog for the result of a multiplayer game
     *
     * @param responseContent the message sent by the client with the final scores
     */
    private void manageleaderboard(String responseContent) {
        FinalScoresMessage message = gson.fromJson(responseContent, FinalScoresMessage.class);
        visualizer.submit(() -> {
            resultsDialog.setFinalScores(message.getResults());
            resultsDialog.setVisible(true);
        });
    }

    /**
     * Shows the dialog for the configuration of the game
     */
    private void manageConfigurationDone() {
        configureGameDialog.setVisible(false);
    }

    /**
     * Shows the waiting room after the nickname of the player has been set
     *
     * @param responseContent the message sent by the server with the confirmation of the nickname of the player
     */
    private void manageNickSetting(String responseContent) {
        synchronized (this) {
            LoginConfirmationMessage setNickMessage = gson.fromJson(responseContent, LoginConfirmationMessage.class);
            nickname = setNickMessage.getConfirmedNickname();
        }

        loginDialog.setVisible(false);
        visualizer.submit(() -> {
            entryPanel.setVisible(false);
            waitingRoomPanel.setVisible(true);
        });

    }

    /**
     * Shows the dialog that allows to configure the game
     */
    private void manageGameConfiguration() {
        visualizer.submit(() -> {
            configureGameDialog.setVisible(true);
            //System.out.println("Ended GameConfiguration set visible true");
        });

    }

    /**
     * Sends a new getProductionCost to the server
     *
     * @param devCards the desired DevCards
     * @param leader   the desired active leaders
     * @param baseProd the desired baseproductionparams
     */
    public void manageProductionInfos(List<Integer> devCards, List<Integer> leader, BaseProductionParams baseProd) {
        synchronized (this) {
            this.lastSelectedLeaderList = leader;
            this.lastSelectedDevCards = devCards;
            this.lastSelectedBaseProdParams = baseProd;
        }

        this.writeMessage(new Command("getProductionCost", new GetProductionCostMessage(devCards, leader, baseProd)));
    }

    /**
     * Shows the panel that allows to choose where the resources for the production come from
     *
     * @param responseContent the message from the server with the cost of the production
     */
    private void manageProductionResources(String responseContent) {
        synchronized (this) {
            HashMapResFromProdCostMessage message = gson.fromJson(responseContent, HashMapResFromProdCostMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "resourcesProduced";
            productionGetResources = new ProductionGetResources(resourcesMap, lastSelectedLeaderList, lastSelectedDevCards, lastSelectedBaseProdParams);
        }
        gameFrame.add(productionGetResources);
        gameFrame.revalidate();

        productionGetInfo.setVisible(false);
        productionGetResources.setVisible(true);
    }

    /**
     * Shows the panel that allows to put the resourcec got from the market in the desired place
     *
     * @param responseContent the message sent by the server with the resources to place
     */
    private void manageMarketResources(String responseContent) {
        synchronized (this) {
            HashMapResFromMarketMessage message = gson.fromJson(responseContent, HashMapResFromMarketMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "MarketResource";
        }

        //gameFrame.remove(marketPlacingResources);

        /*System.out.println("MESSAGE FROM SERVER: ");
        for (Map.Entry<ResourceType, Integer> e : this.resourcesMap.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());*/

        marketPlacingResources = new MarketPlacingResources(resourcesMap, lastSelectedRow + 1, lastSelectedCol + 1, lastSelectedLeaderList);

        gameFrame.add(marketPlacingResources);
        gameFrame.revalidate();

        buyFromMarketPanel.setVisible(false);
        marketPlacingResources.setVisible(true);
    }

    /**
     * Shows the panel that allows to choose where the resources to pay the desired devCard came from
     *
     * @param responseContent the message sent by the server with the cost of the devCard
     */
    private void manageCardCost(String responseContent) {
        synchronized (this) {
            HashMapResFromDevGridMessage message = gson.fromJson(responseContent, HashMapResFromDevGridMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "DevCardCost";
        }

        devGridPayingCost = new DevGridPayingCost(resourcesMap, lastSelectedRow, lastSelectedCol, lastSelectedLeaderList);
        gameFrame.add(devGridPayingCost);
        gameFrame.revalidate();

        devCardCostPanel.setVisible(false);
        devGridPayingCost.setVisible(true);
    }

    /**
     * Shows the panel that allows to do the initial decisions of the game
     *
     * @param responseContent the message sent by the server with the 4 leader card of the player and the amount of resources to put in the depot
     */
    private void manageStart(String responseContent) {
        synchronized (this) {
            ExtraResAndLeadToDiscardBeginningMessage message = gson.fromJson(responseContent, ExtraResAndLeadToDiscardBeginningMessage.class);
            nLeadersToDiscard = message.getNumLeader();
            resourcesToTake = message.getNumRes();
        }

        List<LeaderCard> leaderList = new ArrayList<>();
        for (Player player : gameModel.getPlayers())
            if (player.getNickName().equals(nickname))
                leaderList = player.getUnUsedLeaders();

        ArrayList<String> leaderUrls = new ArrayList<>();
        for (LeaderCard leader : leaderList)
            leaderUrls.add(leader.getUrl());
        beginningDecisionsPanel = new BeginningDecisionsPanel(leaderUrls, resourcesToTake, nLeadersToDiscard);

        visualizer.submit(() -> {
            gameFrame.add(beginningDecisionsPanel);
            waitingRoomPanel.setVisible(false);
            beginningDecisionsPanel.setVisible(true);
        });
    }

    /**
     * Shows the main panel of the game with all the controls and the playerboard
     */
    private synchronized void managePostStart() {
        synchronized (this) {
            nLeadersToDiscard = 0;
            resourcesToTake = 0;
        }

        List<String> nicknameList = new ArrayList<>();
        Player actualPlayer = null;
        for (Player player : gameModel.getPlayers()) {
            if (player.getNickName().equals(nickname))
                actualPlayer = player;
            nicknameList.add(player.getNickName());
        }

        gameFrame.remove(mainPanel);
        mainPanel = new MainPanel(nicknameList, actualPlayer, gameModel);
        gameFrame.add(mainPanel);
        gameFrame.revalidate();

        visualizer.submit(() -> {
            beginningDecisionsPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
    }

    /**
     * Closes the panel passed as parameter and displays the main panel
     *
     * @param jPanel the panel to close
     */
    public void showPlayerBoard(JPanel jPanel) {
        jPanel.setVisible(false);
        mainPanel.setVisible(true);
    }

    /**
     * Shows the panel that allows to move the resources between shelves; between depot and leader card and vice versa
     */
    public void showMoveResources() {
        mainPanel.setVisible(false);
        moveResourceChoice = new MoveResourceChoice();
        gameFrame.add(moveResourceChoice);
        moveResourceChoice.setVisible(true);
    }

    /**
     * Shows the panel that allows to choose the leader cards to use while buying the specified devCard
     *
     * @param row the row of the desired dev card in the grid
     * @param col the column of the desired dev card in the grid
     * @throws IllegalArgumentException if an argument is not valid
     * @throws IllegalStateException    if the action can't be done without errors
     */
    public void printGetCardCostPanel(int row, int col) throws IllegalArgumentException, IllegalStateException {
        visualizer.submit(() -> {
            this.getDevCardCostPanel().selectCell(row, col);
            this.getDevCardCostPanel().print();
        });
    }

    /**
     * Sends a message to the server with all the information required to get the cost of a dev card in the grid
     *
     * @param row        the row of the desired dev card in the grid
     * @param col        the column of the desired dev card in the grid
     * @param leaderList the list of leader card to use
     */
    public synchronized void manageGetCardCost(int row, int col, List<Integer> leaderList) {
        this.lastSelectedRow = row;
        this.lastSelectedCol = col;
        this.lastSelectedLeaderList = leaderList;
        System.out.println((lastSelectedRow + 1) + " " + (lastSelectedCol + 1) + " " + lastSelectedLeaderList);
        writeMessage(new Command("getCardCost", new GetFromMatrixMessage(lastSelectedRow + 1, lastSelectedCol + 1, lastSelectedLeaderList)));
    }

    /**
     * Sends a message to the server to activate cheats
     */
    public void manageCheat(){
        writeMessage(new Command("activateCheat", new CheatMessage(99)));
    }

    /**
     * Sends a message to the server with all the information required to get the resources from a line in the market
     *
     * @param isRow  if the selected line of the market is a row
     * @param number the number of the row
     */
    public synchronized void manageGetResourcesFromMarket(boolean isRow, int number) {
        this.lastSelectedLeaderList = buyFromMarketPanel.getLeaderList();
        int row;
        int col;
        if (isRow) {
            this.lastSelectedRow = number;
            this.lastSelectedCol = -1;
            row = number + 1;
            col = 0;
        } else {
            this.lastSelectedRow = -1;
            this.lastSelectedCol = number;
            col = number + 1;
            row = 0;
        }

        System.out.println(row + " " + col + " " + lastSelectedLeaderList);
        writeMessage(new Command("getResourcesFromMarket", new GetFromMatrixMessage(row, col, lastSelectedLeaderList)));
    }

    /**
     * Displays the DevGrid panel
     */
    public void displayDevGrid() {
        mainPanel.setVisible(false);
        devGridContainer = new DevGridContainer(gameModel.getMainBoard());
        gameFrame.add(devGridContainer);
        devGridContainer.setVisible(true);
        devGridContainer.update(150, 200);
    }

    /**
     * Displays the production Panel
     */
    public void displayProduction() {
        mainPanel.setVisible(false);
        if (productionGetInfo != null) {
            gameFrame.remove(productionGetInfo);
        }
        productionGetInfo = new ProductionGetInfo(player);
        gameFrame.add(productionGetInfo);
        productionGetInfo.setVisible(true);
        gameFrame.revalidate();
    }

    /**
     * Displays the market
     */
    public void displayMarket() {
        mainPanel.setVisible(false);
        gameFrame.remove(buyFromMarketPanel);
        buyFromMarketPanel = new BuyFromMarketPanel();
        buyFromMarketPanel.setPlayer(getPlayer());
        buyFromMarketPanel.setBoard(gameModel.getMainBoard());
        gameFrame.add(buyFromMarketPanel);
        buyFromMarketPanel.setVisible(true);
        buyFromMarketPanel.print();
        gameFrame.revalidate();
    }

    /**
     * if infoDialog is visible the string is added at the end, if the dialog is not visible,
     * the string will be the only string in the dialog and the dialog will be set visible
     *
     * @param info the string to be displayed in InfoPanel
     */
    public synchronized void printInfo(String info) {
        visualizer.submit(() -> {
            infoDialog.setInfoMessage(info);
            infoDialog.setVisible(true);
        });
    }

    /**
     * Shows a dialog with a string message passed as parameter
     *
     * @param responseContent the message sent by the server with the string to print
     */
    private void manageInfo(String responseContent) {
        visualizer.submit(() -> {
            GeneralInfoStringMessage errorMessage = gson.fromJson(responseContent, GeneralInfoStringMessage.class);
            String info = errorMessage.getMessage();
            infoDialog.setInfoMessage(info);
            infoDialog.setVisible(true);
        });

    }

    /**
     * if errorDialog is visible the string is added at the end, if the dialog is not visible,
     * the string will be the only string in the dialog and the dialog will be set visible
     *
     * @param error the string to be displayed in errorPanel
     */
    public void printError(String error) {
        visualizer.submit(() -> {
            errorDialog.setErrorMessage(error);
            errorDialog.setVisible(true);
        });
    }

    /**
     * Shows a dialog with an error message sent by the server
     *
     * @param responseContent the message sent by the server with the error to print
     */
    private void manageError(String responseContent) {
        visualizer.submit(() -> {
            ErrorMessage errorMessage = gson.fromJson(responseContent, ErrorMessage.class);
            String error = errorMessage.getErrorMessage();
            errorDialog.setErrorMessage(error);
            errorDialog.setVisible(true);
        });
    }

    /**
     * Sets the player for the for this class and for the getDevCardCostPanel
     *
     * @param player the player to set
     */
    private void setPlayerAndViews(Player player) {
        this.player = player;
        this.getDevCardCostPanel().setPlayer(this.player);
    }

    /**
     * Manages the message with the updates of the model: it merges the old model with the new info, or it creates a new ine
     * if the model doesn't already exists; it shows a message if the turn has changed; closes other panels and shows the
     * main panel
     *
     * @param responseContent the message sent by the server with the update of the model
     */
    private synchronized void manageUpdate(String responseContent) {
        PlayerState oldPlayerState = null;
        PlayerState newPlayerState;

        if (player != null)
            oldPlayerState = player.getPlayerState();
        ModelUpdate modelUpdate = gson.fromJson(responseContent, ModelUpdate.class);
        Game update = modelUpdate.getGame();
        if (this.gameModel == null) {
            gameModel = update;
            this.setPlayerAndViews(gameModel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().get());
        } else
            gameModel.merge(update);

        newPlayerState = gameModel.findByNick(nickname).getPlayerState();
        if (player == null && (newPlayerState == PlayerState.PLAYING || newPlayerState == PlayerState.PLAYINGBEGINNINGDECISIONS))
            printInfo("Now it's your turn, Master " + nickname + "!");
        else if (player != null) {
            if ((oldPlayerState != PlayerState.PLAYING && oldPlayerState != PlayerState.PLAYINGBEGINNINGDECISIONS) && (newPlayerState == PlayerState.PLAYING || newPlayerState == PlayerState.PLAYINGBEGINNINGDECISIONS))
                printInfo("Now it's your turn, Master " + nickname + "!");
        }

        //This remove are needed because we have to close these panels if they're open.
        gameFrame.remove(moveResourceChoice);
        gameFrame.remove(marketPlacingResources);
        gameFrame.remove(devGridPayingCost);
        if (productionGetResources != null)
            gameFrame.remove(productionGetResources);

        if (mainPanel.getCreated()) {
            gameFrame.remove(mainPanel);
            List<String> nicknameList = new ArrayList<>();
            Player actualPlayer = null;
            for (Player player : gameModel.getPlayers()) {
                if (player.getNickName().equals(nickname))
                    actualPlayer = player;
                nicknameList.add(player.getNickName());
            }
            mainPanel = new MainPanel(nicknameList, actualPlayer, gameModel);
            gameFrame.add(mainPanel);
        }

        gameFrame.revalidate();
    }

    public Game getGameModel() {
        return gameModel;
    }

    public String getNickname() {
        return nickname;
    }

    /*public HashMap<ResourceType, Integer> getResourcesMap() {
        return resourcesMap;
    }

    public String getMapDescription() {
        return mapDescription;
    }*/

    public int getnLeadersToDiscard() {
        return nLeadersToDiscard;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    /*public SoloActionToken getLorenzoToken() {
        return lorenzoToken;
    }*/

    public JFrame getGameFrame() {
        return gameFrame;
    }

    /*public LoginDialog getLoginDialog() {
        return loginDialog;
    }

    public ErrorDialog getErrorDialog() {
        return errorDialog;
    }*/

    public InfoDialog getInfoDialog() {
        return infoDialog;
    }

    public SetNumOfPlayersDialog getConfigureGameDialog() {
        return configureGameDialog;
    }

    /*public EntryViewPanel getEntryPanel() {
        return entryPanel;
    }

    public WaitingRoomPanel getWaitingRoomPanel() {
        return waitingRoomPanel;
    }*/

    public DevCardGetInfo getDevCardCostPanel() {
        return devCardCostPanel;
    }

    /*public BuyFromMarketPanel getBuyFromMarketPanel() {
        return buyFromMarketPanel;
    }*/

    public int getResourcesToTake() {
        return resourcesToTake;
    }

    /**
     * Returns the player's depot shelves
     *
     * @return the player's depot shelves
     */
    //In order to have the DnD properly function, the DepotShelf must be present in the GameModel (at least they can be empty)
    public List<DepotShelf> getDepotShelves() {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getDepotShelves();
    }

    /**
     * Returns the player's depot shelves of the selected player
     *
     * @param playerNick the nickname of the player from which you want retrieve the depot shelves
     * @return the player's depot shelves of the selected player
     */
    public List<DepotShelf> getDepotShelves(String playerNick) {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(playerNick)).findAny().get().getDepotShelves();
    }

    /**
     * Returns the player's StrongBox
     *
     * @return the player's StrongBox
     */
    public HashMap<ResourceType, Integer> getStrongBox() {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getStrongBox();
    }

    /**
     * Returns the player's StrongBox
     *
     * @param playerNick the nickname of the player from which you want retrieve the strongbox
     * @return the player's StrongBox
     */
    public HashMap<ResourceType, Integer> getStrongBox(String playerNick) {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(playerNick)).findAny().get().getStrongBox();
    }

    /**
     * Returns the list of active LeaderCards the player holds which have an ExtraSlot effect
     *
     * @return a list of the active LeaderCards with an ExtraSlot effect the player has
     */
    public List<LeaderCard> getExtraSlotActiveLeaderCards() {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getUsedLeaders().stream().filter(x -> x.getEffect() instanceof ExtraSlotLeaderEffect).collect(Collectors.toList());
    }

    /**
     * Returns how many resources are already stored in the LeaderCard with an ExtraSlot effect that can store the specified kind of resource
     *
     * @param type the kind of resource the desired LeaderCard can store
     * @return how many resources are already stored in the desired LeaderCard
     */
    public int getAlreadyStoredInLeaderSlot(ResourceType type) {
        Integer result = gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getLeaderSlots().get(type);
        if (result == null)
            return 0;
        return result;
    }

    /**
     * Returns the player's LeaderSlots
     *
     * @return the player's LeaderSlots
     */
    public HashMap<ResourceType, Integer> getLeaderSlots() {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getLeaderSlots();
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the DevCard of the player that are visible in the DevSlots (and therefore the user can use)
     *
     * @return the visible DevCards of the player
     */
    public HashMap<Integer, DevCard> getTopDevCardInDevSlot() {
        return this.player.getDevSlots().getTopCards();
    }

    //public Player getPlayer(){return player;}

    public void setPlayer(Player player) {
        this.player = player;
    }

    //For testing purposes:

    public void setGameModel(Game gameModel) {
        this.gameModel = gameModel;
    }

    public void setResourcesToTake(int resourcesToTake) {
        this.resourcesToTake = resourcesToTake;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
