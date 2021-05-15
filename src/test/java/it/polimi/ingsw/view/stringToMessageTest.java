package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class stringToMessageTest {

    @Test
    void toMatrixMessageTest() {
        String string = "row 3; 1 2 4;";
        GetFromMatrixMessage message = stringToMessage.toMatrixMessage(string);
        assertEquals(0, message.getCol());
        assertEquals(3, message.getRow());
        List<Integer> integers = message.getLeaderList();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
    }
}