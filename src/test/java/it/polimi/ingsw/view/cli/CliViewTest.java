package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraProductionLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

class CliViewTest {
    static LeaderCard card1;
    static LeaderCard card2;
    static List<LeaderCard> list;
    static HashMap<ResourceType, Integer> resources;
    static List<DepotShelf> shelves;
    static HashMap<ResourceType, Integer> input;
    static HashMap<ResourceType, Integer> output;
    static DevCard devCard;
    static DevCard devCard2;
    static HashMap<ResourceType, Integer> devCardInput;
    static HashMap<ResourceType, Integer> devCardOutput;
    static HashMap<ResourceType, Integer> cost;
    static Player player;

    @BeforeAll
    static void setup() throws NegativeQuantityException {
        player=new Player();
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2 ));
        card1 = new LeaderCard(4, requirements, new ExtraProductionLeaderEffect(ResourceType.SHIELD, 2));
        requirements = new ArrayList<>();
        requirements.add(new CardRequirementResource(ResourceType.SERVANT, 5));
        card2 = new LeaderCard(3, requirements, new ExtraSlotLeaderEffect(ResourceType.SHIELD, 2));
        list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        player.setUsedLeaders(list);
        player.setUnUsedLeaders(list);
        resources = new HashMap<>();
        resources.put(ResourceType.COIN, 1);
        resources.put(ResourceType.STONE, 2);
        resources.put(ResourceType.SERVANT, 30);
        resources.put(ResourceType.SHIELD, 4);
        resources.put(ResourceType.FAITHPOINT, 50);
        player.setBaseProductionInput(resources);
        player.setBaseProductionOutput(resources);
        player.setLeaderSlots(resources);
        player.setStrongBox(resources);
        shelves = new ArrayList<>();
        shelves.add(new DepotShelf(ResourceType.SERVANT, 1));
        shelves.add(new DepotShelf(ResourceType.COIN, 2));
        shelves.add(new DepotShelf(ResourceType.SHIELD, 3));

        input = new HashMap<>();
        input.put(ResourceType.SHIELD, 2);
        input.put(ResourceType.COIN, 1);
        output = new HashMap<>();
        output.put(ResourceType.SERVANT, 2);
        output.put(ResourceType.FAITHPOINT, 1);
        devCardInput = new HashMap<>();
        devCardInput.put(ResourceType.STONE, 2);
        devCardOutput = new HashMap<>();
        devCardOutput.put(ResourceType.COIN, 1);
        devCardOutput.put(ResourceType.SERVANT, 1);
        devCardOutput.put(ResourceType.STONE, 1);
        cost = new HashMap<>();
        cost.put(ResourceType.STONE, 3);
        devCard = new DevCard(1, DevCardColour.YELLOW, 3,  devCardInput, devCardOutput, cost, "some/random/url");
        devCard2 = new DevCard(2, DevCardColour.PURPLE, 2,  devCardInput, devCardOutput, cost, "some/random/url");
        player.setPlayerState(PlayerState.PLAYING);
        player.setFaithPosition(3);
        player.setNickName("gianjd");
        List<PopeTile> popeTiles =new ArrayList<>();
        PopeTile popeTile = new PopeTile(1, ReportNum.REPORT1);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        player.setPopeTiles(popeTiles);
        for (int i = 0; i<3; i++)
            player.addDepotShelf(new DepotShelf());
    }

    @Test
    void printPlayerState() {
        CliView.printPlayerBoard(player, 6);
    }
    @Test
    void printMarketTest() {
        MarbleType[][] matrix = new MarbleType[3][4];
        Random random = new Random();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                matrix[i][j] = MarbleType.values()[random.nextInt(6)];
        Board board = new Board();
        board.setMarketMatrix(matrix);
        board.setMarbleOnSlide(MarbleType.values()[random.nextInt(6)]);
        DevCard[][] devGrid = new DevCard[3][4];
        devGrid[1][1] = devCard;
        devGrid[1][0] = devCard2;
        board.setDevMatrix(devGrid);
        CliView.printMarket(board);
        CliView.printDevGrid(board);
    }

    @Test
    void printWelcome() {
        CliView.printWelcome();
    }

    @Test
    void printGameState() {
    }

    @Test
    void printFaithTrack() {
        List<PopeTile> popeTiles =new ArrayList<>();
        PopeTile popeTile = new PopeTile(1, ReportNum.REPORT1);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        CliView.printFaithTrack(2, popeTiles, 4);
    }

    @Test
    void printError() {
        CliView.printError("bdsvjhke sbvhj lsdmzb dbjhj jedvk kgvwk kvgqdg jkcqwv kwcv");
        System.out.println("\n");
    }

    @Test
    void printInfo() {
        CliView.printInfo("bdsvjhke sbvhj lsdmzb dbjhj jedvk kgvwk kvgqdg jkcqwv kwcv");
        System.out.println("\n");
    }

    @Test
    void printSetUpView() {
        CliView.printSetUpView(2,0);
        System.out.println("\n");
        CliView.printSetUpView(2,1);
        System.out.println("\n");
        CliView.printSetUpView(2,2);
        System.out.println("\n");
    }

    @Test
    void printResourcesMap() {
        CliView.printResourcesMap(resources, "Master Kenobi, this morrow, local shopkeepers tenders thou:");
    }

    @Test
    void printFinalScores(){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Darth Vader", 67);
        map.put("General Grievous", 33);
        map.put("General Kenobi", 1100);
        map.put("Jar Jar Binks", 1099);
        List<Map.Entry<String, Integer>> results = new LinkedList<>(map.entrySet());
        results.sort((x, y) -> y.getValue().compareTo(x.getValue()));
        CliView.printFinalScores(results);
    }

    @Test
    void printLeaderCard() {
        CliView.printLeaderCard(card1, AnsiCommands.WHITE.getTextColor());
    }

    @Test
    void printUnusedLeaderCards(){
        CliView.printUnusedLeaderCards(list);
    }

    @Test
    void printUsedLeaderCards(){
        CliView.printUsedLeaderCards(list);
    }

    @Test
    void printStrongBox(){
        CliView.printStrongBox(resources);
    }

    @Test
    void printLeaderDepot(){
        CliView.printLeaderDepots(resources);
    }

    @Test
    void printDepotFull(){
        CliView.printDepot(shelves);
    }

    @Test
    void printDepotFirstEmpty(){
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        e.add(new DepotShelf(ResourceType.SHIELD, 3));
        CliView.printDepot(e);
    }
    
    @Test
    void printDepotSecondEmpty(){
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(ResourceType.SERVANT, 1));
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.SHIELD, 3));
        CliView.printDepot(e);
    }

    @Test
    void printDepotThirdEmpty(){
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(ResourceType.SERVANT, 1));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        e.add(new DepotShelf(null, 0));
        CliView.printDepot(e);
    }

    @Test
    void printDepotAllEmpty(){
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(null, 0));
        CliView.printDepot(e);
    }

    @Test
    void printBaseProduction(){
        CliView.printBaseProduction(input, output);
    }

    @Test
    void printResourcesOnALine(){
        CliView.printGeneralResourcesMapOnALine(resources, AnsiCommands.BLACK.getTextColor());
    }

    @Test
    void printDevCard(){
        CliView.printCardInfoIfValid(devCard, AnsiCommands.resetStyle());
    }
}