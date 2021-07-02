package it.polimi.ingsw;

import it.polimi.ingsw.controller.Server;
import it.polimi.ingsw.view.cli.CliClient;
import it.polimi.ingsw.view.gui.GuiClient;

import java.io.IOException;

/**
 * This class is used as the Application launcher.
 */
public class Main {

    /**
     * Prepares the Game by starting the clients
     * @param args the strings given by the client which specify whether to run a GUI or a CLI
     */
    public static void main(String [] args) {
        //Default values
        boolean cliParam = false;
        boolean serverParam = false;
        String address = "127.0.0.1";
        int port = 9047;
        //String address = "";
        //int port = -1;

        //The user specifies whether they want to use the CLI
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("--server")) {
                serverParam = true;
                port = Integer.parseInt(args[i + 1]);
                i++;
            }
            if (args[i].equals("--cli") || args[i].equals("-c"))
                cliParam = true;
            else if (args[i].equals("--IP")) {
                //"--IP" must be followed by the address and port numbers
                address = args[i + 1];
                port = Integer.parseInt(args[i + 2]);
                i = i + 2;
            }
            i++;
        }

        if (serverParam) {
            Server server = new Server(port);
            server.startServer();
        } else {
            //We create the client
            if (cliParam) {
                //We open the CLI
                CliClient cliClient = new CliClient(port, address);
                cliClient.startConnection();
                cliClient.doConnection();
            } else {
                //We open the GUI
                GuiClient guiClient = new GuiClient(port, address);
                guiClient.startConnection();
                guiClient.doConnection();
            }
        }
    }
}
