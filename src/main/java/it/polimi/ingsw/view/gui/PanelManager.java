package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.view.readOnlyModel.Game;

public class PanelManager {
    private Game gameModel;
    public static PanelManager getInstance() {
        return null;
    }

    public void readMessage(ResponseMessage responseMessage) {
    }

    public void init() {
    }

    public void forceLogout(String logoutMessage) {
    }
}
