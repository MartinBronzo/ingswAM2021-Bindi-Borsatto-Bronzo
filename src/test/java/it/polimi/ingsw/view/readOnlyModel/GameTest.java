package it.polimi.ingsw.view.readOnlyModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    Game game;

    @Test
    void merge() {
        game.setLorenzosPosition(2);
        Game update = new Game(2);
        update.setLorenzosPosition(3);
        update.setMainBoard(game.getMainBoard());
        update.setPlayers(null);
        Collection<Player> players = game.getPlayers();
        game.merge(update);
        assertEquals(3, game.getLorenzosPosition());
        assertEquals(update.getMainBoard(), game.getMainBoard());
        assertEquals(players, game.getPlayers());

    }

    @BeforeEach
    void setUp() {
        game = new Game(2);
    }
}