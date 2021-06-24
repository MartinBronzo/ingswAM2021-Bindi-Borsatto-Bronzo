package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.devCards.DevSlot;
import it.polimi.ingsw.model.devCards.DevSlots;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BaseProductionParams;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.mainViews.panels.DevGridPayingCost;
import it.polimi.ingsw.view.gui.mainViews.panels.MarketPlacingResources;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.gui.mainViews.panels.ProductionGetResources;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompletePanelsTest {
    PanelManager panelManager;
    Player player;
    Game game;
    HashMap<ResourceType, Integer> resToBePlaced;
    HashMap<ResourceType, Integer> resToBeTaken;
    List<Integer> aLeaderList;

    private void init() throws NegativeQuantityException {
        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the PanelManager
        panelManager = PanelManager.createInstance(new GuiClient());
        player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));

        player.setUsedLeaders(active);
        //player.setUsedLeaders(new ArrayList<>());

        HashMap<ResourceType, Integer> lSlot = new HashMap<>();
        lSlot.put(ResourceType.STONE, 2);
        player.setLeaderSlots(lSlot);

        HashMap<ResourceType, Integer> resToStrongBox = new HashMap<>();
        resToStrongBox.put(ResourceType.COIN, 2);
        resToStrongBox.put(ResourceType.STONE, 3);
        resToStrongBox.put(ResourceType.SERVANT, 1);
        player.setStrongBox(resToStrongBox);

        panelManager.setPlayer(player);
        game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        resToBePlaced = new HashMap<>();
        resToBePlaced.put(ResourceType.COIN, 2);
        resToBePlaced.put(ResourceType.STONE, 3);
        resToBePlaced.put(ResourceType.SERVANT, 1);
        resToBePlaced.put(ResourceType.SHIELD, 1);

        aLeaderList = new ArrayList<>();
        aLeaderList.add(1);

        resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.SERVANT, 1);
        resToBeTaken.put(ResourceType.STONE, 2);
        resToBeTaken.put(ResourceType.COIN, 2);
        DevSlot[] slots = new DevSlot[3];

        DevSlot devSlot = new DevSlot();
        HashMap<ResourceType, Integer> prodInput = new HashMap<>();
        prodInput.put(ResourceType.COIN, 1);
        HashMap<ResourceType, Integer> prodOut = new HashMap<>();
        prodOut.put(ResourceType.FAITHPOINT, 1);
        HashMap<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.SHIELD, 2);
        devSlot.addDevCard(new DevCard(1, DevCardColour.GREEN, 1, prodInput, prodOut, cost, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png"));
        slots[0] = devSlot;

        devSlot = new DevSlot();
        slots[1] = devSlot;

        devSlot = new DevSlot();
        devSlot.addDevCard(new DevCard(1, DevCardColour.GREEN, 1, prodInput, prodOut, cost, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-10-1.png"));
        slots[2] = devSlot;

        DevSlots fullSlot = new DevSlots(slots);

        player.setDevSlots(fullSlot);
    }

    public static void main(String[] args) throws NegativeQuantityException {
        CompletePanelsTest test = new CompletePanelsTest();
        test.init();

        //These following methods create the panels
        //test.checkMarketPlacingResources();
        //test.checkDevGridPayingCost();
        test.checkGetProductionCost();
    }

    private void checkMarketPlacingResources() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        MarketPlacingResources market = new MarketPlacingResources(resToBePlaced, 0, 1, aLeaderList);

        frame.add(market);
        frame.setVisible(true);
    }

    private void checkDevGridPayingCost() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Integer> leaders = new ArrayList<>();
        leaders.add(1);
        DevGridPayingCost market = new DevGridPayingCost(resToBeTaken, 1, 1, leaders);

        frame.add(market);
        frame.setVisible(true);
    }

    private void checkGetProductionCost(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Integer> leaders = new ArrayList<>();
        leaders.add(1);
        leaders.add(2);
        List<Integer> devCards = new ArrayList<>();
        devCards.add(2);
        List<ResourceType> input = new ArrayList<>();
        input.add(ResourceType.COIN);
        input.add(ResourceType.SERVANT);
        List<ResourceType> output = new ArrayList<>();
        output.add(ResourceType.STONE);
        BaseProductionParams productionParams = new BaseProductionParams(true, input, output);

        ProductionGetResources prod = new ProductionGetResources(resToBeTaken, new ArrayList<>(), devCards, productionParams);

        frame.add(prod);
        frame.setVisible(true);

    }
}
