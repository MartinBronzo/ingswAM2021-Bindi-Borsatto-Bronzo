package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.view.readOnlyModel.Game;

public class ModelUpdate implements ResponseInterface{
    private Game game;

    public ModelUpdate(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.UPDATE;
    }
}
