package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
//import com.sun.rowset.internal.Row;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.readOnlyModel.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class CliClient extends Client implements Runnable {
    private final int portNumber;
    private final String hostName;
    private final CliView cliView;
    private final Game gamemodel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private static Gson gson = new Gson();
    private static Thread thread;
    private Map<ResourceType, Integer> resourcesMap;
    private boolean errorReceived;
    private String nickname;


    public CliClient(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.gamemodel = new Game();
        this.cliView = new CliView(this.gamemodel);
    }

    @Override
    public void startConnection() throws IOException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }

        this.manageLogin();
        CliView.printWelcome();
        this.manageGameStarting();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void doConnection() {
        CliCommandType cliCommandType = null;
        do {
            try {
                cliCommandType = CliCommandType.valueOf(stdIn.readLine());
                switch (cliCommandType) {
                    case BUYFROMMARKET:
                        this.buyFromMarket();
                        break;
                    case BUYDEVCARD:
                        break;
                    case ACTIVATEPRODUCTION:
                        break;
                    case DISCARDLEADER:
                        break;
                    case MOVEBETWEENSHELF:
                        break;
                    case MOVELEADERTOSHELF:
                        break;
                    case MOVESHELFTOLEADER:
                        break;
                    case ACTIVATELEADER:
                        break;
                    case ENDTURN:
                        break;
                }
            } catch (IOException e) {
                System.err.println("can't read your stream");
                System.exit(1);
            }catch (IllegalArgumentException e) {
                System.err.println("your Command doesn't exists");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (cliCommandType.equals(CliCommandType.QUIT));
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread.interrupt();

    }


    @Override
    public void sendMessage(Command command) throws NullPointerException {
        if (out == null) throw new NullPointerException("PrintWriter is null");
        out.println(gson.toJson(command));
    }

    @Override
    public synchronized void manageLogin() {
        boolean isNickValid = false;
        while (!isNickValid){
            System.out.println("What's your nickname?\n");
            try {
                nickname = stdIn.readLine();
                Command loginCommand = new Command("login", new LoginMessage(nickname));
                sendMessage(loginCommand);
                isNickValid = Boolean.parseBoolean(in.readLine());
                if (!isNickValid)
                    System.out.println("Nick not Valid\n");
            } catch (IOException e) {
                System.err.println("can't read your stream");
                System.exit(1);
            }
        }
        /*
        client configurator message is not created
         */
    }

    @Override
    protected synchronized void manageGameStarting() {

    }

    @Override
    protected synchronized void buyFromMarket() throws InterruptedException, IOException {
        Boolean isRow;
        int rowColumnNumber = 0;
        int nWhiteMarble = 0;
        List<Integer> leaderCardsId = new ArrayList<>();
        String userResponse = null;

        while (!userResponse.equals("Row") && !userResponse.equals("Column")){
            System.out.println("Row or Column?\n");
            userResponse = stdIn.readLine();
            if (!userResponse.equals("Row") && !userResponse.equals("Column"))
                System.out.println(userResponse + "is not a valid response\n");
        }
        isRow = "Row".equals(userResponse);

        userResponse=null;
        Boolean isResponseValid = false;
        while (!isResponseValid){
            System.out.println("Row/Column number?\n");
            try {
                userResponse = stdIn.readLine();
                rowColumnNumber = Integer.parseInt(userResponse);
                rowColumnNumber--;
                if (isRow)
                    nWhiteMarble = gamemodel.getMainBoard().getNumberOfWhiteMarbleInTheRow(rowColumnNumber);
                else
                    nWhiteMarble = gamemodel.getMainBoard().getNumberOfWhiteMarbleInTheColumn(rowColumnNumber);
                isResponseValid = true;
            } catch (NumberFormatException e){
                System.out.println(userResponse + "is not a number\n");
                isResponseValid = false;
            }
        }
        rowColumnNumber++;

        userResponse = null;
        int c = 1;
        while (nWhiteMarble>0){
            System.out.println("Insert the id of the leader card to be activated for the white marble number " + c
                               + "\n0 if you don't have any whiteMarble LeaderCard Activated\n");
            try {
                userResponse = stdIn.readLine();
                leaderCardsId.add(Integer.parseInt(userResponse));
                nWhiteMarble--;
            } catch (NumberFormatException e) {
                System.out.println(userResponse + "is not a number\n");
            }
        }

        resourcesMap = null;
        errorReceived = false;
        Command getResourcesCommand;
        if (isRow)
            getResourcesCommand = new Command("getResourcesFromMarket", new GetFromMatrixMessage(rowColumnNumber, 0, leaderCardsId));
        else
            getResourcesCommand = new Command("getResourcesFromMarket", new GetFromMatrixMessage(0, rowColumnNumber, leaderCardsId));
        sendMessage(getResourcesCommand);
        while (resourcesMap == null || !errorReceived)
            wait();
        if (errorReceived) {
            return;
        }
        System.out.println("your bought resources:\n" + resourcesMap.toString());
        System.out.println("put the resources in the desired position:\n" + "Commands: MOVETODEPOT, MOVETOLEADER, DISCARD, END");
        userResponse=null;
        List<DepotParams> depotParamsList = new ArrayList<>();
        HashMap<ResourceType, Integer> leaderMap = new HashMap<>();
        HashMap<ResourceType, Integer> discardsMap = new HashMap<>();
        ResourceType resourceType;
        int qt;
        int shelf;
        while (!userResponse.equals("END")){
            switch (userResponse){
                case "MOVETODEPOT":
                    try {
                        System.out.println("Insert ResourceType to be added to shelf\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be added to shelf\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);

                        System.out.println("Insert shelfnumber\n");
                        userResponse = stdIn.readLine();
                        shelf = Integer.parseInt(userResponse);

                        depotParamsList.add(new DepotParams(resourceType, qt, shelf));
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "MOVETOLEADER":
                    try {
                        System.out.println("Insert ResourceType to be added to leaderSlot\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be added to leaderSlot (delete previous quantity on same leadercard\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);
                        leaderMap.put(resourceType, qt);
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "DISCARD":
                    try {
                        System.out.println("Insert ResourceType to be discarded\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be discarded (delete previous quantity on same resorcetype discarded\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);
                        discardsMap.put(resourceType, qt);
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "END":
                    break;
                default:
                    System.out.println("Not Valid line");
            }
        }
        Command buyFromMarket;
        if (isRow)
            buyFromMarket = new Command("buyFromMarket", new BuyFromMarketMessage(rowColumnNumber, 0, leaderCardsId, depotParamsList, leaderMap, discardsMap));
        else
            buyFromMarket = new Command("buyFromMarket", new BuyFromMarketMessage(0, rowColumnNumber, leaderCardsId, depotParamsList, leaderMap, discardsMap));
        sendMessage(buyFromMarket);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String response = in.readLine();
                ResponseMessage responseMessage = gson.fromJson(response, ResponseMessage.class);
                switch (responseMessage.getResponseType()) {
                    case PING:
                        /*call to method to to ping stuff...*/
                        break;
                    case UPDATE:
                        break;
                    case ERROR:
                        break;
                    case EXTRARESOURCEBLABLA:
                        break;
                    case HASHMAPRESOURCES:
                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String hostName ="127.0.0.1";
        int portNumber = 1234;
        Client client = new CliClient(portNumber, hostName);
        client.startConnection();
        client.doConnection();
    }
}
