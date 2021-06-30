package it.polimi.ingsw.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final private int port;

    /**
     * @param port the server socket port
     */
    public Server(int port) {
        this.port = port;
    }


    /**
     * start the server in listening for any new socket connection
     * each connection is managed in a new thread
     */
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }

        System.out.println("Server ready");
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                System.out.println("A new client is connected : " + socket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            try {
                System.out.println("Adding Client to thread Pool");
                executor.submit(new ClientHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
    }

    /**
     * Starts the server
     * @param args parameters that can be used to start the server
     */
    public static void main(String[] args) {
        Server server = new Server(9047);
        server.startServer();
    }


}