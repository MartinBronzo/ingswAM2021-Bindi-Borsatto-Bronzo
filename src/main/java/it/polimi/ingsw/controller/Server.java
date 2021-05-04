package it.polimi.ingsw.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    final private int port;
    private Collection<GameController> games;
    private GameController startingGame;

    public Server(int port) {
        this.port = port;
        this.games = new ArrayList<>();
        startingGame = new GameController();
    }


    public void startServer(){
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return; }

        System.out.println("Server ready");
        while (true)
        {
            Socket socket = null;
            ClientHandler client;
            try {
                socket = serverSocket.accept();
                System.out.println("A new client is connected : " + socket);
                client = new ClientHandler(socket);
                System.out.println("A new client is connected : " + socket);

                System.out.println("Adding Client to thread Pool");
            } catch (Exception e){
                    break;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        Server server = new Server(9047);
        server.startServer();
    }


}