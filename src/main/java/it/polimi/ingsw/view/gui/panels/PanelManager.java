package it.polimi.ingsw.view.gui.panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
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
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.cli.CliView;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevCardPanel1;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private SetNumOfPlayersDialog configureGameDialog;

    //TODO: add here panels created for each view
    private EntryViewPanel entryPanel;
    private WaitingRoomPanel waitingRoomPanel;
    private BeginningDecisionsPanel beginningDecisionsPanel;
    private MainPanel mainPanel;
    private DevCardPanel1 devCardCostPanel;

    //TODO: add here attibutes used in panels
    private String nickname;
    private Game gameModel;
    private HashMap<ResourceType, Integer> resourcesMap;
    private String mapDescription;
    private int nLeadersToDiscard;
    private int resourcesToTake;
    private Map<String, Integer> results;
    private SoloActionToken lorenzoToken;
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

        //TODO: add here panels to frame
        entryPanel = new EntryViewPanel();
        gameFrame.add(entryPanel);

        waitingRoomPanel = new WaitingRoomPanel();
        gameFrame.add(waitingRoomPanel);
        devCardCostPanel = new DevCardPanel1();
        gameFrame.add(devCardCostPanel);

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
            default:
                System.out.println("unmanaged respone:\t" + responseMessage);
                System.exit(1);
        }
    }

    private void manageLorenzoAction(String responseContent) {
        synchronized (this) {
            LorenzosActionMessage lorenzosActionMessage = gson.fromJson(responseContent, LorenzosActionMessage.class);
            lorenzoToken = lorenzosActionMessage.getSoloActionToken();
        }
        //TODO: do things to print Lorenzo Action

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

    private void manageProductionResources(String responseContent) {
        synchronized (this) {
            HashMapResFromProdCostMessage message = gson.fromJson(responseContent, HashMapResFromProdCostMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "resourcesProduced";
        }
        //TODO: do things to setup view

    }

    private void manageMarketResources(String responseContent) {
        synchronized (this) {
            HashMapResFromMarketMessage message = gson.fromJson(responseContent, HashMapResFromMarketMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "MarketResource";
        }

        //TODO: do things to setup view

    }

    private void manageCardCost(String responseContent) {
        synchronized (this) {
            HashMapResFromDevGridMessage message = gson.fromJson(responseContent, HashMapResFromDevGridMessage.class);
            this.resourcesMap = message.getResources();
            this.mapDescription = "DevCardCost";
        }

        //TODO: do things to setup view

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
            //gameFrame.validate();
            waitingRoomPanel.setVisible(false);
            beginningDecisionsPanel.setVisible(true);
        });
    }

    private void managePostStart(String responseContent) {
        synchronized (this) {
            nLeadersToDiscard = 0;
            resourcesToTake = 0;
        }

        //TODO: do things to remove manageStartView and setup new view
        List<String> nicknameList = new ArrayList<>();
        Player actualPlayer = null;
        for(Player player : gameModel.getPlayers()) {
            if(player.getNickName().equals(nickname))
                actualPlayer = player;
            nicknameList.add(player.getNickName());
        }
        mainPanel = new MainPanel(nicknameList, actualPlayer, gameModel);
        gameFrame.add(mainPanel);

        visualizer.submit(() -> {
            beginningDecisionsPanel.setVisible(false);
            mainPanel.setVisible(true);
            mainPanel.updateGridView(50,100);
        });

    }

    public void printBuyCardDialog(int row, int col) throws IllegalArgumentException, IllegalStateException {
        visualizer.submit(() -> {
            //TODO
        });
    }


    public void printInfo(String info) {
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

    private void manageUpdate(String responseContent) {
        synchronized (this) {
            ModelUpdate modelUpdate = gson.fromJson(responseContent, ModelUpdate.class);
            Game update = modelUpdate.getGame();
            if (this.gameModel == null)
                gameModel = update;
            else
                gameModel.merge(update);
        }

        //TODO: do things to setup view
        visualizer.submit(() -> {
            if(mainPanel!=null) {
                mainPanel.updateMainPanel(gameModel);
            }
        });
    }

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

    public DevCardPanel1 getDevCardCostPanel() { return devCardCostPanel; }



    public int getResourcesToTake() {
        return resourcesToTake;
    }

    /**
     * Returns the player's depot shelves
     *
     * @return the player's depot shelves
     */
    public List<DepotShelf> getDepotShelves() {
        return gameModel.getPlayers().stream().filter(x -> x.getNickName().equals(this.nickname)).findAny().get().getDepotShelves();
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
