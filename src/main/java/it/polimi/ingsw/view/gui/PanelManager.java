package it.polimi.ingsw.view.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
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
import it.polimi.ingsw.view.gui.View.Welcome;
import it.polimi.ingsw.view.readOnlyModel.Game;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public final class PanelManager {
    private final GuiClient gui;
    private final Gson gson;
    private static PanelManager instance;

    private JFrame gameFrame;

    private Welcome welcome;

    //TODO: add here label/frame created for each view
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

    public void init() {
        /*Initializing frame, panels....*/
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        welcome = new Welcome(gameFrame);
        //gameFrame.add(welcome);
        gameFrame.setVisible(true);
        gameFrame.setSize(600,600);
        gameFrame.setLocation(400,20);
        gameFrame.validate();
        welcome.setVisible(true);

    }


    public void writeMessage(Command command) {
        gui.sendMessage(command);
    }

    public void forceLogout(String logoutMessage) {
        //TODO: print logout view
    }

    public void readMessage(ResponseMessage responseMessage) {
        String responseContent = responseMessage.getResponseContent();
        switch (responseMessage.getResponseType()){
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
                this.manageGameConfiguration(responseContent);
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
                System.out.println("unmanaged respone:\t"+responseMessage);
                System.exit(1);
        }
    }

    private void manageLorenzoAction(String responseContent) {
        synchronized (this){
            LorenzosActionMessage lorenzosActionMessage = gson.fromJson(responseContent, LorenzosActionMessage.class);
            lorenzoToken = lorenzosActionMessage.getSoloActionToken();
        }
        //TODO: do things to print Lorenzo Action

    }


    private void manageleaderboard(String responseContent) {
        synchronized (this){
            FinalScoresMessage message = gson.fromJson(responseContent, FinalScoresMessage.class);
            this.results = message.getResults();
        }
        //TODO: do things to setup view

    }

    private void manageConfigurationDone(String responseContent) {

        //Todo idk if there is something to print
    }

    private void manageNickSetting(String responseContent) {
        synchronized (this) {
            LoginConfirmationMessage setNickMessage = gson.fromJson(responseContent, LoginConfirmationMessage.class);
            nickname = setNickMessage.getConfirmedNickname();
        }
        //Todo idk if there is something to print

    }

    private void manageGameConfiguration(String responseContent) {
        //TODO: do things to setup view
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

    }

    private void managePostStart(String responseContent) {
        synchronized (this) {
            nLeadersToDiscard = 0;
            resourcesToTake = 0;
        }

        //TODO: do things to remove manageStartView and setup new view
    }

    private void manageInfo(String responseContent) {
        //TODO: do things to setup view

    }

    private void manageError(String responseContent) {
        //TODO: do things to setup view
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

}
