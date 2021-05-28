package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.gui.panels.WaitingRoomPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PanelManagerTest {

    @BeforeAll
    static void Setup() throws IOException {
        PanelManager.createInstance(new GuiClient()).init();
        PanelManager.getInstance().getEntryPanel().setVisible(false);
    }

    @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        PanelManager.getInstance().getWaitingRoomPanel().setVisible(true);
        System.in.read();
    }

}