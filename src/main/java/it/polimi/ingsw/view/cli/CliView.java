package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.view;

import javax.swing.*;
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
//SPAZI TRA I BORDI DELLA PERGAMENA: 28
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

    public static void printResourcesMap(Map<ResourceType, Integer> resourcesMap, String heading) {
        String[] lines = CliView.splitInLinesBySize(heading, 27);

        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.GREEN.getTextColor());
        System.out.print("   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }

      for(Map.Entry<ResourceType, Integer> e: resourcesMap.entrySet())
          printResources(e.getKey(), e.getValue());

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }

    public static void printFinalScores(List<Map.Entry<String, Integer>> results) {
        System.out.print(AnsiCommands.clear());
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("   ______________________________\n" +
                " / \\                             \\.\n");
        System.out.print("|   |                            |.\n");
        System.out.print(" \\_ |                            |.\n" +
                "    |                            |.\n");

        String[] lines = CliView.splitInLinesBySize("The game hath sadly come to an end, we shall see the final scores:", 27);
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }
        System.out.print("    |                            |.\n");

        for(Map.Entry<String, Integer> e: results)
            printPlayersAndScore(e.getKey(), e.getValue());

        System.out.print("    |                            |.\n");
        lines = CliView.splitInLinesBySize("We shall crown our King,", 27);
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }
        lines = CliView.splitInLinesBySize(results.get(0).getKey(), 27);
        for (String line: lines) {
            System.out.print("    | ");
            System.out.print(AnsiCommands.YELLOW.getTextColor());
            System.out.print(line+ " ".repeat(27-line.length()));
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("|.\n");
        }
        System.out.print("    |                            |.\n");

        System.out.print(AnsiCommands.RED.getTextColor());
        lines = CliView.splitInLinesBySize("May long live the King!", 27);
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }

        System.out.print(
                "    |   _________________________|___\n" +
                        "    |  /                            /.\n" +
                        "    \\_/____________________________/.\n");

        System.out.print(AnsiCommands.resetStyle() + AnsiCommands.clearLine());
    }


    /*
    ###########################
    HELPING METHODS
    ###########################
     */

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


    public static void main(String[] args){
        //System.out.println("Points of Faith: ".length());
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 30);
        map.put("C", 4);
        for(Map.Entry<String, Integer> e: map.entrySet())
            System.out.println(e.getKey() + " "+ e.getValue());


        List<Map.Entry<String, Integer>> results = new LinkedList<>(map.entrySet());
        Collections.sort(results, Comparator.comparing(Map.Entry::getValue));
        for(Map.Entry<String, Integer> e: results)
            System.out.println(e.getKey() + " " + e.getValue());
        PopeTile popeTile = new PopeTile(1,ReportNum.REPORT1);
        List<PopeTile> popeTiles =new ArrayList<>();
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        popeTiles.add(popeTile);
        printfaithTrack(3,popeTiles);
    }

    private static void coins(int quantity){
        System.out.print("    | ");
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        System.out.print("©©©©©: ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                   |.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                  |.\n" );
            System.out.print("");
        }
    }

    private static void stones(int quantity){
        System.out.print("    | ");
        System.out.print(AnsiCommands.WHITE.getTextColor());
        System.out.print("Stones: ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                  |.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                 |.\n" );
            System.out.print("");
        }
    }

    private static void servants(int quantity){
        System.out.print("    | ");
        System.out.print(AnsiCommands.PURPLE.getTextColor());
        System.out.print("Servants: ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                |.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("               |.\n" );
            System.out.print("");
        }
    }

    private static void shields(int quantity){
        System.out.print("    | ");
        System.out.print(AnsiCommands.BLUE.getTextColor());
        System.out.print("Shields: ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                 |.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("                |.\n" );
            System.out.print("");
        }
    }

    private static void faithPoints(int quantity){
        System.out.print("    | ");
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("Points of Faith: ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("         |.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print("        |.\n" );
            System.out.print("");
        }
    }

    private static void printPlayersAndScore(String name, Integer score){
        String fullLine = name + ": " + score;
        String[] lines = CliView.splitInLinesBySize(fullLine, 27);
        for(String line: lines){
            System.out.print("    | ");
            System.out.print(AnsiCommands.GREEN.getTextColor());
            System.out.print(line+ " ".repeat(27-line.length()));
            System.out.print(AnsiCommands.RED.getTextColor());
            System.out.print("|.\n");
        }
    }

    private static void printResources(ResourceType type, Integer quantity){
        switch (type){
            case COIN:
                coins(quantity);
                break;
            case STONE:
                stones(quantity);
                break;
            case SERVANT:
                servants(quantity);
                break;
            case SHIELD:
                shields(quantity);
                break;
            case FAITHPOINT:
                faithPoints(quantity);
                break;
        }
    }

    private static void printDivider(AnsiCommands color){
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

    private static void printfaithTrack(int position, List<PopeTile> popeTiles){
        System.out.println("⚀⚀⚀⚀⚀⚀♕♕⚀⚀"+AnsiCommands.YELLOW.getTextColor()+"⚀⚀♕♕⚀⚀☩☩"+AnsiCommands.resetStyle()+"♕♕⚀⚀⚀⚀"+AnsiCommands.YELLOW.getTextColor()+"♕♕⚀⚀⚀⚀♕♕☩☩"+AnsiCommands.resetStyle()+"⚀⚀♕♕"+AnsiCommands.YELLOW.getTextColor()+"⚀⚀⚀⚀♕♕⚀⚀⚀⚀☩☩"+AnsiCommands.resetStyle());
        System.out.print(AnsiCommands.YELLOW.getBackgroundColor()+AnsiCommands.BLACK.getTextColor());
        System.out.println(" ".repeat(position*2)+"⸡⸠"+" ".repeat((24-position)*2)+AnsiCommands.resetStyle());
        System.out.println("⚀⚀⚀⚀⚀⚀♕♕⚀⚀"+AnsiCommands.YELLOW.getTextColor()+"⚀⚀♕♕⚀⚀☩☩"+AnsiCommands.resetStyle()+"♕♕⚀⚀⚀⚀"+AnsiCommands.YELLOW.getTextColor()+"♕♕⚀⚀⚀⚀♕♕☩☩"+AnsiCommands.resetStyle()+"⚀⚀♕♕"+AnsiCommands.YELLOW.getTextColor()+"⚀⚀⚀⚀♕♕⚀⚀⚀⚀☩☩"+AnsiCommands.resetStyle());
        System.out.print(AnsiCommands.RED.getTextColor());
        if (popeTiles.get(0).isDiscarded()){
            System.out.print(" ".repeat(18));
        }else if (popeTiles.get(0).isChanged()){
            System.out.print(" ".repeat(12)+"\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B"+" ".repeat(2));
        }else{
            System.out.print(" ".repeat(12)+"\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06"+" ".repeat(2));
        }
        if (popeTiles.get(1).isDiscarded()){
            System.out.print(" ".repeat(16));
        }else if (popeTiles.get(1).isChanged()){
            System.out.print(" ".repeat(9)+"\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B"+" ".repeat(3));
        }else{
            System.out.print(" ".repeat(9)+"\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06"+" ".repeat(3));
        }
        if (popeTiles.get(2).isDiscarded()){
            System.out.print("\n");
        }else if (popeTiles.get(2).isChanged()){
            System.out.print(" ".repeat(8)+"\uD83C\uDC2B\uD83C\uDC2B\uD83C\uDC2B"+"\n");
        }else{
            System.out.print(" ".repeat(8)+"\uD83C\uDC06\uD83C\uDC06\uD83C\uDC06"+"\n");
        }
        System.out.print(AnsiCommands.resetStyle()+AnsiCommands.clearLine());
    }


}
