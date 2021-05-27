package it.polimi.ingsw.view.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.GamesManagerSingleton;
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
import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.view.readOnlyModel.Game;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;

public final class PanelManager {
    private final GuiClient gui;
    private final Gson gson;
    private static PanelManager instance;

    private JFrame gameFrame;

    //TODO: add here label/frame created for each view
    //TODO: add here attibutes used in panels
    private Game gameModel;
    private HashMap<ResourceType, Integer> map;

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

    public void readMessage(ResponseMessage responseMessage) {
    }

    public void writeMessage(Command command) {
        gui.sendMessage(command);
    }

    public void init() {
        /*Initializing frame, panels....*/
        gameFrame = new JFrame();
    }

    public void forceLogout(String logoutMessage) {
    }

    public Game getGameModel() {
        return gameModel;
    }
}
