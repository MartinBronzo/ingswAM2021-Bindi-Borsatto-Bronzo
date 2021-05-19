package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.StringToMessage;
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
    private Game gamemodel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private static final Gson gson = new Gson();
    private static Thread thread;
    private Map<ResourceType, Integer> resourcesMap;
    private int nLeadersToDiscard;
    private int resourcesToTake;
    private String nickname;
    private boolean forceLogout = false;
    String logoutMessage = "Thanks for Playing, See you next time :D";


    public CliClient(int portNumber, String hostName) {
        this.gamemodel = null;
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    @Override
    public void startConnection(){
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
        CliView.printWelcome();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void doConnection() {
        CliCommandType cliCommandType = CliCommandType.SETNICKNAME;
        do {
            try {
                cliCommandType = CliCommandType.valueOf(stdIn.readLine().toUpperCase());
                if (forceLogout)
                    break;
                switch (cliCommandType) {
                    case QUIT:
                        synchronized (this){
                            sendMessage(new Command("quit"));
                        }
                        break;
                    case SETNICKNAME:
                        this.manageLogin();
                        break;
                    case SETNUMOFPLAYERS:
                        this.setNumPlayer();
                        break;
                    case GETRESOURCESFROMMARKET:
                        this.getResourcesFromMarket();
                        break;
                    case BUYFROMMARKET:
                        this.buyFromMarket();
                        break;
                    case GETDEVCARDCOST:
                        this.getDevCardCost();
                        break;
                    case BUYDEVCARD:
                        this.buyDevCard();
                        break;
                    case GETPRODUCTIONCOST:
                        this.getProductionCost();
                        break;
                    case ACTIVATEPRODUCTION:
                        this.activateProduction();
                        break;
                    case DISCARDLEADER:
                        this.discardLeader();
                        break;
                    case MOVEBETWEENSHELF:
                        this.moveBetweenShelves();
                        break;
                    case MOVELEADERTOSHELF:
                        this.moveLeaderToShelf();
                        break;
                    case MOVESHELFTOLEADER:
                        this.moveShelfToLeader();
                        break;
                    case ACTIVATELEADER:
                        this.activateLeader();
                        break;
                    case ENDTURN:
                        synchronized (this){
                            sendMessage(new Command("endTurn"));
                        }
                        break;
                    default:
                        System.err.println("Command not Valid\n");
                }
            } catch (IOException e) {
                System.err.println("can't read your stream");
                System.exit(1);
            }catch (IllegalArgumentException e) {
                System.err.println("your Command doesn't exists");
            }
        }while (cliCommandType.equals(CliCommandType.QUIT) && !forceLogout);
        this.endConnection();
    }

    @Override
    protected synchronized void endConnection() {
        CliView.printinfo(logoutMessage);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O");
            System.exit(1);
        }
        thread.interrupt();
    }


    @Override
    public void sendMessage(Command command) throws NullPointerException {
        if (out == null) throw new NullPointerException("PrintWriter is null");
        out.println(gson.toJson(command));
    }

    @Override
    public synchronized void manageLogin() throws IOException {
        String nickUnchecked;
        System.out.println("What's your nickname?\n");
        nickUnchecked = stdIn.readLine();
        Command loginCommand = new Command("login", new LoginMessage(nickUnchecked));
        sendMessage(loginCommand);
    }

    @Override
    protected synchronized void manageGameStarting() {
        //TODO: no Idea what Server Needs
    }


    @Override
    protected synchronized void setNumPlayer() throws IOException {
        System.out.println("set num Player example: 4;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command setNumPlayerCommand = new Command("setNumPlayer", StringToMessage.toSetNumPlayerMessage(usrCommand));
            sendMessage(setNumPlayerCommand);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void getResourcesFromMarket() throws IOException {
        System.out.println("getResourcesFromMarket example: row 3; 1, 2, 4;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command getResourcesFromMarket = new Command("getResourcesFromMarket", StringToMessage.toMatrixMessage(usrCommand));
            sendMessage(getResourcesFromMarket);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void buyFromMarket() throws IOException {
        System.out.println("buyFromMarket example: row 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2; 4;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command buyFromMarketCommand = new Command("buyFromMarket", StringToMessage.toBuyFromMarketMessage(usrCommand));
            sendMessage(buyFromMarketCommand);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void getDevCardCost() throws IOException {
        System.out.println("getDevCardCost example: row 3; 1, 2, 4;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command getResourcesFromMarket = new Command("getCardCost", StringToMessage.toMatrixMessage(usrCommand));
            sendMessage(getResourcesFromMarket);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void buyDevCard() throws IOException {
        System.out.println("buyDevCard example: row 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2; 4;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command buyDevCard = new Command("buyDevCard", StringToMessage.toBuyDevCardMessage(usrCommand));
            sendMessage(buyDevCard);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void getProductionCost() throws IOException {
        System.out.println("getProductionCost example: 1, 2, 4; 1, 2, 4; TRUE, COIN SERVANT, STONE;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command getProductionCost = new Command("getProductionCost", StringToMessage.toGetProductionCostMessage(usrCommand));
            sendMessage(getProductionCost);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void activateProduction() throws IOException {
        System.out.println("activateProduction example: 1, 2, 4; 1, 2, 4; TRUE, COIN SERVANT, STONE;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command activateProduction = new Command("activateProductionMesssage", StringToMessage.toActivateProductionMessage(usrCommand));
            sendMessage(activateProduction);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void moveBetweenShelves() throws IOException {
        System.out.println("moveBetweenShelves example: 1; 2;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command moveBetweenShelves = new Command("moveBetweenShelves", StringToMessage.toMoveBetweenShelvesMessage(usrCommand));
            sendMessage(moveBetweenShelves);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void moveLeaderToShelf() throws IOException {
        System.out.println("moveLeaderToShelf example: COIN; 2; 1;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command moveLeaderToShelf = new Command("moveLeaderToShelf", StringToMessage.toMoveLeaderToShelfMessage(usrCommand));
            sendMessage(moveLeaderToShelf);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void moveShelfToLeader() throws IOException {
        System.out.println("moveShelfToLeader example: 2; 1;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command moveShelfToLeader = new Command("moveShelfToLeader", StringToMessage.toMoveShelfToLeaderMessage(usrCommand));
            sendMessage(moveShelfToLeader);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void discardLeader() throws IOException {
        System.out.println("discardLeader example: 2;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command discardLeader = new Command("discardLeader", StringToMessage.toLeaderMessage(usrCommand));
            sendMessage(discardLeader);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected synchronized void activateLeader() throws IOException {
        System.out.println("activateLeader example: 2;\n");
        String usrCommand = stdIn.readLine();
        try {
            Command activateLeader = new Command("ActivateLeader", StringToMessage.toLeaderMessage(usrCommand));
            sendMessage(activateLeader);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void run() {
        String response;
        ResponseMessage responseMessage;
        String responseContent;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                response = in.readLine();
                responseMessage = gson.fromJson(response, ResponseMessage.class);
                responseContent = responseMessage.getResponseContent();
                switch (responseMessage.getResponseType()) {
                    case PING:
                        sendMessage(new Command("pingResponse"));
                        break;
                    case UPDATE:
                        synchronized (this){
                            ModelUpdate modelUpdate = gson.fromJson(responseContent, ModelUpdate.class);
                            Game update = modelUpdate.getGame();
                            if (this.gamemodel == null)
                                gamemodel = update;
                            else
                                gamemodel.merge(update);
                            CliView.printGameState(gamemodel, nickname);
                        }
                        break;
                    case ERROR:
                        synchronized (this){
                            ErrorMessage errorMessage = gson.fromJson(responseContent, ErrorMessage.class);
                            String error = errorMessage.getErrorMessage();
                            CliView.printError(error);
                        }
                        break;
                    case EXTRARESOURCEANDLEADERCARDBEGINNING:
                        synchronized (this){
                            ExtraResAndLeadToDiscardBeginningMessage message = gson.fromJson(responseContent, ExtraResAndLeadToDiscardBeginningMessage.class);
                            nLeadersToDiscard = message.getNumLeader();
                            resourcesToTake = message.getNumLeader();
                            CliView.printSetUpView(nLeadersToDiscard, resourcesToTake);
                        }
                        break;
                    case HASHMAPRESOURCES:
                        synchronized (this){
                            HashMapResources message = gson.fromJson(responseContent, HashMapResources.class);
                            this.resourcesMap = message.getResources();
                            CliView.printResourcesMap(resourcesMap);
                        }
                        break;
                    case INFOSTRING:
                        synchronized (this){
                            GeneralInfoStringMessage infoMessage = gson.fromJson(responseContent, GeneralInfoStringMessage.class);
                            String info = infoMessage.getMessage();
                            CliView.printinfo(info);
                        }
                        break;
                    case ASKFORNUMPLAYERS:
                        synchronized (this){
                            String info = "You are the game Creator, you must set the number of players (1-4)";
                            CliView.printinfo(info);
                        }
                        break;
                    case KICKEDOUT:
                        synchronized (this) {
                            logoutMessage = "You Have been kicked out, please restart the game to connect to a new Game";
                            forceLogout = true;
                            stdIn.close();
                        }
                        break;
                    case SETNICK:
                        synchronized (this){
                            LoginConfirmationMessage setNickMessage = gson.fromJson(responseContent, LoginConfirmationMessage.class);
                            nickname = setNickMessage.getConfirmedNickname();
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String hostName ="127.0.0.1";
        int portNumber = 9047;
        Client client = new CliClient(portNumber, hostName);
        client.startConnection();
        client.doConnection();
    }
}





    /*
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
     */