package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.enums.PlayerState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonTest {
    private static Gson gson;

    @BeforeAll
    static void setUp() {
        gson = new Gson();
    }

    @Test
    public void playerStateTest() {
        PlayerState playerState = PlayerState.WAITING4NAME;
        System.out.println(gson.toJson(playerState).toString());
    }

    @Test
    public void ctrlCommandTesting() {
        String x = "boo:and:foo:";
        System.out.println(x.split("o"));
    }
}
