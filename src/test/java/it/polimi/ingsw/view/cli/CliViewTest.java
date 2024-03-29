package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.devCards.DevSlots;
import it.polimi.ingsw.model.faithTrack.PopeTile;
import it.polimi.ingsw.model.faithTrack.ReportNum;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraProductionLeaderEffect;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.view.lightModel.Board;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;
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
    static DevCard devCard3;
    static HashMap<ResourceType, Integer> devCardInput;
    static HashMap<ResourceType, Integer> devCardOutput;
    static HashMap<ResourceType, Integer> cost;
    static Player player;
    static DevSlots devSlots;

    @BeforeAll
    static void setup() throws NegativeQuantityException {
        player = new Player();
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2));
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
        resources.put(ResourceType.SERVANT, 3);
        resources.put(ResourceType.SHIELD, 4);
        resources.put(ResourceType.FAITHPOINT, 5);
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
        devCard = new DevCard(1, DevCardColour.YELLOW, 3, devCardInput, devCardOutput, cost, "some/random/url");
        devCard2 = new DevCard(1, DevCardColour.PURPLE, 2, devCardInput, devCardOutput, cost, "some/random/url");
        devCard3 = new DevCard(1, DevCardColour.GREEN, 2, devCardInput, devCardOutput, cost, "some/random/url");
        player.setPlayerState(PlayerState.PLAYING);
        player.setFaithPosition(3);
        player.setNickName("gianjd");
        player.setVictoryPoints(56);
        List<PopeTile> popeTiles = new ArrayList<>();
        PopeTile popeTile = new PopeTile(1, ReportNum.REPORT1);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        player.setPopeTiles(popeTiles);
        for (int i = 0; i < 3; i++)
            player.addDepotShelf(new DepotShelf());
        devSlots = new DevSlots();
        devSlots.addDevCard(0, devCard);
        devSlots.addDevCard(2, devCard2);
        player.setDevSlots(devSlots);
    }

    @Test
    void printPlayerState() {
        CliView.printPlayerBoard(player, 6);
    }

    @Test
    void printMarketTest() {
        MarbleType[][] matrix = new MarbleType[3][4];
        Random random = new Random();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
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
        List<PopeTile> popeTiles = new ArrayList<>();
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
        CliView.printSetUpView(2, 0);
        System.out.println("\n");
        CliView.printSetUpView(2, 1);
        System.out.println("\n");
        CliView.printSetUpView(2, 2);
        System.out.println("\n");
    }

    @Test
    void printResourcesMap() {
        CliView.printResourcesMap(resources, "Master Kenobi, this morrow, local shopkeepers tenders thou:");
    }

    @Test
    void printFinalScores() {
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
    void printUnusedLeaderCards() {
        CliView.printUnusedLeaderCards(list);
    }

    @Test
    void printUsedLeaderCards() {
        CliView.printUsedLeaderCards(list);
    }

    @Test
    void printStrongBox() {
        CliView.printStrongBox(resources);
    }

    @Test
    void printLeaderDepot() {
        CliView.printLeaderDepots(resources);
    }

    @Test
    void printDepotFull() {
        CliView.printDepot(shelves);
    }

    @Test
    void printDepotFirstEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        e.add(new DepotShelf(ResourceType.SHIELD, 3));
        CliView.printDepot(e);
    }

    @Test
    void printDepotSecondEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(ResourceType.SERVANT, 1));
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.SHIELD, 3));
        CliView.printDepot(e);
    }

    @Test
    void printDepotThirdEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(ResourceType.SERVANT, 1));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        e.add(new DepotShelf(null, 0));
        CliView.printDepot(e);
    }

    @Test
    void printDepotAllEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(null, 0));
        CliView.printDepot(e);
    }

    @Test
    void printDepotSecondOneEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.STONE, 1));
        e.add(new DepotShelf(null, 0));
        CliView.printDepot(e);
    }

    @Test
    void printDepotThirdOneEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.STONE, 1));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        CliView.printDepot(e);
    }

    @Test
    void printDepotThirdTwoEmpty() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(null, 0));
        e.add(new DepotShelf(ResourceType.STONE, 1));
        e.add(new DepotShelf(ResourceType.COIN, 1));
        CliView.printDepot(e);
    }

    @Test
    void printDepotSomethingInside() {
        List<DepotShelf> e = new ArrayList<>();
        e.add(new DepotShelf(ResourceType.SERVANT, 1));
        e.add(new DepotShelf(ResourceType.STONE, 1));
        e.add(new DepotShelf(ResourceType.COIN, 2));
        CliView.printDepot(e);
    }

    @Test
    void printBaseProduction() {
        CliView.printBaseProduction(input, output);
    }

    @Test
    void printResourcesOnALine() {
        CliView.printGeneralResourcesMapOnALine(resources, AnsiCommands.BLACK.getTextColor());
    }

    @Test
    void printDevCard() {
        CliView.printCardInfoIfValid(devCard, AnsiCommands.resetStyle());
    }

    @Test
    void printDevSlots() {
        CliView.printDevSlots(devSlots);
    }

    @Test
    void printDevSlotsWithOneUnusedCard() throws NegativeQuantityException {
        DevSlots dev = new DevSlots();
        dev.addDevCard(0, devCard);
        dev.addDevCard(0, new DevCard(2, DevCardColour.YELLOW, 2, devCardInput, devCardOutput, cost, "url"));
        dev.addDevCard(1, devCard2);
        CliView.printDevSlots(dev);
    }

    @Test
    void printDevSlotsWithTwoUnusedCards() throws NegativeQuantityException {
        DevSlots dev = new DevSlots();
        dev.addDevCard(0, devCard);
        dev.addDevCard(0, new DevCard(2, DevCardColour.YELLOW, 2, devCardInput, devCardOutput, cost, "url"));
        dev.addDevCard(0, new DevCard(3, DevCardColour.YELLOW, 2, devCardInput, devCardOutput, cost, "url"));
        dev.addDevCard(1, devCard2);
        CliView.printDevSlots(dev);
    }

    @Test
    void printDevSlotsWithThreeFullSlots() throws NegativeQuantityException {
        DevSlots dev = new DevSlots();
        dev.addDevCard(0, devCard);
        dev.addDevCard(0, new DevCard(2, DevCardColour.YELLOW, 2, devCardInput, devCardOutput, cost, "url"));
        dev.addDevCard(0, new DevCard(3, DevCardColour.YELLOW, 2, devCardInput, devCardOutput, cost, "url"));
        dev.addDevCard(1, devCard2);
        dev.addDevCard(1, new DevCard(2, DevCardColour.PURPLE, 2, devCardInput, devCardOutput, cost, "some/random/url"));
        dev.addDevCard(1, new DevCard(3, DevCardColour.PURPLE, 2, devCardInput, devCardOutput, cost, "some/random/url"));
        dev.addDevCard(2, devCard3);
        dev.addDevCard(2, new DevCard(2, DevCardColour.GREEN, 2, devCardInput, devCardOutput, cost, "some/random/url"));
        dev.addDevCard(2, new DevCard(3, DevCardColour.GREEN, 2, devCardInput, devCardOutput, cost, "some/random/url"));
        CliView.printDevSlots(dev);
    }

    @Test
    void printGeneralInfo() {
        CliView.printGeneralInfo("Master Kenobi is thou turn!");
    }

    @Test
    void printCommonParts() {
        MarbleType[][] matrix = new MarbleType[3][4];
        Random random = new Random();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                matrix[i][j] = MarbleType.values()[random.nextInt(6)];
        Board board = new Board();
        board.setMarketMatrix(matrix);
        board.setMarbleOnSlide(MarbleType.values()[random.nextInt(6)]);
        DevCard[][] devGrid = new DevCard[3][4];
        devGrid[1][1] = devCard;
        devGrid[1][0] = devCard2;
        board.setDevMatrix(devGrid);
        Game gameModel = new Game();
        gameModel.setMainBoard(board);
        CliView.printCommonParts(gameModel);
    }

    @Test
    void printWaiting4TurnState() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4TURN);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Jar Jar Binks");
        player.setPlayerState(PlayerState.PLAYING);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printWaiting4LastTurnState() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4LASTTURN);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Jar Jar Binks");
        player.setPlayerState(PlayerState.PLAYINGLASTTURN);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printWaitingForTurn() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4TURN);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Jar Jar Binks");
        player.setPlayerState(PlayerState.PLAYING);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printPlayerBoardNicer() {
        CliView.printPlayerBoardWithFrame(player, 0);
    }

    @Test
    void printPlaying() {
        Game game = new Game();
        Player player1 = new Player();
        player1.setNickName("Darth Vader");
        player1.setPlayerState(PlayerState.WAITING4TURN);
        game.addPlayer(player1);
        player1 = new Player();
        player1.setNickName("Jar Jar Binks");
        player1.setPlayerState(PlayerState.WAITING4TURN);
        game.addPlayer(player1);
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.PLAYING);
        game.addPlayer(player);
        MarbleType[][] matrix = new MarbleType[3][4];
        Random random = new Random();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                matrix[i][j] = MarbleType.values()[random.nextInt(6)];
        Board board = new Board();
        board.setMarketMatrix(matrix);
        board.setMarbleOnSlide(MarbleType.values()[random.nextInt(6)]);
        DevCard[][] devGrid = new DevCard[3][4];
        devGrid[1][1] = devCard;
        devGrid[1][0] = devCard2;
        board.setDevMatrix(devGrid);
        game.setMainBoard(board);
        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printWaiting4GameStart() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4GAMESTART);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Darth Vader");
        player.setPlayerState(PlayerState.WAITING4GAMESTART);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Jar Jar Binks");
        player.setPlayerState(PlayerState.WAITING4GAMESTART);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printPlayingLastTurn() {
        Game game = new Game();
        Player player1 = new Player();
        player1.setNickName("Darth Vader");
        player1.setPlayerState(PlayerState.WAITING4GAMEEND);
        game.addPlayer(player1);
        player1 = new Player();
        player1.setNickName("Jar Jar Binks");
        player1.setPlayerState(PlayerState.WAITING4GAMEEND);
        game.addPlayer(player1);
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.PLAYINGLASTTURN);
        game.addPlayer(player);
        MarbleType[][] matrix = new MarbleType[3][4];
        Random random = new Random();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                matrix[i][j] = MarbleType.values()[random.nextInt(6)];
        Board board = new Board();
        board.setMarketMatrix(matrix);
        board.setMarbleOnSlide(MarbleType.values()[random.nextInt(6)]);
        DevCard[][] devGrid = new DevCard[3][4];
        devGrid[1][1] = devCard;
        devGrid[1][0] = devCard2;
        board.setDevMatrix(devGrid);
        game.setMainBoard(board);
        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printWaiting4GameEnd() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4GAMEEND);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Darth Vader");
        player.setPlayerState(PlayerState.PLAYINGLASTTURN);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printWaiting4BeginningDecisions() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        player.setPlayerState(PlayerState.WAITING4BEGINNINGDECISIONS);
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Darth Vader");
        player.setPlayerState(PlayerState.PLAYINGBEGINNINGDECISIONS);
        game.addPlayer(player);

        CliView.printGameState(game, "Kenobi");
    }

    @Test
    void printOtherNicknames() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Darth Vader");
        game.addPlayer(player);
        player = new Player();
        player.setNickName("Jar Jar Binks");
        game.addPlayer(player);

        CliView.printOthersPlayersName(game, "Kenobi");
    }

    @Test
    void printOtherNicknameWhenPlayingAgainstLorenzo() {
        Game game = new Game();
        Player player = new Player();
        player.setNickName("Kenobi");
        game.addPlayer(player);

        CliView.printOthersPlayersName(game, "Kenobi");
    }

    @Test
    void printDiscardTokenGreen(){
        DiscardToken token = new DiscardToken(DevCardColour.GREEN, 1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenBlue(){
        DiscardToken token = new DiscardToken(DevCardColour.BLUE, 1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenYellow(){
        DiscardToken token = new DiscardToken(DevCardColour.YELLOW, 1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenPurple(){
        DiscardToken token = new DiscardToken(DevCardColour.PURPLE, 1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenGreenCards(){
        DiscardToken token = new DiscardToken(DevCardColour.GREEN, 2, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenBlueCards(){
        DiscardToken token = new DiscardToken(DevCardColour.BLUE, 2, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenYellowCards(){
        DiscardToken token = new DiscardToken(DevCardColour.YELLOW, 2, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printDiscardTokenPurpleCards(){
        DiscardToken token = new DiscardToken(DevCardColour.PURPLE, 2, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printFaithPointTokenOnePoint(){
        FaithPointToken token = new FaithPointToken(1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printFaithPointTokenTwoPoints() {
        FaithPointToken token = new FaithPointToken(2, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printShuffleToken(){
        ShuffleToken token = new ShuffleToken(1, "name");

        CliView.printLorenzosAction(token);
    }

    @Test
    void printOthersPlayerBoard(){
        Game game = new Game();
        game.addPlayer(player);
        CliView.printOtherGameBoard(game, "gianjd");
    }

    @Test
    void printMyPlayerBoard(){
        Game game = new Game();
        game.addPlayer(player);
        CliView.printPlayerBoard(player, -1);
    }

    @Test
    void printHolpMessage(){
        CliView.printHolpMessage();
    }

    @Test
    void printPlayerLeftGame(){
        CliView.printDisconnectionUpdate("Jar-Jar", true);
    }

    @Test
    void printPlayerBackInTheGame(){
        CliView.printDisconnectionUpdate("Jar-Jar", false);
    }

    @Test
    void printSoloScoreLorenzoWins(){
        CliView.printSoloGameScores(false, "You lost! Lorenzo bought an entire column of dev cards!");
        CliView.printSoloGameScores(false, "You lost! Lorenzo made his last vatican report!");
    }

    @Test
    void printSoloScoreLorenzoLoses(){
        CliView.printSoloGameScores(true, "You won against Lorenzo the Magnificent! Your score is: 155  points!");
    }

}