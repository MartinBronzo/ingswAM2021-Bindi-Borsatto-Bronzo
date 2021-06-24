package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.devCards.DevSlot;
import it.polimi.ingsw.model.devCards.DevSlots;
import it.polimi.ingsw.model.faithTrack.PopeTile;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.view.lightModel.Board;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import java.util.*;
import java.util.stream.Collectors;

public class CliView {

     /*
    #############################################################################################
    CORE METHODS
    #############################################################################################
     */

    public static void printGameState(Game gameModel, String nickname) throws NoSuchElementException {
        if (gameModel == null || nickname == null || nickname.equals("")) return;


        Player player = gameModel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().orElseThrow(NoSuchElementException::new);
        switch (player.getPlayerState()) {
            case WAITING4TURN:
                try {
                    printGeneralInfo("My master, thou mustn't worry. Your time will soon come! It is " + findPlayerByState(new LinkedList<>(gameModel.getPlayers()), PlayerState.PLAYING) +
                            "'s turn!\n");
                }catch (NoSuchElementException e){
                    printGeneralInfo("My master, thou mustn't worry. Your time will soon come! It is " + findPlayerByState(new LinkedList<>(gameModel.getPlayers()), PlayerState.PLAYINGBEGINNINGDECISIONS) +
                            "'s turn!\n");
                }
                break;
            case WAITING4LASTTURN:
                printGeneralInfo("My master, thou mustn't worry. Thee will soon play your LAST turn! It is " + findPlayerByState(new LinkedList<>(gameModel.getPlayers()), PlayerState.PLAYINGLASTTURN) +
                                         "'s turn!\n");
                break;
            case PLAYINGBEGINNINGDECISIONS:
                printPlayerBoardWithFrame(player, gameModel.getLorenzosPosition());
                break;
            case PLAYING:
                printGeneralInfo("Master " + player.getNickName() + " is thy turn!");
                printCommonParts(gameModel);
                printPlayerBoardWithFrame(player, gameModel.getLorenzosPosition());
                break;
            case WAITING4GAMESTART:
                printGeneralInfo("The game is about to starteth, my Master! These are thou competitors: " + getOtherPlayersNicknames(gameModel, player.getNickName()));
                break;
            case PLAYINGLASTTURN:
                printGeneralInfo("Master " + player.getNickName() + " is thy LAST turn!");
                printCommonParts(gameModel);
                printPlayerBoardWithFrame(player, gameModel.getLorenzosPosition());
                break;
            case WAITING4GAMEEND:
                printGeneralInfo("Our fun game is coming to an endeth, thou competitors will soon play their last turn and we shall see how good you played, my master!");
                break;
            case WAITING4BEGINNINGDECISIONS:
                printGeneralInfo("We are about to start our marvellous journey togeth'r: hold tight a little longeth'r while thy dreadful competit'rs take their first choices, thee'll lief do the same ");
                printOthersPlayersName(gameModel, player.getNickName());
                break;
        }
        //printOthersPlayersName(gameModel, player.getNickName());
    }



    /*
    #############################################################################################
    WELCOME MESSAGES
    #############################################################################################
     */

    public static void printWelcome() {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        System.out.print("                                                                                                                                      \n" +
                                 "  /\\\\,/\\\\,               ,                                  /\\       -__ /\\                                                           \n" +
                                 " /| || ||    _          ||                                 ||          || \\,                _    '               _                    \n" +
                                 " || || ||   < \\,  _-_, =||=  _-_  ,._-_  _-_,        /'\\\\ =||=        /|| /    _-_  \\\\/\\\\  < \\, \\\\  _-_,  _-_,  < \\, \\\\/\\\\  _-_  _-_  \n" +
                                 " ||=|= ||   /-|| ||_.   ||  || \\\\  ||   ||_.        || ||  ||         \\||/-   || \\\\ || ||  /-|| || ||_.  ||_.   /-|| || || ||   || \\\\ \n" +
                                 "~|| || ||  (( ||  ~ ||  ||  ||/    ||    ~ ||       || ||  ||          ||  \\  ||/   || || (( || ||  ~ ||  ~ || (( || || || ||   ||/   \n" +
                                 " |, \\\\,\\\\,  \\/\\\\ ,-_-   \\\\, \\\\,/   \\\\,  ,-_-        \\\\,/   \\\\,       _---_-|, \\\\,/  \\\\ \\\\  \\/\\\\ \\\\ ,-_-  ,-_-   \\/\\\\ \\\\ \\\\ \\\\,/ \\\\,/  \n" +
                                 "_-                                                                                                                                    \n");
        System.out.print(AnsiCommands.YELLOW.getBackgroundColor());
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("Digital Edition by Ludovica Bindi, Martin Bronzo and Andrea Borsatto\n");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printSetUpView(int nLeadersToDiscard, int resourcesToTake) {
        System.out.print(AnsiCommands.clear() + AnsiCommands.GREEN.getTextColor());
        System.out.print("     _______________________________________________________\n" +
                                 "    /\\                                                      \\\n" +
                                 "(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)\n" +
                                 "    \\/''''''''''''''''''''''''''''''''''''''''''''''''''''''/\n" +
                                 "    (                                                      (\n" +
                                 "     )                  YOUR LIEGE DEMANDS:                 )\n" +
                                 "    (                                                      (\n" +
                                 "     )            " + nLeadersToDiscard + " LEADER CARDS OF YOUR CHOICE             )\n" +
                                 "    (                                                      (\n");
        if (resourcesToTake == 0)
            System.out.print("     )                                                      )\n");
        else if (resourcesToTake == 1)
            System.out.print("     )        IN EXCHANGE OF 1 RESOURCE OF YOUR CHOICE      )\n");
        else
            System.out.print("     )       IN EXCHANGE OF 2 RESOURCES OF YOUR CHOICE      )\n");
        System.out.print(
                "    (                                                      (\n" +
                        "    /\\''''''''''''''''''''''''''''''''''''''''''''''''''''''\\    \n" +
                        "(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)\n" +
                        "    \\/______________________________________________________/");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

     /*
    #############################################################################################
    END MESSAGES
    #############################################################################################
     */

    public static void printPlayerBoard(Player player, Integer lorenzoPosition) {
        if (player == null) return;
        if (lorenzoPosition == null) lorenzoPosition = -1;
        printVictoryPoints(player.getNickName(), player.getPlayerState(), player.getVictoryPoints());
        printDividerSmall(AnsiCommands.GREEN);
        printFaithTrack(player.getFaithPosition(), player.getPopeTiles(), lorenzoPosition);
        printDividerSmall(AnsiCommands.PURPLE);
        printDepot(player.getDepotShelves());
        printStrongBox(player.getStrongBox());
        printLeaderDepots(player.getLeaderSlots());
        printBaseProduction(player.getBaseProductionInput(), player.getBaseProductionOutput());
        printDevSlots(player.getDevSlots());
        printUsedLeaderCards(player.getUsedLeaders());
        printUnusedLeaderCards(player.getUnUsedLeaders());
    }

    public static void printOtherBoard(Player player, Integer lorenzoPosition) {
        if (player == null) return;
        if (lorenzoPosition == null) lorenzoPosition = -1;
        printVictoryPoints(player.getNickName(), player.getPlayerState(), player.getVictoryPoints());
        printDividerSmall(AnsiCommands.GREEN);
        printFaithTrack(player.getFaithPosition(), player.getPopeTiles(), lorenzoPosition);
        printDividerSmall(AnsiCommands.PURPLE);
        printDepot(player.getDepotShelves());
        printStrongBox(player.getStrongBox());
        printLeaderDepots(player.getLeaderSlots());
        printBaseProduction(player.getBaseProductionInput(), player.getBaseProductionOutput());
        printDevSlots(player.getDevSlots());
        printUsedLeaderCards(player.getUsedLeaders());
        if(player.getUnUsedLeaders().size() != 0){
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print(".... and ");
            if(player.getUnUsedLeaders().size() == 1)
                System.out.print("one other not active Leader Card!\n");
            else
                System.out.print(player.getUsedLeaders().size() + " other not active Leader Cards!\n");
        }
        System.out.print(AnsiCommands.resetStyle());
    }

    private static void printVictoryPoints(String nickName, PlayerState playerState, Integer victoryPoints) {
        if (nickName == null || playerState == null || victoryPoints == null) return;
        if (playerState.equals(PlayerState.PLAYING)) System.out.print(AnsiCommands.RED.getTextColor());
        else System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.println(nickName + ":\t" + victoryPoints + " VP");
        System.out.print(AnsiCommands.resetStyle());
    }

    public static void printDevSlots(DevSlots devSlots) {
        if (devSlots == null) return;
        System.out.print(AnsiCommands.PURPLE.getTextColor());
        System.out.print("Thou great Slots of DevCards are hither presented:\n");
        for (int i = 0; i < 3; i++)
            printADevSlot(i + 1, devSlots.getDevSlot(i), AnsiCommands.PURPLE.getTextColor());

    }

    private static void printADevSlot(int devSlotNumber, DevSlot devSlot, String backgroundColor) {
        List<DevCard> list = new LinkedList<>(devSlot.getDevCards());
        System.out.print(devSlotNumber + " : ");
        if (devSlot.size() != 0) {
            printCardInfo(list.get(devSlot.size() - 1), backgroundColor);
            System.out.print("\n");
            if (devSlot.size() > 1) {
                System.out.print(" ".repeat(8));
                String line = "Not usable: ";
                System.out.print(line);
                printCardInfo(list.get(devSlot.size() - 2), backgroundColor);
                System.out.print("\n");
                for (int i = devSlot.size() - 3; i >= 0; i--) {
                    System.out.print(" ".repeat(8 + line.length()));
                    printCardInfo(list.get(i), backgroundColor);
                    System.out.print("\n");
                }
            }
        } else
            System.out.print("\n");
    }

    public static void printFinalScores(List<Map.Entry<String, Integer>> results) {
        if (results == null) return;
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("\n   ______________________________\n" +
                                 " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                                 "    |                            |.\n");

        String[] lines = CliView.splitInLinesBySize("The game hath sadly come to an end, we shall see the final scores:", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        System.out.print("    |                            |.\n");

        for (Map.Entry<String, Integer> e : results)
            printPlayersAndScore(e.getKey(), e.getValue());


        lines = CliView.splitInLinesBySize("We shall crown our Lord,", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        lines = CliView.splitInLinesBySize(results.get(0).getKey(), 27);
        for (String line : lines) {
            System.out.print("    | ");
            System.out.print(AnsiCommands.YELLOW.getTextColor());
            System.out.print(line + " ".repeat(27 - line.length()));
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("|.\n");
        }
        System.out.print("    |                            |.\n");

        System.out.print(AnsiCommands.RED.getTextColor());
        lines = CliView.splitInLinesBySize("May long live the Lord!", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printSoloGameScores(boolean victory, String message) {
        System.out.print(AnsiCommands.clear());
        if(victory)
            System.out.print(AnsiCommands.GREEN.getTextColor());
        else
            System.out.print(AnsiCommands.RED.getTextColor());

        System.out.print("\n   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");

        String[] lines = CliView.splitInLinesBySize(message, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        System.out.print("    |                            |.\n");

        String end;
        if(victory)
            end = "My master, thou art our Almighty New Lord!";
        else
            end = "My master, Lorenzo the Magnificent is our Eternal Almighty Lord!";
        lines = CliView.splitInLinesBySize(end, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }

        System.out.print("    |                            |.\n");

        lines = CliView.splitInLinesBySize("May long live the Lord!", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());

    }

    /*
    #############################################################################################
    GENERAL PURPOSES METHODS
    #############################################################################################
     */

    //SPAZI TRA I BORDI DELLA PERGAMENA PICCOLA: 28

    public static void printOtherGameBoard(Game gameModel, String nickname) {
        if (gameModel == null || nickname == null || nickname.equals("")) return;
        try {
            Player player = gameModel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().orElseThrow(NoSuchElementException::new);
            printOthersPlayerBoardWithFrame(player, gameModel.getLorenzosPosition());
        } catch (NoSuchElementException e) {
            printError("Is not a Player in the Game");
        }

    }

    public static void printOthersPlayersName(Game gameModel, String currentPlayer) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("\n   ______________________________\n" +
                                 " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                                 "    |                            |.\n");
        String fullLine;
        if (gameModel.getPlayers().size() > 1) {
            fullLine = "Master " + currentPlayer + ", thy dreadful competitors are hither shown: ";
            fullLine = fullLine + getOtherPlayersNicknames(gameModel, currentPlayer);
        } else {
            fullLine = "Master " + currentPlayer + ", thy dreadful competitor is the Almighty Lorenzo the Magnificent!";
        }
        String[] lines = CliView.splitInLinesBySize(fullLine, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }

        System.out.print("    |                            |.\n");

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printPlayerBoardWithFrame(Player player, int lorenzoPosition) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");

        System.out.print(AnsiCommands.WHITE.getTextColor());
        System.out.print("\nThou Board, my Master!\n");
        System.out.print(AnsiCommands.BLUE.getTextColor());

        printPlayerBoard(player, lorenzoPosition);

        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printOthersPlayerBoardWithFrame(Player player, int lorenzoPosition) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");

        System.out.print(AnsiCommands.WHITE.getTextColor());
        System.out.print("\n" + player.getNickName() + "'s Board!\n\n");
        System.out.print(AnsiCommands.BLUE.getTextColor());

        printOtherBoard(player, lorenzoPosition);

        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printCommonParts(Game gameModel) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");

        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("\nMarket\n");
        System.out.print(AnsiCommands.BLACK.getTextColor());
        printMarket(gameModel.getMainBoard());
        System.out.print(AnsiCommands.GREEN.getTextColor());
        System.out.print("\n\nGrid of Dev Cards\n");
        printDevGrid(gameModel.getMainBoard());
        System.out.print(AnsiCommands.BLACK.getTextColor());

        System.out.print("_________________________________________________________________________________\n");
        System.out.print("_________________________________________________________________________________\n");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }


    public static void printGeneralInfo(String info) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        System.out.print("\n   ______________________________\n" +
                                 " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                                 "    |                            |.\n");

        String[] lines = CliView.splitInLinesBySize(info, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        System.out.print("    |                            |.\n");

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());

    }

    public static void printError(String error) {
        System.out.print(AnsiCommands.clear());
        try {
            String[] lines = CliView.splitInLinesBySize(error, 27);
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("   ______________________________\n" +
                                     " / \\                             \\.\n" +
                                     "|   |            ERROR           |.\n" +
                                     " \\_ |                            |.\n" +
                                     "    |                            |.\n");
            for (String line : lines) {
                System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
            }
            System.out.print(
                    "    |   _________________________|___\n" +
                            "    |  /                            /.\n" +
                            "    \\_/____________________________/.\n");
        } catch (IllegalArgumentException e) {
            System.out.print(AnsiCommands.RED.getBackgroundColor());
            System.out.println(error);
        }
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printInfo(String info) {
        System.out.print(AnsiCommands.clear());
        try {
            String[] lines = CliView.splitInLinesBySize(info, 27);
            System.out.print(AnsiCommands.YELLOW.getTextColor());
            System.out.print("   ______________________________\n" +
                                     " / \\                             \\.\n" +
                                     "|   |            INFO            |.\n" +
                                     " \\_ |                            |.\n" +
                                     "    |                            |.\n");
            for (String line : lines) {
                System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
            }
            System.out.print(
                    "    |   _________________________|___\n" +
                            "    |  /                            /.\n" +
                            "    \\_/____________________________/.\n");
        } catch (IllegalArgumentException e) {
            System.out.print(AnsiCommands.YELLOW.getBackgroundColor());
            System.out.println(info);
        }
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    /*
    #############################################################################################
    METHODS WHICH HELP TO PRINT RECURRING STUFF
    #############################################################################################
     */


    public static void printResourcesMap(Map<ResourceType, Integer> resourcesMap, String heading) {
        if (resourcesMap == null) return;
        String[] lines = CliView.splitInLinesBySize(heading, 27);

        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.GREEN.getTextColor());
        System.out.print("   ______________________________\n" +
                                 " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                                 "    |                            |.\n");
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }

      /*for(Map.Entry<ResourceType, Integer> e: resourcesMap.entrySet())
          printResources(e.getKey(), e.getValue(), AnsiCommands.GREEN.getTextColor());*/
        printGeneralResourcesMapsOnSmallScroll(resourcesMap, AnsiCommands.GREEN.getTextColor());

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    private static void printPlayersAndScore(String name, Integer score) {
        if (score == null) return;
        String fullLine = name + ": " + score;
        String[] lines = CliView.splitInLinesBySize(fullLine, 27);
        for (String line : lines) {
            System.out.print("    | ");
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print(line + " ".repeat(27 - line.length()));
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("|.\n");
        }
    }

    private static void printGeneralResourcesMapsOnSmallScroll(Map<ResourceType, Integer> map, String boundaryColor) {
        if (map == null) return;
        for (Map.Entry<ResourceType, Integer> e : map.entrySet())
            printResourcesOnScroll(e.getKey(), e.getValue(), boundaryColor);
    }

    public static void printGeneralResourcesMapOnALine(Map<ResourceType, Integer> map, String boundaryColor) {
        if (map == null) return;
        int j = 0;
        for (Map.Entry<ResourceType, Integer> e : map.entrySet()) {
            if (j != 0)
                System.out.print(" + ");
            else
                j++;
            printResAndNumberWithSameColor(e.getKey(), e.getValue(), boundaryColor);
        }
    }

    public static void printFaithTrack(Integer position, List<PopeTile> popeTiles, int lorenzoPosition) {
        if (popeTiles == null || popeTiles.size() != 3 || position == null) return;
        System.out.print(AnsiCommands.resetStyle());
        if (lorenzoPosition != -1) {
            System.out.print(AnsiCommands.PURPLE.getBackgroundColor() + AnsiCommands.BLACK.getTextColor());
            System.out.println(" ".repeat(lorenzoPosition * 2) + "ℒ" + " ".repeat((24 - lorenzoPosition) * 2) + AnsiCommands.resetStyle());
        }
        System.out.println("⚀⚀⚀⚀⚀⚀♕♕⚀⚀" + AnsiCommands.YELLOW.getTextColor() + "⚀⚀♕♕⚀⚀☩☩" + AnsiCommands.resetStyle() + "♕♕⚀⚀⚀⚀" + AnsiCommands.YELLOW.getTextColor() + "♕♕⚀⚀⚀⚀♕♕☩☩" + AnsiCommands.resetStyle() + "⚀⚀♕♕" + AnsiCommands.YELLOW.getTextColor() + "⚀⚀⚀⚀♕♕⚀⚀⚀⚀☩☩" + AnsiCommands.resetStyle());
        System.out.print(AnsiCommands.YELLOW.getBackgroundColor() + AnsiCommands.BLACK.getTextColor());
        System.out.println(" ".repeat(position * 2) + "⸡⸠" + " ".repeat((24 - position) * 2) + AnsiCommands.resetStyle());
        System.out.println("⚀⚀⚀⚀⚀⚀♕♕⚀⚀" + AnsiCommands.YELLOW.getTextColor() + "⚀⚀♕♕⚀⚀☩☩" + AnsiCommands.resetStyle() + "♕♕⚀⚀⚀⚀" + AnsiCommands.YELLOW.getTextColor() + "♕♕⚀⚀⚀⚀♕♕☩☩" + AnsiCommands.resetStyle() + "⚀⚀♕♕" + AnsiCommands.YELLOW.getTextColor() + "⚀⚀⚀⚀♕♕⚀⚀⚀⚀☩☩" + AnsiCommands.resetStyle());
        System.out.print(AnsiCommands.RED.getTextColor());
        if (popeTiles.get(0).isDiscarded()) {
            System.out.print(" ".repeat(18));
        } else if (popeTiles.get(0).isChanged()) {
            System.out.print(" ".repeat(12) + "\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B" + " ".repeat(2));
        } else {
            System.out.print(" ".repeat(12) + "\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06" + " ".repeat(2));
        }
        if (popeTiles.get(1).isDiscarded()) {
            System.out.print(" ".repeat(16));
        } else if (popeTiles.get(1).isChanged()) {
            System.out.print(" ".repeat(9) + "\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B" + " ".repeat(3));
        } else {
            System.out.print(" ".repeat(9) + "\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06" + " ".repeat(3));
        }
        if (popeTiles.get(2).isDiscarded()) {
            System.out.print("\n");
        } else if (popeTiles.get(2).isChanged()) {
            System.out.print(" ".repeat(8) + "\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B" + "\n");
        } else {
            System.out.print(" ".repeat(8) + "\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06" + "\n");
        }
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printStrongBox(Map<ResourceType, Integer> map) {
        if (map == null) return;
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("     ____________________________.\n");
        String[] lines = CliView.splitInLinesBySize("Thy precious Strongbox is hither presented: ", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        printGeneralResourcesMapsOnSmallScroll(map, AnsiCommands.BLACK.getTextColor());
        System.out.print("     ____________________________.\n");
    }

    public static void printLeaderDepots(Map<ResourceType, Integer> map) {
        if (map == null) return;
        System.out.print(AnsiCommands.WHITE.getTextColor());
        System.out.print("     ____________________________.\n");
        String[] lines = CliView.splitInLinesBySize("Thy Extra Slots are hither presented: ", 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        printGeneralResourcesMapsOnSmallScroll(map, AnsiCommands.WHITE.getTextColor());
        System.out.print("     ____________________________.\n");
    }

    public static void printDepot(List<DepotShelf> shelves) {
        if (shelves == null) return;
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("Thy Depot is hither presented:\n");
        DepotShelf d;
        for (int j = 0; j < shelves.size(); j++) {
            d = shelves.get(j);
            if (j == 0)
                System.out.print("         ");
            else if (j == 1)
                System.out.print("    ");
            if (d.getQuantity() == 0 || d.getResourceType() == null)
                if (j == 0)
                    System.out.print("|        |");
                else if (j == 1)
                    System.out.print("|        |        |");
                else
                    System.out.print("|        |        |        |");
            else {
                System.out.print("|   ");
                int i;
                for (i = 0; i < d.getQuantity(); i++) {
                    if (i != 0)
                        System.out.print("   ");
                    printResName(d.getResourceType(), AnsiCommands.BLACK.getTextColor());
                    System.out.print("   |");
                }
                int tmp = d.getQuantity();
                while (tmp != j + 1) {
                    System.out.print("        |");
                    tmp++;
                }

            }
            System.out.print("\n");
        }
    }

    private static void printDivider(AnsiCommands color) {
        if (color != null)
            System.out.print(color.getTextColor());
        System.out.println("  .-----------------------------------------------------------------.\n" +
                                   "|  /   \\                                                       /   \\  |\n" +
                                   "| |\\_.  |                                                     |    /| |\n" +
                                   "|\\|  | /|                                                     |\\  | |/|\n" +
                                   "| `---' |                                                     | `---' |\n" +
                                   "|       |-----------------------------------------------------|       |\n" +
                                   "\\       |                                                     |       /\n" +
                                   " `-----'                                                       `-----'");
        System.out.print(AnsiCommands.resetStyle());
    }

    private static void printDividerSmall(AnsiCommands color) {
        if (color != null)
            System.out.print(color.getTextColor());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.print(AnsiCommands.resetStyle());
    }

    public static void printBaseProduction(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output) {
        if (input == null || output == null)
            return;
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        System.out.print("Thy Base Production is hither presented:\n");

        System.out.print("\nFrom: ");
        int j = 0;
        for (Map.Entry<ResourceType, Integer> e : input.entrySet()) {
            if (j != 0)
                System.out.print(" + ");
            else
                j++;
            for (int i = 0; i < e.getValue() - 1; i++) {
                printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
                System.out.print(" + ");
            }
            printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
        }
        System.out.print("\n");

        System.out.print("      | |\n");
        System.out.print("     _| |_\n");
        System.out.print("     \\   /\n");
        System.out.print("      \\ /\n");
        System.out.print("       '");


        System.out.print("\nThou get: ");
        j = 0;
        for (Map.Entry<ResourceType, Integer> e : output.entrySet()) {
            if (j != 0)
                System.out.print(" + ");
            else
                j++;
            for (int i = 0; i < e.getValue() - 1; i++) {
                printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
                System.out.print(" + ");
            }
            printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
        }
        System.out.print("\n");
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

    }

    public static void printMarket(Board board) {
        System.out.print(AnsiCommands.resetStyle());
        if (board == null || board.getMarketMatrix() == null || board.getMarbleOnSlide() == null) {
            System.out.println("missing board or getMatrix or marble on slide in print market");
            return;
        }
        MarbleType[][] matrix = board.getMarketMatrix();
        System.out.println("___________ " + board.getMarbleOnSlide().toString());
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j].toString() + "|");
            }
            System.out.println("\n___________");
        }
    }

    public static void printDevGrid(Board board) {
        if (board == null || board.getDevMatrix() == null) {
            System.out.println("missing board or devgrid in print devgrid");
            return;
        }
        DevCard[][] devGrid = board.getDevMatrix();
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("_____________");
        System.out.println(AnsiCommands.resetStyle());
        int c;
        for (int i = 0; i < devGrid.length; i++) {
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("|");
            System.out.print(AnsiCommands.resetStyle());
            for (int j = 0; j < devGrid[i].length; j++) {
                c = (1 + j + 4 * i);
                System.out.print(c);
                if (c < 10) {
                    System.out.print(" " + AnsiCommands.RED.getTextColor() + "|" + AnsiCommands.resetStyle());
                } else {
                    System.out.print(AnsiCommands.RED.getTextColor() + "|" + AnsiCommands.resetStyle());
                }
            }
            System.out.print("\n" + AnsiCommands.RED.getTextColor());
            System.out.print("_____________");
            System.out.println(AnsiCommands.resetStyle());
        }

        for (int i = 0; i < devGrid.length; i++) {
            for (int j = 0; j < devGrid[i].length; j++) {
                c = (1 + j + 4 * i);
                //System.out.println(c+":\t");
                System.out.print(c + " : ");
                printCardInfo(devGrid[i][j], AnsiCommands.resetStyle());
                System.out.print("\n");
            }
        }

    }

    /*
    #############################################################################################
    LEADERCARDS-RELATED METHODS
    #############################################################################################
     */

    public static void printUnusedLeaderCards(List<LeaderCard> list) {
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.print("Thy Not Active Card of the Leader are hither presented:\n\n");
        printLeaderCards(list, AnsiCommands.RED.getTextColor());
    }

    public static void printUsedLeaderCards(List<LeaderCard> list) {
        System.out.print(AnsiCommands.GREEN.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.print("Thy Active Card of the Leader are hither presented:\n\n");
        printLeaderCards(list, AnsiCommands.GREEN.getTextColor());
    }

    public static void printLeaderCards(List<LeaderCard> list, String backgroundColor) {
        if (list == null)
            return;
        for (LeaderCard lD : list) {
            printLeaderCard(lD, backgroundColor);
            System.out.print("\n");
        }

        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

    }

    public static void printLeaderCard(LeaderCard card, String backgroundColor) {
        if (card == null)
            return;
        System.out.print(backgroundColor);
        System.out.print("CARD OF THE LEADER\n");
        System.out.print("PV: " + card.getVictoryPoints() + " | ");
        for (Requirement r : card.getRequirementsListSafe()) {
            printRequirements(r, backgroundColor);
            System.out.print(" | ");
        }
        printEffects(card.getEffect(), backgroundColor);
    }

    private static void printRequirements(Requirement req, String backgroundColor) {
        if (req == null)
            return;
        if (req instanceof CardRequirementColor)
            printCardRequirementColor((CardRequirementColor) req, backgroundColor);
        else if (req instanceof CardRequirementColorAndLevel)
            printCardRequirementColorAndLevel((CardRequirementColorAndLevel) req, backgroundColor);
        else if (req instanceof CardRequirementResource)
            printCardRequirementResource((CardRequirementResource) req, backgroundColor);
    }

    private static void printCardRequirementColor(CardRequirementColor req, String backgroundColor) {
        System.out.print("Card and Color Req: ");
        if (req.getQuantity() == 1) {
            System.out.print("a ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" card");
        } else {
            System.out.print(req.getQuantity() + " ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" cards");
        }

    }

    private static void printCardRequirementColorAndLevel(CardRequirementColorAndLevel req, String backgroundColor) {
        System.out.print("Card, Color and Level Req: ");
        if (req.getQuantity() == 1) {
            System.out.print("a ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" card of at lest level " + req.getLevel());

        } else {
            System.out.print(req.getLevel() + " ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" cards of at lest level " + req.getLevel());
        }
    }

    private static void printCardRequirementResource(CardRequirementResource req, String backgroundColor) {
        System.out.print("Resources Req: ");
        if (req.getQuantity() == 1) {
            System.out.print("a ");
            printResName(req.getResourceType(), backgroundColor);
        } else {
            System.out.print(req.getQuantity() + " ");
            printResName(req.getResourceType(), backgroundColor);
        }
    }

    private static void printEffects(Effect effect, String backgroundColor) {
        if (effect == null)
            return;
        if (effect instanceof DiscountLeaderEffect)
            printDiscountLeaderEffect((DiscountLeaderEffect) effect, backgroundColor);
        else if (effect instanceof ExtraProductionLeaderEffect)
            printExtraProductionLeaderEffect((ExtraProductionLeaderEffect) effect, backgroundColor);
        else if (effect instanceof ExtraSlotLeaderEffect)
            printExtraSlotLeaderEffect((ExtraSlotLeaderEffect) effect, backgroundColor);
        else if (effect instanceof WhiteMarbleLeaderEffect)
            printWhiteMarbleEffect((WhiteMarbleLeaderEffect) effect, backgroundColor);
        System.out.print("\n");
    }

    private static void printDiscountLeaderEffect(DiscountLeaderEffect e, String backgroundColor) {
        System.out.print("Card Discount Eff: ");
        if (e.getDiscountAmount() == 1) {
            System.out.print(" a ");
            printResName(e.getDiscountType(), backgroundColor);
            System.out.print(" off");
        } else {
            System.out.print(" " + e.getDiscountAmount());
            printResName(e.getDiscountType(), backgroundColor);
            System.out.print(" off");
        }
    }

    private static void printExtraProductionLeaderEffect(ExtraProductionLeaderEffect e, String backgroundColor) {
        System.out.print("Extra Prod Eff: ");
        String line;
        if (e.getRequiredInputNumber() == 1) {
            System.out.print("from: ");
            printResName(e.getRequiredInputType(), backgroundColor);
        } else {
            System.out.print("from: " + e.getRequiredInputNumber() + " ");
            printResName(e.getRequiredInputType(), backgroundColor);
        }
        if (e.getExtraOutputQuantity() == 1)
            System.out.print(" --> 1 Res of your choice ");
        else
            System.out.print(" --> " + e.getExtraOutputQuantity() + " resources of your choice ");
        System.out.print("and ");
        printResName(e.getExtraOutputType(), backgroundColor);
    }

    private static void printExtraSlotLeaderEffect(ExtraSlotLeaderEffect e, String backgroundColor) {
        System.out.print("Depot Slots Eff: ");
        if (e.extraSlotGetResourceNumber() == 1) {
            System.out.print("a extra Slot for ");
        } else {
            System.out.print(+e.extraSlotGetResourceNumber() + " extra Slots for ");
        }
        printResName(e.extraSlotGetType(), backgroundColor);
    }

    private static void printWhiteMarbleEffect(WhiteMarbleLeaderEffect e, String backgroundColor) {
        System.out.print("White Marble Eff: ");
        if (e.getExtraResourceAmount() == 1) {
            System.out.print("get a ");
            printResName(e.getExtraResourceType(), backgroundColor);
            System.out.print("from a WhiteMarble");

        } else {
            System.out.print("get " + e.getExtraResourceAmount() + " ");
            printResName(e.getExtraResourceType(), backgroundColor);
            System.out.print(" from a WhiteMarble");
        }
    }

     /*
    #############################################################################################
    DEVCARDS METHODS
    #############################################################################################
     */

    /*public static String printCardInfo(DevCard devCard) {
        if (devCard == null)
            return "Empty slot";
        return devCard.toString();
    }*/

    public static void printCardInfo(DevCard devCard, String backgroundColor) {
        if (devCard == null)
            System.out.print("Empty Slot");
        else
            printCardInfoIfValid(devCard, backgroundColor);
    }

    //Prints a DevCard if it is a valid card (it has all the attributes it is supposed to have)
    public static void printCardInfoIfValid(DevCard devCard, String backgroundColor) {
        System.out.print(AnsiCommands.BLACK.getTextColor());
        printCardColorAndLevel(devCard.getLevel(), devCard.getColour(), AnsiCommands.BLACK.getTextColor());
        System.out.print(" VP: " + devCard.getVictoryPoints() + " | ");
        System.out.print("Prod: ");
        printGeneralResourcesMapOnALine(devCard.getProductionInput(), AnsiCommands.BLACK.getTextColor());
        System.out.print(" ------> ");
        printGeneralResourcesMapOnALine(devCard.getProductionOutput(), AnsiCommands.BLACK.getTextColor());
        System.out.print(" | ");
        System.out.print("Cost: ");
        printGeneralResourcesMapOnALine(devCard.getCost(), AnsiCommands.BLACK.getTextColor());
        System.out.print(backgroundColor);
    }

    private static void printCardColorAndLevel(Integer level, DevCardColour color, String backgroundColor) {
        switch (color) {
            case PURPLE:
                System.out.print(AnsiCommands.PURPLE.getTextColor());
                System.out.print("C: ");
                printColor(color, AnsiCommands.PURPLE.getTextColor());
                break;
            case GREEN:
                System.out.print(AnsiCommands.GREEN.getTextColor());
                System.out.print("C: ");
                printColor(color, AnsiCommands.GREEN.getTextColor());
                break;
            case BLUE:
                System.out.print(AnsiCommands.BLUE.getTextColor());
                System.out.print("C: ");
                printColor(color, AnsiCommands.BLUE.getTextColor());
                break;
            case YELLOW:
                System.out.print(AnsiCommands.YELLOW.getTextColor());
                System.out.print("C: ");
                printColor(color, AnsiCommands.YELLOW.getTextColor());
                break;
        }
        System.out.print(", L: " + level);
        System.out.print(backgroundColor);
        System.out.print(" | ");
    }

    /*
    #############################################################################################
    LORENZOS ACTION METHODS
    #############################################################################################
     */

    public static void printLorenzosAction(SoloActionToken soloActionToken){
        if(soloActionToken instanceof DiscardToken){
            printDiscardToken((DiscardToken) soloActionToken);
        } else if(soloActionToken instanceof ShuffleToken) {
            printShuffleToken((ShuffleToken) soloActionToken);
        } else if(soloActionToken instanceof FaithPointToken){
            printFaithPointToken((FaithPointToken) soloActionToken, null);
        }
        System.out.print("\n");

    }

    private static void printDiscardToken(DiscardToken token) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("\n   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |       LORENZO PLAYED       |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");
        String fullLine = "The Almighty Lorenzo The Magnificent decided to take for His selfish needs ";
        if(token.getNumCards() == 1){
            String[] lines = CliView.splitInLinesBySize(fullLine, 27);
            for (String line : lines) {
                System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
            }

            System.out.print("    | ");
            printNumberWithColor(token.getNumCards(), getColorOfDevColor(token.getCardColour()), AnsiCommands.BLACK.getTextColor());
            System.out.print(" ");
            printColor(token.getCardColour(), AnsiCommands.BLACK.getTextColor());
            switch (token.getCardColour()) {
                case YELLOW:
                case PURPLE:
                    System.out.print(" card!" + " ".repeat(27 - 14) + "|.\n");
                    break;
                case GREEN:
                    System.out.print(" card!" + " ".repeat(27 - 13) + "|.\n");
                    break;
                case BLUE:
                    System.out.print(" card!" + " ".repeat(27 - 12) + "|.\n");
                    break;
            }

        } else {
            fullLine = fullLine + token.getNumCards() + " ";
            String[] lines = CliView.splitInLinesBySize(fullLine, 27);
            for (String line : lines) {
                System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
            }

            System.out.print("    | ");
            printNumberWithColor(token.getNumCards(), getColorOfDevColor(token.getCardColour()), AnsiCommands.BLACK.getTextColor());
            System.out.print(" ");
            printColor(token.getCardColour(), AnsiCommands.BLACK.getTextColor());
            switch (token.getCardColour()) {
                case YELLOW:
                case PURPLE:
                    System.out.print(" cards!" + " ".repeat(27 - 15) + "|.\n");
                    break;
                case GREEN:
                    System.out.print(" cards!" + " ".repeat(27 - 14) + "|.\n");
                    break;
                case BLUE:
                    System.out.print(" cards!" + " ".repeat(27 - 13) + "|.\n");
                    break;
            }
        }

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());

    }

    private static void printFaithPointToken(FaithPointToken token, String endingForShuffleToken) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("\n   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |       LORENZO PLAYED       |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");
        String fullLine = "The Almighty Lorenzo The Magnificent charmed the Vatican with His foul words: He advanced of ";
        String[] lines = CliView.splitInLinesBySize(fullLine, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        System.out.print("    | ");
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print(token.getFaithPoints() + " ");
        System.out.print(AnsiCommands.BLACK.getTextColor());
        printResName(ResourceType.FAITHPOINT, AnsiCommands.BLACK.getTextColor());
        System.out.print(" on the FaithTrack");
        System.out.print(" !" + " ".repeat(27 - 23) + "|.\n");

        if(endingForShuffleToken != null){
            System.out.print("    |                            |.\n");
            lines = CliView.splitInLinesBySize(endingForShuffleToken, 27);
            for (String line : lines) {
                System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
            }
        }

        printSmallScrollClosing();

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    private static void printShuffleToken(ShuffleToken token) {
        String end = "His Lordness was vicious in another way: He shuffled all the tokens!";

        printFaithPointToken(token, end);
    }

    private static void printSmallScrollClosing(){
        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");
    }


    /*
    #############################################################################################
    RESOURCES-RELATED METHODS
    #############################################################################################
     */

    //Prints only a SINGLE symbol of the resources with their corresponding color. The background color is the one which must be
    //reinstate after the resource has been printed
    private static void printResName(ResourceType res, String boundaryColor) {
        switch (res) {
            case COIN:
                System.out.print(AnsiCommands.YELLOW.getTextColor());
                //System.out.print("©©©©©");
                System.out.print("\uD83D\uDCB0");
                System.out.print(boundaryColor);
                break;
            case STONE:
                System.out.print(AnsiCommands.WHITE.getTextColor());
                //System.out.print("Stone");
                System.out.print("\uD83D\uDDFF");
                System.out.print(boundaryColor);
                break;
            case SERVANT:
                System.out.print(AnsiCommands.PURPLE.getTextColor());
                //System.out.print("Servant");
                System.out.print("\uD83D\uDC68");
                System.out.print(boundaryColor);
                break;
            case SHIELD:
                System.out.print(AnsiCommands.BLUE.getTextColor());
                //System.out.print("Shield");
                System.out.print("\uD83D\uDEE1️");
                System.out.print(boundaryColor);
                break;
            case FAITHPOINT:
                System.out.print(AnsiCommands.RED.getTextColor());
                //System.out.print("Point of Faith");
                System.out.print("✝");
                System.out.print(boundaryColor);
                break;
        }
    }

    //Prints the resources and their corresponding quantity given. It prints the whole line of a small scroll
    private static void printResourcesOnScroll(ResourceType type, Integer quantity, String boundaryColor) {
        switch (type) {
            case COIN:
                coins(quantity, boundaryColor);
                break;
            case STONE:
                stones(quantity, boundaryColor);
                break;
            case SERVANT:
                servants(quantity, boundaryColor);
                break;
            case SHIELD:
                shields(quantity, boundaryColor);
                break;
            case FAITHPOINT:
                faithPoints(quantity, boundaryColor);
                break;
        }
    }

    //Prints the resources and their corresponding quantity given
    private static void printResAndNumberWithSameColor(ResourceType res, Integer quantity, String backgroundColor) {
        switch (res) {
            case COIN:
                printResName(res, AnsiCommands.YELLOW.getTextColor());
                break;
            case STONE:
                printResName(res, AnsiCommands.WHITE.getTextColor());
                break;
            case SHIELD:
                printResName(res, AnsiCommands.BLUE.getTextColor());
                break;
            case SERVANT:
                printResName(res, AnsiCommands.PURPLE.getTextColor());
                break;
            case FAITHPOINT:
                printResName(res, AnsiCommands.RED.getTextColor());
                break;
        }
        System.out.print(" : " + quantity);
        System.out.print(backgroundColor);
    }


    //The following methods print the resources and their quantity in a line for a small scroll

    private static void coins(int quantity, String color) {
        System.out.print("    | ");
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        //System.out.print("©©©©©: ");
        System.out.print("\uD83D\uDCB0\uD83D\uDCB0" + " : ");
        if (quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        } else {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void stones(int quantity, String color) {
        System.out.print("    | ");
        System.out.print(AnsiCommands.WHITE.getTextColor());
        //System.out.print("Stones: ");
        System.out.print("\uD83D\uDDFF\uD83D\uDDFF" + " : ");
        if (quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        } else {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void servants(int quantity, String color) {
        System.out.print("    | ");
        System.out.print(AnsiCommands.PURPLE.getTextColor());
        //System.out.print("Servants: ");
        System.out.print("\uD83D\uDC68\uD83D\uDC68" + " : ");
        if (quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        } else {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void shields(int quantity, String color) {
        System.out.print("    | ");
        System.out.print(AnsiCommands.BLUE.getTextColor());
        //System.out.print("Shields: ");
        System.out.print("\uD83D\uDEE1" + "\uD83D\uDEE1️" + " : ");
        if (quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        } else {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void faithPoints(int quantity, String color) {
        System.out.print("    | ");
        System.out.print(AnsiCommands.RED.getTextColor());
        //System.out.print("Points of Faith: ");
        System.out.print("✝✝  : ");
        if (quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        } else {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    /*
    ###########################
    COLOR-RELATED METHODS
    ###########################
     */

    private static void printNumberWithColor(int number, String color, String backgroundColor){
        System.out.print(color);
        System.out.print(number);
        System.out.print(backgroundColor);
    }

    private static String getColorOfDevColor(DevCardColour dev){
        String color = AnsiCommands.resetStyle();
        switch (dev){
            case GREEN:
                color = AnsiCommands.GREEN.getTextColor();
                break;
            case PURPLE:
                color = AnsiCommands.PURPLE.getTextColor();
                break;
            case BLUE:
                color = AnsiCommands.BLUE.getTextColor();
                break;
            case YELLOW:
                color = AnsiCommands.YELLOW.getTextColor();
                break;
        }
        return color;
    }

    //Prints the name of the color using the corresponding color. The background color is the one which must be
    //reinstate after the resource has been printed
    private static void printColor(DevCardColour color, String backgroundColor) {
        switch (color) {
            case BLUE:
                System.out.print(AnsiCommands.BLUE.getTextColor());
                System.out.print("BLUE");
                System.out.print(backgroundColor);
                break;
            case GREEN:
                System.out.print(AnsiCommands.GREEN.getTextColor());
                System.out.print("GREEN");
                System.out.print(backgroundColor);
                break;
            case PURPLE:
                System.out.print(AnsiCommands.PURPLE.getTextColor());
                System.out.print("PURPLE");
                System.out.print(backgroundColor);
                break;
            case YELLOW:
                System.out.print(AnsiCommands.YELLOW.getTextColor());
                System.out.print("YELLOW");
                System.out.print(backgroundColor);
                break;
        }
    }

    /*
    ###########################
    GENERAL HELPING METHODS (they don't print stuff)
    ###########################
     */

    private static String[] splitInLinesBySize(String string, int limit) throws IllegalArgumentException {
        List<String> words = Arrays.stream(string.split(" ")).map(String::strip).collect(Collectors.toList());
        ArrayList<String> lines = new ArrayList<>();
        lines.add("");
        int c = 0;
        for (String word : words) {
            if (word.length() > limit) throw new IllegalArgumentException("word is too long");
            if (lines.get(c).equals("")) lines.set(c, word);
            else if (lines.get(c).length() + word.length() < limit) lines.set(c, lines.get(c) + " " + word);
            else {
                c++;
                lines.add(word);
            }
        }
        return lines.toArray(new String[0]);
    }

    private static String findPlayerByState(List<Player> players, PlayerState state) {
        return players.stream().filter(x -> x.getPlayerState() == state).findAny().map(Player::getNickName).orElseThrow(NoSuchElementException::new);
    }

    private static String getOtherPlayersNicknames(Game gameModel, String currentPlayer) {
        List<String> nicknames = gameModel.getPlayers().stream().map(Player::getNickName).filter(x -> !x.equals(currentPlayer)).collect(Collectors.toList());
        String output = "";
        int j = 0;
        for (String name : nicknames) {
            if (j != 0)
                output = output + ", ";
            else j++;
            output = output + name;
        }
        return output;
    }

    public static void printHolpMessage() {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.resetStyle());
        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("***********************************************************************************\n");
        /*String beginning = "My Master, thee mustn't feel lost: thou devoted servant is here to holp you out! Hither presented the commands are: ";
        String[] lines = CliView.splitInLinesBySize(beginning, 40);
        for (String line : lines) {
            System.out.print(line + "\n");
        }*/
        System.out.print("My Master, thee mustn't feel lost: thou devoted servant is here to holp you out!\n");
        System.out.print("Hither presented the commands are:\n");
        printCommandAndString(CliCommandType.QUIT, ": disconnects you from the game", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.SETNICKNAME, ": sets your nickname", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.SETNUMOFPLAYERS, ": sets the number of the players in your game", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.CONFIGURESTART, ": plays your beginning decisions", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.GETRESOURCESFROMMARKET, ": tells you how many resources you'd get from the market", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.BUYFROMMARKET, ": gets you the resources from the market", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.GETDEVCARDCOST, ": tells you how much a DevCard would cost", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.BUYDEVCARD, ": buys a DevCard", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.GETPRODUCTIONCOST, ": tells you how much producing would cost you", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.ACTIVATEPRODUCTION, ": activates your production", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.DISCARDLEADER, ": discards a LeaderCard", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.ACTIVATELEADER, ": activates a LeaderCard", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.MOVEBETWEENSHELF, ": moves resources between shelves", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.MOVELEADERTOSHELF, ": moves resources from Leader Depots to the Depot shelves", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.MOVESHELFTOLEADER, ": moves resources from Depot shelves to the Leader Depots", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.SEEPLAYERBOARD, ": lets you see the public PlayerBoard of another player", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.PRINTMYBOARD, ": lets you see your PlayerBoard", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.ENDTURN, ": ends your turn", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.SEEOTHERSPLAYERSNAMES, ": tells your your competitors' names", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.MOVESHELFTOLEADER, ": moves resources from Depot shelves to the Leader Depots", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.HELP, ": less cool way to ask for this awesome guide", AnsiCommands.BLUE.getTextColor());
        printCommandAndString(CliCommandType.HOLP, ": cooler way to ask for this awesome guide", AnsiCommands.BLUE.getTextColor());

        System.out.print("***********************************************************************************\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    private static void printCommandAndString(CliCommandType command, String line, String backgroundColor){
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        System.out.print(command);
        System.out.print(backgroundColor);
        System.out.print(line + "\n");
    }

    public static void printDisconnectionUpdate(String changedPlayerName, boolean disconnected) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("\n   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");

        String message;
        if(disconnected)
            message = "My master, thy dreadful competitor " + changedPlayerName + " was so afraid of thy excellence that cowardly left the battle!";
        else
            message = "My master, thy dreadful competitor " + changedPlayerName + " found the courage to challenge thee and is back in the battle. Fear thou not because braver you are!";

        String[] lines = CliView.splitInLinesBySize(message, 27);
        for (String line : lines) {
            System.out.print("    | " + line + " ".repeat(27 - line.length()) + "|.\n");
        }
        System.out.print("    |                            |.\n");

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }






    /*public static void main(String[] args){
        //System.out.println("Points of Faith: ".length());
        System.out.print(" |  |\n");
        System.out.print(" |  |\n");
        System.out.print("_|  |_\n");
        System.out.print("\\    /\n");
        System.out.print(" \\  /\n");
        System.out.print("  \\/\n");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 30);
        map.put("C", 4);
        for(Map.Entry<String, Integer> e: map.entrySet())
            System.out.println(e.getKey() + " "+ e.getValue());


        List<Map.Entry<String, Integer>> results = new LinkedList<>(map.entrySet());
        results.sort(Map.Entry.comparingByValue());
        for(Map.Entry<String, Integer> e: results)
            System.out.println(e.getKey() + " " + e.getValue());
        PopeTile popeTile = new PopeTile(1,ReportNum.REPORT1);
        List<PopeTile> popeTiles =new ArrayList<>();
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        printFaithTrack(3,popeTiles);
    }*/

}
