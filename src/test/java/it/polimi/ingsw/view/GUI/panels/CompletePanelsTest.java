package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.panels.MarketPlacingResources;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompletePanelsTest {
    PanelManager panelManager;
    Player player;
    Game game;
    HashMap<ResourceType, Integer> resToBePlaced;
    List<Integer> aLeaderList;

    private void init(){
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
        HashMap<ResourceType, Integer> lSlot = new HashMap<>();
        lSlot.put(ResourceType.STONE, 2);
        player.setLeaderSlots(lSlot);
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


    }

    public static void main(String[] args){
        CompletePanelsTest test = new CompletePanelsTest();
        test.init();

        //These following methods create the panels
        test.checkMarketPlacingResources();
    }

    private void checkMarketPlacingResources() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        MarketPlacingResources market = new MarketPlacingResources(resToBePlaced, 0, 1, aLeaderList);

        frame.add(market);
        frame.setVisible(true);
    }
}
