package it.polimi.ingsw.view.gui.panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.fromClient.GetProductionCostMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevCardPanel1;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridContainer;
import it.polimi.ingsw.view.gui.ViewComponents.market.BuyFromMarketPanel;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public final class PanelManager {
    private final GuiClient gui;
    private final Gson gson;
    private static PanelManager instance;

    private ExecutorService visualizer;

    private JFrame gameFrame;

    //TODO: add here dialogs
    private LoginDialog loginDialog;
    private ErrorDialog errorDialog;
    private InfoDialog infoDialog;
    private LorenzoDialog lorenzoDialog;
    private SetNumOfPlayersDialog configureGameDialog;

    //TODO: add here panels created for each view
    private EntryViewPanel entryPanel;
    private WaitingRoomPanel waitingRoomPanel;
    private BeginningDecisionsPanel beginningDecisionsPanel;
    private MainPanel mainPanel;
    private DevCardPanel1 devCardCostPanel;
    private DevGridContainer devGridContainer;
    private BuyFromMarketPanel buyFromMarketPanel;
    private MoveResourceChoice moveResourceChoice;
    private MarketPlacingResources marketPlacingResources;
    private DevGridPayingCost devGridPayingCost;
    private ProductionGetResources productionGetResources;
    private ProductionGetInfo productionGetInfo;

    //TODO: add here attibutes used in panels
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
    /*idk if can be useful selected cell, row, column attributes and relatives methods*/


    private PanelManager(GuiClient gui) {
        this.gameModel = null;
        this.gui = gui;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement"); //TODO: this is only for testing purpose, in the real game we won't have requirements of type Requirement but a subtype of it
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect"); //TODO: this is only for testing purpose, in the real game we won't have effect of type Effect but a subtype of it
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken"); //TODO: this is only for testing purpose, in the real game we won't have token of type SoloActionToken but a subtype of it
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");


        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();

        this.visualizer = Executors.newCachedThreadPool();
    }

    public static PanelManager getInstance() {
        if (instance == null) {
            synchronized (PanelManager.class) {
                if (instance == null)
                    throw new IllegalStateException("Panel manager instance is null");
            }
        }
        return instance;
    }

    public static PanelManager createInstance(GuiClient gui) {
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

    public void init() throws IOException {
        /*Initializing frame, panels....*/
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //gameFrame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame.setSize(screenSize.width, screenSize.height - 100);

        //TODO: add here dialog with gameframe owner
        loginDialog = new LoginDialog(gameFrame);
        configureGameDialog = new SetNumOfPlayersDialog(gameFrame);
        infoDialog = new InfoDialog(gameFrame);
        errorDialog = new ErrorDialog(gameFrame);
        lorenzoDialog = new LorenzoDialog(gameFrame);

        //TODO: add here panels to frame
        entryPanel = new EntryViewPanel();
        gameFrame.add(entryPanel);

        waitingRoomPanel = new WaitingRoomPanel();
        gameFrame.add(waitingRoomPanel);

        mainPanel = new MainPanel();
        gameFrame.add(mainPanel);

        devCardCostPanel = new DevCardPanel1();
        gameFrame.add(devCardCostPanel);
        devCardCostPanel.setVisible(false);

        buyFromMarketPanel = new BuyFromMarketPanel();
        gameFrame.add(buyFromMarketPanel);
        buyFromMarketPanel.setVisible(false);

        moveResourceChoice = new MoveResourceChoice(true);
        gameFrame.add(moveResourceChoice);

        marketPlacingResources = new MarketPlacingResources(true);

        devGridPayingCost = new DevGridPayingCost();

        //ProductionGetInfo must be created each time must be used
        //productionGetInfo = new ProductionGetInfo();

        gameFrame.validate();

        gameFrame.setVisible(true);
        entryPanel.setVisible(true);

    }

    public void seeLoginPopup() {
        loginDialog.setVisible(true);
    }


    public void writeMessage(Command command) {
        gui.sendMessage(command);
    }

    public void printLogout(String logoutMessage) {
        infoDialog.setInfoMessage(logoutMessage);
        infoDialog.setVisible(true);
        visualizer.shutdown();
        gameFrame.dispose();
    }

    /*method to be called when in a panel is called the quitCommand*/
    public void manageLogoutCommand() {
        writeMessage(new Command("quit"));
        gui.quitCommand();
    }

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
                this.managePostStart(responseContent);
                break;
            case FINALSCORES:
                this.manageleaderboard(responseContent);
                break;
            case SETNUMPLAYERCONF:
                this.manageConfigurationDone(responseContent);
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

    private void manageSoloGameResult(String responseContent) {
        synchronized (this) {
            SoloGameResultMessage soloGameResultMessage = gson.fromJson(responseContent, SoloGameResultMessage.class);
        }
        //TODO: mostrare panel con risultato finale;
    }

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
                //TODO: aggiungere cosa far rivedere a chi si riconnette, questo sotto NON funziona
                this.showPlayerBoard(this.waitingRoomPanel);
            }
        }
    }

    private void manageLorenzoAction(String responseContent) {
        synchronized (this) {
            LorenzosActionMessage lorenzosActionMessage = gson.fromJson(responseContent, LorenzosActionMessage.class);
            lorenzoToken = lorenzosActionMessage.getSoloActionToken();
        }
        printLorenzosAction(lorenzoToken.getName());
    }

    private void printLorenzosAction(String path){
        visualizer.submit(() -> {
            lorenzoDialog.setInfoMessage("Lorenzo's action");
            lorenzoDialog.setTokenImage(path);
            lorenzoDialog.setVisible(true);
        });
    }

    private void manageleaderboard(String responseContent) {
        synchronized (this) {
            FinalScoresMessage message = gson.fromJson(responseContent, FinalScoresMessage.class);
            this.results = message.getResults();
        }
        //TODO: do things to setup view

    }

    private void manageConfigurationDone(String responseContent) {
        configureGameDialog.setVisible(false);
    }

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

    private void manageGameConfiguration() {
        visualizer.submit(() -> {
            configureGameDialog.setVisible(true);
            System.out.println("Ended GameConfiguration set visible true");
        });

    }

    public void manageProductionInfos(List<Integer> devCards, List<Integer> leader, BaseProductionParams baseProd){
        this.lastSelectedLeaderList = leader;
        this.lastSelectedDevCards = devCards;
        this.lastSelectedBaseProdParams = baseProd;

        this.writeMessage(new Command("getProductionCost", new GetProductionCostMessage(devCards,leader, baseProd)));
    }

    private void manageProductionResources(String responseContent) {
        synchronized (this) {
            HashMapResFromProdCostMessage message = gson.fromJson(responseContent, HashMapResFromProdCostMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "resourcesProduced";
        }
        productionGetResources = new ProductionGetResources(resourcesMap, lastSelectedLeaderList, lastSelectedDevCards, lastSelectedBaseProdParams);

        //TODO: aggiungere al panel
        //TODO: aggiungere al panel

    }

    private void manageMarketResources(String responseContent) {
        synchronized (this) {
            HashMapResFromMarketMessage message = gson.fromJson(responseContent, HashMapResFromMarketMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "MarketResource";
        }

        //gameFrame.remove(marketPlacingResources);

        System.out.println("MESSAGE FROM SERVER: ");
        for (Map.Entry<ResourceType, Integer> e : this.resourcesMap.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());

        marketPlacingResources = new MarketPlacingResources(resourcesMap, lastSelectedRow + 1, lastSelectedCol + 1, lastSelectedLeaderList);

        gameFrame.add(marketPlacingResources);
        gameFrame.revalidate();

        buyFromMarketPanel.setVisible(false);
        marketPlacingResources.setVisible(true);

        //TODO: add this panel to the frame

    }

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
        //TODO: add this panel to the frame

        //TODO: aggiungere bottone back sulla schermata!

    }

    private void manageStart(String responseContent) {
        synchronized (this) {
            ExtraResAndLeadToDiscardBeginningMessage message = gson.fromJson(responseContent, ExtraResAndLeadToDiscardBeginningMessage.class);
            nLeadersToDiscard = message.getNumLeader();
            resourcesToTake = message.getNumRes();
        }

        //TODO: do things to setup view
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

    private synchronized void managePostStart(String responseContent) {
        synchronized (this) {
            nLeadersToDiscard = 0;
            resourcesToTake = 0;
        }

        //TODO: do things to remove manageStartView and setup new view
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

    public void showPlayerBoard(JPanel jPanel) {
        jPanel.setVisible(false);
        mainPanel.setVisible(true);
    }

    public void showMoveResources() {
        mainPanel.setVisible(false);
        moveResourceChoice = new MoveResourceChoice();
        gameFrame.add(moveResourceChoice);
        moveResourceChoice.setVisible(true);
    }

    public void printGetCardCostPanel(int row, int col) throws IllegalArgumentException, IllegalStateException {
        visualizer.submit(() -> {
            this.getDevCardCostPanel().selectCell(row, col);
            this.getDevCardCostPanel().print();
        });
    }

    public synchronized void manageGetCardCost(int row, int col, List<Integer> leaderList) {
        this.lastSelectedRow = row;
        this.lastSelectedCol = col;
        this.lastSelectedLeaderList = leaderList;
        System.out.println((lastSelectedRow + 1) + " " + (lastSelectedCol + 1) + " " + lastSelectedLeaderList);
        writeMessage(new Command("getCardCost", new GetFromMatrixMessage(lastSelectedRow + 1, lastSelectedCol + 1, lastSelectedLeaderList)));
    }

    public synchronized void manageGetResourcesFromMarket(boolean isRow, int number) {
        this.lastSelectedLeaderList = buyFromMarketPanel.getLeaderList();
        int row;
        int col;
        if (isRow == true) {
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

    public void displayDevGrid() {
        //visualizer.submit(() -> {
        mainPanel.setVisible(false);
        devGridContainer = new DevGridContainer(gameModel.getMainBoard());
        gameFrame.add(devGridContainer);
        devGridContainer.setVisible(true);
        devGridContainer.update(150, 200);
        // });
    }

    public void displayProduction() {
        visualizer.submit(() -> {

            if (productionGetInfo != null) {
                gameFrame.remove(productionGetInfo);
            }
            productionGetInfo = new ProductionGetInfo(player);
            gameFrame.add(productionGetInfo);
            gameFrame.revalidate();
            mainPanel.setVisible(false);
            productionGetInfo.setVisible(true);
        });
    }

    //TODO: this must be changed we shound't print just the market but the action view

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

    public synchronized void printInfo(String info) {
        visualizer.submit(() -> {
            infoDialog.setInfoMessage(info);
            infoDialog.setVisible(true);
        });
    }

    private void manageInfo(String responseContent) {
        visualizer.submit(() -> {
            GeneralInfoStringMessage errorMessage = gson.fromJson(responseContent, GeneralInfoStringMessage.class);
            String info = errorMessage.getMessage();
            infoDialog.setInfoMessage(info);
            infoDialog.setVisible(true);
        });

    }

    public void printError(String error) {
        visualizer.submit(() -> {
            errorDialog.setErrorMessage(error);
            errorDialog.setVisible(true);
        });
    }

    private void manageError(String responseContent) {
        visualizer.submit(() -> {
            ErrorMessage errorMessage = gson.fromJson(responseContent, ErrorMessage.class);
            String error = errorMessage.getErrorMessage();
            errorDialog.setErrorMessage(error);
            errorDialog.setVisible(true);
        });
    }

    private void setPlayerAndViews(Player player) {
        this.player = player;
        this.getDevCardCostPanel().setPlayer(this.player);
    }

    private synchronized void manageUpdate(String responseContent) {
        PlayerState oldPlayerState = null;
        PlayerState newPlayerState;

        //TODO: questo sync interno è ridondante
        synchronized (this) {
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
            //TODO: add turn info dialog
        }

        //This remove are needed because we have to close these panels if they're open.
        gameFrame.remove(moveResourceChoice);
        gameFrame.remove(marketPlacingResources);
        gameFrame.remove(devGridPayingCost);

        gameFrame.revalidate();


        //TODO: do things to setup view
        //TODO: handle reconnection case: we have to set the main panel
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
            gameFrame.revalidate();
        }

        /*visualizer.submit(() -> {
            // if(mainPanel!=null) {
            if (mainPanel.getCreated()) {
                mainPanel.updateMainPanel(gameModel);
            }
        });*/
    }

    /*public void closeBuyFromMarket() {
        buyFromMarketPanel.setVisible(false);
        marketPlacingResources.setVisible(false);
        gameFrame.remove(marketPlacingResources);
        gameFrame.revalidate();
    }*/

    public Game getGameModel() {
        return gameModel;
    }

    public String getNickname() {
        return nickname;
    }

    public HashMap<ResourceType, Integer> getResourcesMap() {
        return resourcesMap;
    }

    public String getMapDescription() {
        return mapDescription;
    }

    public int getnLeadersToDiscard() {
        return nLeadersToDiscard;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public SoloActionToken getLorenzoToken() {
        return lorenzoToken;
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public LoginDialog getLoginDialog() {
        return loginDialog;
    }

    public ErrorDialog getErrorDialog() {
        return errorDialog;
    }

    public InfoDialog getInfoDialog() {
        return infoDialog;
    }

    public SetNumOfPlayersDialog getConfigureGameDialog() {
        return configureGameDialog;
    }

    public EntryViewPanel getEntryPanel() {
        return entryPanel;
    }

    public WaitingRoomPanel getWaitingRoomPanel() {
        return waitingRoomPanel;
    }

    public DevCardPanel1 getDevCardCostPanel() {
        return devCardCostPanel;
    }

    public BuyFromMarketPanel getBuyFromMarketPanel() {
        return buyFromMarketPanel;
    }

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

    /*public void updatePlayerBoard(){
        List<String> nicknameList = new ArrayList<>();
        Player actualPlayer = null;

        gameFrame.remove(mainPanel);

        for(Player player : gameModel.getPlayers()) {
            if(player.getNickName().equals(nickname))
                actualPlayer = player;
            nicknameList.add(player.getNickName());
        }
        mainPanel = new MainPanel(nicknameList, actualPlayer, gameModel);
        gameFrame.add(mainPanel);
        mainPanel.setVisible(true);
    }*/
}
