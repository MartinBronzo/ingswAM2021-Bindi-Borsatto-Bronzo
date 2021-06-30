package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.view.lightModel.Game;

/**
 * This message is used to send the player an update of the Model.
 */
public class ModelUpdate implements ResponseInterface {
    private Game game;

    /**
     * Constructs a ModelUpdate
     * @param game the LightModel object which contains the update of the Model
     */
    public ModelUpdate(Game game) {
        this.game = game;
    }

    /**
     * Returns the LightModel object which contains the update of the Model
     * @return the LightModel object which contains the update of the Model
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the LightModel object, which contains the update of the Model, stored in this ModelUpdate message
     * @param game the LightModel object which contains the update of the Model
     */
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.UPDATE;
    }
}
