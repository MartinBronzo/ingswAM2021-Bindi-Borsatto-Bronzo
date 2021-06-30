package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.network.messages.sendToClient.*;

import java.rmi.UnexpectedException;
import java.util.*;

/**
 * manages games in the server, avoids more players having same nick and manages player reconnection
 */
public final class GamesManagerSingleton {

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

    /** creates a new instance if there is none
     * @return the instance of the GamesManagerSingleton
     */
    public static GamesManagerSingleton getInstance() {
        if (instance == null) {
            synchronized (GamesManagerSingleton.class) {
                if (instance == null)
                    instance = new GamesManagerSingleton();
            }
        }
        return instance;
    }


    /**
     * used to add clientHandler to a game.
     * if a new game is created it must be configured,
     * if a game is in waiting for players the client is add to the starting game
     * if client has a occupied nickname of another disconnected client
     * the client can know if he is the client Configurator if the methods returns null
     * if the game is not configured a timer is started, the game must be configured before the timer ends using configure method
     *
     * @param client is the clientHandler added to the game, it can be also the clientHandlerConfigurator if the game must be configured
     * @return the Game where the client will play or null if the game is just created and the client must configure it
     * @throws InterruptedException          if a thread is interrupted while it is in waiting
     * @throws NotAvailableNicknameException if the nickname of client is not available
     */
    public synchronized GameController joinOrCreateNewGame(ClientHandler client) throws InterruptedException, NotAvailableNicknameException, IllegalActionException {
        if (client == null) throw new NullPointerException("client is null");
        try {
            GameController gameWithThatNickname = searchPlayerInGames(client.getNickname());
            client.send(new LoginConfirmationMessage(client.getNickname()));
            gameWithThatNickname.substitutesClient(client);
            return gameWithThatNickname;
        } catch (NoSuchElementException e) {
            while (startingGame != null && startingGame.getState().equals(GameState.CONFIGURING)) {
                client.send(new GeneralInfoStringMessage("Another player is creating a game, please wait"));
                wait();
            }

            client.send(new LoginConfirmationMessage(client.getNickname()));

            if (startingGame == null) {
                startingGame = new GameController();
                this.clientConfigurator = client;
                startingGame.setState(GameState.CONFIGURING);
                client.setPlayerState(PlayerState.WAITING4SETNUMPLAYER);
                startTimer();
                client.send(new AskForNumPlayersMessage("You are creating a game! Tell me how many players you want in this game!"));
                return null;
            }

            GameController returnedGame = startingGame;
            startingGame.setPlayer(client);
            /*IDK if I should add method start game or game is automatically started when reaches the correct number of players*/
            if (startingGame.getNumberOfPlayers() == startingGame.getPlayersList().size()) {
                games.add(startingGame);
                startingGame = null;
                clientConfigurator = null;
            }
            return returnedGame;

        }
    }

    private void startTimer() {
        this.timeOutTimer = new Timer();
        timeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerCanCancel = true;
                if (!configurationTimeElapsed())
                    System.out.println("Games Manager Timer has run but but not cancelled the game. The connection will be closed soon");
            }
        }, 30000);
    }

    private void stopTimer() {
        this.timeOutTimer.cancel();
        this.timerCanCancel = false;
        this.timeOutTimer = null;
    }

    private synchronized boolean configurationTimeElapsed() {
        if (startingGame != null && startingGame.getState().equals(GameState.CONFIGURING) && timerCanCancel) {
            startingGame = null;
            clientConfigurator.send(new ResponseMessage(ResponseType.KICKEDOUT, "Configuration Time Elapsed\n"));
            clientConfigurator = null;
            stopTimer();
            notify();
            return true;
        }
        return false;
    }

    /**
     * it is used to configure the game
     *
     * @param client          must be the same client configurator
     * @param numberOfPlayers the desired number of players in the game, must be between 1 (solitary game) and 4 (max number of Players)
     * @return the game where the client will play
     * @throws IllegalStateException    if client is not the same client configurator
     * @throws UnexpectedException      if an unknown error synchronizing GameManager is detected
     * @throws IllegalArgumentException if numberOfPlayers is not between 1 and 4
     */
    public synchronized GameController configureGame(ClientHandler client, int numberOfPlayers) throws IllegalStateException, UnexpectedException, IllegalArgumentException, IllegalActionException {
        if (client == null) throw new NullPointerException("client is null");
        if (!client.equals(this.clientConfigurator))
            throw new IllegalStateException("client is not the same client manager or configuration time has elapsed");
        if (this.startingGame == null || !this.startingGame.getState().equals(GameState.CONFIGURING))
            throw new UnexpectedException("gamesManager: client IsGameConfigurator but game is null or not in configuring state");
        if (numberOfPlayers < 1 || numberOfPlayers > 4)
            throw new IllegalArgumentException("configureGame: Not Valid number of players");

        stopTimer();
        this.startingGame.startMainBoard(numberOfPlayers);
        this.startingGame.setState(GameState.WAITING4PLAYERS);
        client.send(new SetNumPlayersConfirmationMessage(startingGame.getNumberOfPlayers()));
        this.startingGame.setPlayer(client);
        notifyAll();
        if (startingGame.getNumberOfPlayers() == startingGame.getPlayersList().size()) {
            GameController returnedGame = startingGame;
            games.add(startingGame);
            startingGame = null;
            clientConfigurator = null;
            return returnedGame;
        }
        return startingGame;
    }

    private GameController searchPlayerInGames(String nickname) throws NoSuchElementException, NotAvailableNicknameException {
        if (nickname == null) throw new IllegalArgumentException("nickname can't be null");
        GameController gameWithThatNick;
        if (startingGame != null) {
            if (startingGame.getPlayersList().stream().anyMatch(client -> client.getNickname().equals(nickname) && !client.getPlayerState().equals(PlayerState.DISCONNECTED))) {
                throw new NotAvailableNicknameException("Nick is taken");
            } else if (startingGame.getPlayersList().stream().anyMatch(client -> client.getNickname().equals(nickname) && client.getPlayerState().equals(PlayerState.DISCONNECTED))) {
                return startingGame;
            }
        }
        if (clientConfigurator != null) {
            if (clientConfigurator.getNickname().equals(nickname))
                throw new NotAvailableNicknameException("Nick is the same of the actual configurator");
        }
        gameWithThatNick = games.stream().filter(game -> game.getPlayersList().stream().map(ClientHandler::getNickname).anyMatch(clientUsername -> clientUsername.equals(nickname))).findAny().orElseThrow(NoSuchElementException::new);
        if (gameWithThatNick.getPlayersList().stream().anyMatch(client -> client.getNickname().equals(nickname) && !client.getPlayerState().equals(PlayerState.DISCONNECTED))) {
            throw new NotAvailableNicknameException("Nick is taken");
        }
        return gameWithThatNick;

    }

    /**
     * deletes an ended game from the list of the games, allowing clientHandlers to join games with previously occupied nicknames
     *
     * @param gameEnded the game ended
     * @return true if the action is performed correctly
     * @throws NullPointerException  if gameEnded is null
     * @throws IllegalStateException if the game is in configuring state, no need to delete the game, just wait for the timer
     */
    public synchronized boolean deleteGame(GameController gameEnded) throws NullPointerException, IllegalStateException {
        if (gameEnded == null) throw new NullPointerException();
        if (gameEnded.equals(startingGame)) {
            if (startingGame.getState().equals(GameState.CONFIGURING))
                throw new IllegalStateException("Configuration timer isn't yet elapsed");
            else {
                startingGame = null;
                notifyAll();
                return true;
            }
        }
        return games.remove(gameEnded);
    }

    /**
     * used only for test purpose
     */
    @Deprecated
    public void resetSingleton() {
        this.games.removeAll(games);
        this.startingGame = null;
        this.clientConfigurator = null;
    }


    /**
     * used only for test purpose
     */
    @Deprecated
    public Collection<GameController> getGames() {
        return games;
    }


    /**
     * used only for test purpose
     */
    @Deprecated
    public GameController getStartingGame() {
        return startingGame;
    }

    /**
     * used only for test purpose
     */
    @Deprecated
    public ClientHandler getClientConfigurator() {
        return clientConfigurator;
    }

}