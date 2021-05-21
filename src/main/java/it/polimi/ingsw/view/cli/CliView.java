package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.model.soloGame.SoloBoard;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;
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

    public static void printGameState(Game gameModel, String nickname) throws NullPointerException, NoSuchElementException {
        if (gameModel == null || nickname==null || nickname.equals("")) return;

        Player player = gameModel.getPlayers().stream().filter(p -> p.getNickName().equals(nickname)).findAny().orElseThrow(NoSuchElementException::new);
        switch (player.getPlayerState()) {
            case WAITING4TURN:
            case WAITING4LASTTURN:
                System.out.println("abcd");
                break;
            /*case WAITING4OTHERBEGINNINGDECISIONS:
                break;*/
            case PLAYING:
                printMarket(gameModel.getMainBoard());
                break;
            case WAITING4GAMESTART:
                break;
            case PLAYINGLASTTURN:
                printMarket(gameModel.getMainBoard());
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

      /*for(Map.Entry<ResourceType, Integer> e: resourcesMap.entrySet())
          printResources(e.getKey(), e.getValue(), AnsiCommands.GREEN.getTextColor());*/
      printGeneralResourcesMaps(resourcesMap, AnsiCommands.GREEN.getTextColor() );

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


        lines = CliView.splitInLinesBySize("We shall crown our Lord,", 27);
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
        lines = CliView.splitInLinesBySize("May long live the Lord!", 27);
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
        printfaithTrack(3,popeTiles);
    }

    private static void coins(int quantity, String color){
        System.out.print("    | ");
        System.out.print(AnsiCommands.YELLOW.getTextColor());
        //System.out.print("©©©©©: ");
        System.out.print("\uD83D\uDCB0\uD83D\uDCB0" +" : ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n" );
            System.out.print("");
        }
    }

    private static void stones(int quantity, String color){
        System.out.print("    | ");
        System.out.print(AnsiCommands.WHITE.getTextColor());
        //System.out.print("Stones: ");
        System.out.print("\uD83D\uDDFF\uD83D\uDDFF" + " : ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void servants(int quantity, String color){
        System.out.print("    | ");
        System.out.print(AnsiCommands.PURPLE.getTextColor());
        //System.out.print("Servants: ");
        System.out.print("\uD83D\uDC68\uD83D\uDC68" + " : ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void shields(int quantity, String color){
        System.out.print("    | ");
        System.out.print(AnsiCommands.BLUE.getTextColor());
        //System.out.print("Shields: ");
        System.out.print("\uD83D\uDEE1"+"\uD83D\uDEE1️" + " : ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n");
            System.out.print("");
        }
    }

    private static void faithPoints(int quantity, String color){
        System.out.print("    | ");
        System.out.print(AnsiCommands.RED.getTextColor());
        //System.out.print("Points of Faith: ");
        System.out.print("✝✝  : ");
        if(quantity < 10) {
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(20) + "|.\n");
        }
        else{
            System.out.print(quantity);
            System.out.print(color);
            System.out.print(" ".repeat(19) + "|.\n" );
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

    private static void printResources(ResourceType type, Integer quantity, String boundaryColor){
        switch (type){
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

    public static void printUnusedLeaderCards(List<LeaderCard> list){
        System.out.print(AnsiCommands.RED.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.print("Thy Not Active Card of the Leader are hither presented:\n\n");
        printLeaderCards(list, AnsiCommands.RED.getTextColor());
    }

    public static void printUsedLeaderCards(List<LeaderCard> list){
        System.out.print(AnsiCommands.GREEN.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.print("Thy Active Card of the Leader are hither presented:\n\n");
        printLeaderCards(list, AnsiCommands.GREEN.getTextColor());
    }

    public static void printLeaderCards(List<LeaderCard> list, String backgroundColor){
        for(LeaderCard lD: list){
            printLeaderCard(lD, backgroundColor );
            System.out.print("\n");
        }

        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

    }

    public static void printLeaderCard(LeaderCard card, String backgroundColor){
        System.out.print(backgroundColor);
        System.out.print("CARD OF THE LEADER\n");
        System.out.print("Points of Victory: " + card.getVictoryPoints() + "\n");
        for(Requirement r: card.getRequirementsListSafe())
            printRequirements(r, backgroundColor);
        printEffects(card.getEffect(), backgroundColor);
    }

    private static void printRequirements(Requirement req, String backgroundColor){
        if(req instanceof CardRequirementColor)
            printCardRequirementColor((CardRequirementColor) req, backgroundColor);
        else if(req instanceof CardRequirementColorAndLevel)
            printCardRequirementColorAndLevel((CardRequirementColorAndLevel) req, backgroundColor);
        else if(req instanceof CardRequirementResource)
            printCardRequirementResource((CardRequirementResource) req, backgroundColor);
        System.out.print("\n");
    }

    private static void printCardRequirementColor(CardRequirementColor req, String backgroundColor){
        System.out.print("Requirement of Card and Color: ");
        if(req.getQuantity() == 1) {
            System.out.print("a ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" card");
        }
        else{
            System.out.print(req.getQuantity() + " ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" cards");}

    }

    private static void printCardRequirementColorAndLevel(CardRequirementColorAndLevel req, String backgroundColor){
        System.out.print("Requirement of Card, Color and Level: ");
        if(req.getQuantity() == 1){
            System.out.print("a ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" card of at lest level "+ req.getLevel());

        }else {
            System.out.print(req.getLevel() + " ");
            printColor(req.getCardColour(), backgroundColor);
            System.out.print(" cards of at lest level " + req.getLevel());
        }
    }

    private static void printCardRequirementResource(CardRequirementResource req, String backgroundColor){
        System.out.print("Requirement of Resources: ");
        if(req.getQuantity() == 1) {
            System.out.print("a ");
            printResName(req.getResourceType(), backgroundColor);
        }
        else{
            System.out.print(req.getQuantity() + " ");
            printResName(req.getResourceType(), backgroundColor);
        }
    }

    private static void printEffects(Effect effect, String backgroundColor){
        if(effect instanceof DiscountLeaderEffect)
            printDiscountLeaderEffect((DiscountLeaderEffect) effect, backgroundColor);
        else if(effect instanceof ExtraProductionLeaderEffect)
            printExtraProductionLeaderEffect((ExtraProductionLeaderEffect) effect, backgroundColor);
        else if(effect instanceof ExtraSlotLeaderEffect)
            printExtraSlotLeaderEffect((ExtraSlotLeaderEffect) effect, backgroundColor);
        else if(effect instanceof WhiteMarbleLeaderEffect)
            printWhiteMarbleEffect((WhiteMarbleLeaderEffect) effect, backgroundColor);
        System.out.print("\n");
    }

    private static void printDiscountLeaderEffect(DiscountLeaderEffect e, String backgroundColor) {
        System.out.print("Effect of Card Discount: ");
        if (e.getDiscountAmount() == 1){
            System.out.print(" thou get a ");
            printResName(e.getDiscountType(), backgroundColor);
            System.out.print(" off");
        }else {
            System.out.print(" thou get " + e.getDiscountAmount());
            printResName(e.getDiscountType(), backgroundColor);
            System.out.print(" off");
        }
    }

    private static void printExtraProductionLeaderEffect(ExtraProductionLeaderEffect e, String backgroundColor){
        System.out.print("Effect of Producing More: ");
        String line;
        if(e.getRequiredInputNumber() == 1) {
            System.out.print("if thou grant a ");
            printResName(e.getRequiredInputType(), backgroundColor);
        }
        else {
            System.out.print("if thou grant " + e.getRequiredInputNumber() + " ");
            printResName(e.getRequiredInputType(), backgroundColor);
        }if(e.getExtraOutputQuantity() == 1)
            System.out.print(", thou will receive a resource of your desire ");
        else
            System.out.print(", thou will receive " + e.getExtraOutputQuantity() + " resources of your desire ");
        System.out.println("and one extra point of Faith");
    }

    private static void printExtraSlotLeaderEffect(ExtraSlotLeaderEffect e, String backgroundColor) {
        System.out.print("Effect of More Precious Depot Slots: ");
        if (e.extraSlotGetResourceNumber() == 1){
            System.out.print("thou get a extra slot for safely storing ");
        }else {
            System.out.print("thou get " + e.extraSlotGetResourceNumber() + " extra slots for safely storing ");
        }
        printResName(e.extraSlotGetType(), backgroundColor);
    }

    private static void printWhiteMarbleEffect(WhiteMarbleLeaderEffect e, String backgroundColor){
        System.out.print("Effect of Coloring A White Marble: ");
        if(e.getExtraResourceAmount() == 1) {
            System.out.print("thou get a ");
            printResName(e.getExtraResourceType(), backgroundColor);
            System.out.println(" when thou encounter an annoying WhiteMarble in the Market");

        }else {
            System.out.print("thou get " + e.getExtraResourceAmount() + " ");
            printResName(e.getExtraResourceType(), backgroundColor);
            System.out.print(" when thou encounter an annoying WhiteMarble in the Market");
        }
    }

    private static void printGeneralResourcesMaps(Map<ResourceType, Integer> map, String boundaryColor){
        for(Map.Entry<ResourceType, Integer> e: map.entrySet())
            printResources(e.getKey(), e.getValue(), boundaryColor);
    }

    public static void printStrongBox(Map<ResourceType, Integer> map){
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("     ____________________________.\n");
        String[] lines = CliView.splitInLinesBySize("Thy precious Strongbox is hither presented: ", 27);
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }
        printGeneralResourcesMaps(map, AnsiCommands.BLACK.getTextColor());
        System.out.print("     ____________________________.\n");
    }

    public static void printLeaderDepots(Map<ResourceType, Integer> map){
        System.out.print(AnsiCommands.WHITE.getTextColor());
        System.out.print("     ____________________________.\n");
        String[] lines = CliView.splitInLinesBySize("Thy Extra Slots are hither presented: ", 27);
        for (String line: lines) {
            System.out.print("    | "+line+ " ".repeat(27-line.length())+ "|.\n");
        }
        printGeneralResourcesMaps(map, AnsiCommands.WHITE.getTextColor());
        System.out.print("     ____________________________.\n");
    }

    public static void printDepot(List<DepotShelf> shelves){
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("Thy Depot is hither presented:\n");
        DepotShelf d;
        for( int j = 0; j < shelves.size(); j++){
            d = shelves.get(j);
            if(j == 0)
                System.out.print("         ");
            else if(j == 1)
                System.out.print("    ");
            if(d.getQuantity() == 0)
                if(j == 0)
                    System.out.print("|        |");
                else if (j == 1)
                    System.out.print("|        |        |");
                else
                    System.out.print("|        |        |        |");
            else{
                System.out.print("|   ");
                for(int i = 0; i < d.getQuantity(); i++) {
                    if(i != 0)
                        System.out.print("   ");
                    printResName(d.getResourceType(), AnsiCommands.BLACK.getTextColor());
                    System.out.print("   |");
                }

            }
            System.out.print("\n");
        }
    }

    private static void printResName(ResourceType res, String boundaryColor){

        switch (res){
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

    public static void printBaseProduction(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output){
        System.out.print(AnsiCommands.BLACK.getTextColor());
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        System.out.print("Thy Base Production is hither presented:\n");

        System.out.print("\nFrom: ");
        int j = 0;
        for(Map.Entry<ResourceType, Integer> e: input.entrySet()){
            if(j != 0)
                System.out.print(" + ");
            else
                j++;
            for(int i = 0; i < e.getValue() - 1; i++){
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
        for(Map.Entry<ResourceType, Integer> e: output.entrySet()){
            if(j != 0)
                System.out.print(" + ");
            else
                j++;
            for(int i = 0; i < e.getValue() - 1; i++){
                printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
                System.out.print(" + ");
            }
            printResName(e.getKey(), AnsiCommands.BLACK.getTextColor());
        }
        System.out.print("\n");
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

    }

    public static void printMarket(Board board){
        MarbleType[][] matrix = board.getMarketMatrix();
        System.out.println("___________ "+ board.getMarbleOnSlide().toString());
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j].toString()+"|");
            }
            System.out.println("\n___________");
        }
    }

    public static void printDevGrid(Board board){
        DevCard[][] devGrid = board.getDevMatrix();
        System.out.print(AnsiCommands.RED.getBackgroundColor());
        System.out.print("_____________");
        System.out.println(AnsiCommands.resetStyle());
        int c;
        for (int i = 0; i < devGrid.length; i++) {
            System.out.print(AnsiCommands.RED.getBackgroundColor());
            System.out.print("|");
            System.out.print(AnsiCommands.resetStyle());
            for (int j = 0; j < devGrid[i].length; j++) {
                c = (1+j+4*i);
                System.out.print(c);
                if (c<10){
                    System.out.print(" "+AnsiCommands.RED.getBackgroundColor()+"|"+AnsiCommands.resetStyle());
                } else {
                    System.out.print(AnsiCommands.RED.getBackgroundColor()+"|"+AnsiCommands.resetStyle());
                }
            }
            System.out.print("\n"+AnsiCommands.RED.getBackgroundColor());
            System.out.print("_____________");
            System.out.println(AnsiCommands.resetStyle());
        }

        for (int i = 0; i < devGrid.length; i++) {
            for (int j = 0; j < devGrid[i].length; j++) {
                c = (1+i+4*j);
                System.out.println(c+":\t"+ printCardInfo(devGrid[i][j]));
            }
        }

    }

    public static String printCardInfo(DevCard devCard){
        if (devCard==null) return "Empty Slot";
        return devCard.toString();
    }

    private static void printColor(DevCardColour color, String backgroundColor){
        switch (color){
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

}
