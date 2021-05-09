package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import java.rmi.UnexpectedException;
import java.util.*;

public final class GamesManagerSingleton
{

    private final Collection<GameController> games;
    private GameController startingGame;
    private ClientHandler clientConfigurator;
    private static GamesManagerSingleton instance;
    private Timer timeOutTimer;
    private Boolean timerCanCancel;

    private GamesManagerSingleton() {
        this.games = new LinkedList<>();
        this.startingGame = null;
    }

    public static GamesManagerSingleton getInstance(){
        if (instance == null){
            synchronized (GamesManagerSingleton.class) {
                if (instance == null)
                    instance = new GamesManagerSingleton();
            }
        }
        return instance;
    }

    public synchronized GameController joinOrCreateNewGame(ClientHandler client) throws InterruptedException, UnexpectedException {
        while (startingGame!=null && startingGame.getState().equals(GameState.CONFIGURING))
            wait();
        if (startingGame == null){
            startingGame = new GameController();
            if (!startingGame.setPlayer(client)) throw new UnexpectedException("bug synchronizing GameManager happened");
            this.clientConfigurator=client;
            startingGame.setState(GameState.CONFIGURING);
            startTimer();
            return startingGame;
        }

        GameController returnedGame = startingGame;
        startingGame.setPlayer(client);
        /*IDK if I should add method start game or game is automatically started when reaches the correct number of players*/
        if(startingGame.getNumberOfPlayers() == startingGame.getPlayersList().size()) {
            games.add(startingGame);
            startingGame = null;
        }
        return returnedGame;

    }

    private void startTimer() {
        this.timeOutTimer = new Timer();
        timeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerCanCancel=true;
                if (!configurationTimeElapsed())
                    System.out.println("Games Manager Timer has run but but not cancelled the game");
            }
        }, 30000);
    }

    private void stopTimer(){
        this.timeOutTimer.cancel();
        this.timeOutTimer.purge();
        this.timerCanCancel = false;
        this.timeOutTimer=null;
    }

    private synchronized boolean configurationTimeElapsed(){
        if (startingGame!=null && startingGame.getState().equals(GameState.CONFIGURING) && timerCanCancel){
            startingGame=null;
            //notifyClient
            clientConfigurator = null;
            notify();
            return true;
        }
        return false;
    }

    public synchronized boolean configureGame(ClientHandler client, int numberOfPlayers) throws IllegalStateException, UnexpectedException, IllegalArgumentException {
        if (!this.clientConfigurator.equals(client)) throw new IllegalStateException("client is not the same client manager");
        if (this.startingGame == null || !this.startingGame.getState().equals(GameState.CONFIGURING)) throw new UnexpectedException("gamesManager: client IsGameConfigurator but game is null or not in configuring state");
        if (numberOfPlayers<1 || numberOfPlayers>4) throw new IllegalArgumentException("configureGame: Not Valid number of players");

        stopTimer();
        this.startingGame.startMainBoard(numberOfPlayers);
        this.startingGame.setState(GameState.WAITING4PLAYERS);
        notifyAll();
        /*IDK if I should add method start game or game is automatically started when reaches the correct number of players*/
        // case client wants play alone
        if(startingGame.getNumberOfPlayers() == startingGame.getPlayersList().size()) {
            games.add(startingGame);
            startingGame = null;
        }
        return true;
}

    private GameController searchPlayerInGames(String nickname) throws NoSuchElementException, NotAvailableNicknameException {
        if (startingGame != null) {
            if (startingGame.getPlayersList().stream().anyMatch(client -> client.getNickname().equals(nickname) && !client.getState().equals(PlayerState.DISCONNECTED))) {
                throw new NotAvailableNicknameException("Nick is taken");
            }
        }
        GameController gameWithThatNick = games.stream().filter(game -> game.getPlayersList().stream().map(ClientHandler::getNickname).anyMatch(clientUsername -> clientUsername.equals(nickname))).findAny().orElseThrow(NoSuchElementException::new);
        if (gameWithThatNick.getPlayersList().stream().anyMatch(client -> client.getNickname().equals(nickname) && !client.getState().equals(PlayerState.DISCONNECTED))) {
            throw new NotAvailableNicknameException("Nick is taken");
        }
        return gameWithThatNick;

    }

    public boolean deleteTerminatedGame(GameController gameEnded) throws NullPointerException, IllegalStateException {
        if (!gameEnded.getState().equals(GameState.ENDED)) throw new IllegalStateException("Game is not in ended state");
        return games.remove(gameEnded);
    }


}