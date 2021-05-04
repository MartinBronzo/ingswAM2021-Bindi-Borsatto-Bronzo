package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.PlayerState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private String nickname;
    private PlayerState state;
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private GameController game;




    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.state = PlayerState.WAITING4NAME;
    }

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.state = PlayerState.WAITING4NAME;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerState getState() {
        return state;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }
}
