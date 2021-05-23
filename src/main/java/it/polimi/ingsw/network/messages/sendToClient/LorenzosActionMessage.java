package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;

public class LorenzosActionMessage implements ResponseInterface {
    SoloActionToken soloActionToken;

    public LorenzosActionMessage(SoloActionToken soloActionToken){
        this.soloActionToken = soloActionToken;
    }

    public SoloActionToken getSoloActionToken() {
        return soloActionToken;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.LORENZOSACTION;
    }
}
