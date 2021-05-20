package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.view;

import java.util.*;
import java.util.stream.Collectors;

public class CliView implements view {

    public static void printWelcome(){
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

    public static void printGameState(Game gamemodel, String nickname) throws NullPointerException, NoSuchElementException {
        if (gamemodel == null) throw new NullPointerException("gamemodel is null");
        Player player = gamemodel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().orElseThrow(NoSuchElementException::new);
        switch (player.getPlayerState()) {
            case WAITING4TURN:
            case WAITING4LASTTURN:
                System.out.println("abcd");
                break;
            case WAITING4OTHERBEGINNINGDECISIONS:
                break;
            case PLAYING:
                break;
            case WAITING4GAMESTART:
                break;
            case PLAYINGLASTTURN:
                break;
            case WAITING4GAMEEND:
                break;

            case WAITING4BEGINNINGDECISIONS:
                break;
            case PLAYINGBEGINNINGDECISIONS:
                break;
        }

    }
//28
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
            for (String line: lines) {
                System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
            }
            System.out.print(
                    "    |   _________________________|___\n" +
                            "    |  /                            /.\n" +
                            "    \\_/____________________________/.\n");
        }catch (IllegalArgumentException e){
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
            for (String line: lines) {
                System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
            }
            System.out.print(
                    "    |   _________________________|___\n" +
                            "    |  /                            /.\n" +
                            "    \\_/____________________________/.\n");
        }catch (IllegalArgumentException e){
            System.out.print(AnsiCommands.YELLOW.getBackgroundColor());
            System.out.println(info);
        }
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printSetUpView(int nLeadersToDiscard, int resourcesToTake) {
        System.out.print(AnsiCommands.clear()+AnsiCommands.GREEN.getTextColor());
        System.out.print("     _______________________________________________________\n" +
                                   "    /\\                                                      \\\n" +
                                   "(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)\n" +
                                   "    \\/''''''''''''''''''''''''''''''''''''''''''''''''''''''/\n" +
                                   "    (                                                      (\n" +
                                   "     )                  YOUR LIEGE DEMANDS:                 )\n" +
                                   "    (                                                      (\n" +
                                   "     )            "+ nLeadersToDiscard +" LEADER CARDS OF YOUR CHOICE             )\n" +
                                   "    (                                                      (\n");
        if (resourcesToTake == 0)
            System.out.print(      "     )                                                      )\n");
        else if (resourcesToTake == 1)
            System.out.print(      "     )        IN EXCHANGE OF 1 RESOURCE OF YOUR CHOICE      )\n");
        else
            System.out.print(      "     )       IN EXCHANGE OF 2 RESOURCES OF YOUR CHOICE      )\n");
        System.out.print(
                                   "    (                                                      (\n" +
                                   "    /\\''''''''''''''''''''''''''''''''''''''''''''''''''''''\\    \n" +
                                   "(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)\n" +
                                   "    \\/______________________________________________________/");
        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printResourcesMap(Map<ResourceType, Integer> resourcesMap) {

    }

    private static String[] splitInLinesBySize(String string, int limit) throws IllegalArgumentException{
        List<String> words = Arrays.stream(string.split(" ")).map(String::strip).collect(Collectors.toList());
        ArrayList<String> lines = new ArrayList<>();
        lines.add("");
        int c = 0;
        for (String word: words) {
            if (word.length() > limit) throw new IllegalArgumentException("word is too long");
            if (lines.get(c).equals("")) lines.set(c, word);
            else if (lines.get(c).length() + word.length() < limit) lines.set(c, lines.get(c) + " " + word);
            else{
                c++;
                lines.add(word);
            }
        }
        return lines.toArray(new String[0]);
    }

    public static void printFinalScores(Map<String, Integer> results) {
    }

    public static void printResourcesMap(Map<ResourceType, Integer> resourcesMap, String heading) {

    }
}
