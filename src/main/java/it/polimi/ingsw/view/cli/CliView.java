package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.view;

public class CliView implements view {
    private final Game gameModel;

    public CliView(Game gameModel) {
        this.gameModel = gameModel;
    }

    public static void printWelcome(){
    }
}
