package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.view.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class cliClient extends Client implements Runnable {
    private final int portNumber;
    private final String hostName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private static Gson gson = new Gson();


    public cliClient(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
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
        this.printWelcome();
        this.manageGameStarting();
        new Thread(this).start();
    }

    @Override
    public void doConnection() {
        CliCommandType cliCommandType = null;
        do {
            try {
                cliCommandType = CliCommandType.valueOf(stdIn.readLine());
                switch (cliCommandType) {
                    case BUYFROMMARKET:
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
            }
        }while (cliCommandType.equals(CliCommandType.QUIT));
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*thread interrupt stuff*/

    }


    @Override
    public void sendMessage(Command command) throws NullPointerException {
        if (out == null) throw new NullPointerException("PrintWriter is null");
        out.println(command);
    }

    @Override
    public void manageLogin() {
        boolean isNickValid = false;
        while (!isNickValid){
            System.out.println("What's your nickname?\n");
            try {
                Command loginCommand = new Command("login", new LoginMessage(stdIn.readLine()));
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
    protected void printWelcome() {

    }

    @Override
    protected void manageGameStarting() {

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


    /*public static void main(String[] args) throws IOException {
        String hostName ="127.0.0.1";
        int portNumber = 1234;
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }*/
}
