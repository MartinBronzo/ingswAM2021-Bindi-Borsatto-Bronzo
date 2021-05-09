package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.enums.PlayerState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonTest {
    private static Gson gson;

    @Test
    public void playerstateTest() {
        PlayerState playerState = PlayerState.WAITING4NAME;
        System.out.println(gson.toJson(playerState).toString());
    }

    @BeforeAll
    static void setUp() {
       gson=new Gson();
    }
}
