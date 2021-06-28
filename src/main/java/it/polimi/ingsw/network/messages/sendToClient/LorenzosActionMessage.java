package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.soloGame.SoloActionToken;

/**
 * This message is used to communicate to the player what kind of action Lorenzo has made in a Solo Game.
 */
public class LorenzosActionMessage implements ResponseInterface {
    SoloActionToken soloActionToken;

    /**
     * Constructs a LorenzosActionMessage
     * @param soloActionToken the SoloActionToken which has been used by Lorenzo in his turn
     */
    public LorenzosActionMessage(SoloActionToken soloActionToken){
        this.soloActionToken = soloActionToken;
    }

    /**
     * Returns the SoloActionToken which has been used by Lorenzo in his turn
     * @return the SoloActionToken which has been used by Lorenzo in his turn
     */
    public SoloActionToken getSoloActionToken() {
        return soloActionToken;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.LORENZOSACTION;
    }
}
