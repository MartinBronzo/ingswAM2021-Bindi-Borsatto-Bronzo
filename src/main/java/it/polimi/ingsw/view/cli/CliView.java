package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.view;

import java.util.Map;

public class CliView implements view {

    public static void printWelcome(){
        System.out.println("                                                                                                                                      \n" +
                                   "  /\\\\,/\\\\,               ,                                  /\\       -__ /\\                                                           \n" +
                                   " /| || ||    _          ||                                 ||          || \\,                _    '               _                    \n" +
                                   " || || ||   < \\,  _-_, =||=  _-_  ,._-_  _-_,        /'\\\\ =||=        /|| /    _-_  \\\\/\\\\  < \\, \\\\  _-_,  _-_,  < \\, \\\\/\\\\  _-_  _-_  \n" +
                                   " ||=|= ||   /-|| ||_.   ||  || \\\\  ||   ||_.        || ||  ||         \\||/-   || \\\\ || ||  /-|| || ||_.  ||_.   /-|| || || ||   || \\\\ \n" +
                                   "~|| || ||  (( ||  ~ ||  ||  ||/    ||    ~ ||       || ||  ||          ||  \\  ||/   || || (( || ||  ~ ||  ~ || (( || || || ||   ||/   \n" +
                                   " |, \\\\,\\\\,  \\/\\\\ ,-_-   \\\\, \\\\,/   \\\\,  ,-_-        \\\\,/   \\\\,       _---_-|, \\\\,/  \\\\ \\\\  \\/\\\\ \\\\ ,-_-  ,-_-   \\/\\\\ \\\\ \\\\ \\\\,/ \\\\,/  \n" +
                                   "_-                                                                                                                                    \n" +
                                   "                                                                                                                                      \n");

    }

    public static void printGameState(Game gamemodel, String nickname) throws NullPointerException {

    }

    public static void printError(String error) {
    }

    public static void printinfo(String info) {
    }

    public static void printSetUpView(int nLeadersToDiscard, int resourcesToTake) {
    }

    public static void printResourcesMap(Map<ResourceType, Integer> resourcesMap) {

    }
}
