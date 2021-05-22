package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class stringToMessageTest {

    @Test
    void toMatrixMessageLineTest() {
        String string = "row 3; 1, 2, 4;";
        GetFromMatrixMessage message = StringToMessage.toMatrixMessageLine(string);
        assertEquals(0, message.getCol());
        assertEquals(3, message.getRow());
        List<Integer> integers = message.getLeaderList();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
    }

    @Test
    void toMatrixMessageCellTest() {
        String string = "2; 3; 1, 2, 4;";
        GetFromMatrixMessage message = StringToMessage.toMatrixMessageCell(string);
        assertEquals(3, message.getCol());
        assertEquals(2, message.getRow());
        List<Integer> integers = message.getLeaderList();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
    }

    @Test
    void toMatrixMessageCellTest2() {
        String string = "2; 3; ;";
        GetFromMatrixMessage message = StringToMessage.toMatrixMessageCell(string);
        assertEquals(3, message.getCol());
        assertEquals(2, message.getRow());
        assertTrue(message.getLeaderList().isEmpty());
    }

    @Test
    void toBuyDevCardMessage() {
        String string = "2; 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2; 4;";
        BuyDevCardMessage message = StringToMessage.toBuyDevCardMessage(string);
        assertEquals(3, message.getCol());
        assertEquals(2, message.getRow());
        List<Integer> integers = message.getLeaders();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        List<DepotParams> depotParamsList = message.getDepotRes();
        assertEquals(ResourceType.STONE, depotParamsList.get(1).getResourceType());
        assertEquals(2, depotParamsList.get(1).getQt());
        assertEquals(2, depotParamsList.get(1).getShelf());
        assertEquals(4, message.getDevSlot());
        HashMap leaderMap = message.getLeaderRes();
        assertEquals(2, leaderMap.get(ResourceType.COIN));
        assertEquals(1, leaderMap.get(ResourceType.SERVANT));
        HashMap stronboxMap = message.getStrongboxRes();
        assertEquals(2, stronboxMap.get(ResourceType.STONE));
        assertEquals(2, stronboxMap.get(ResourceType.SERVANT));
    }

    @Test
    void toBuyFromMarketMessage() {
        String string = "row 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2;";
        BuyFromMarketMessage message = StringToMessage.toBuyFromMarketMessage(string);
        assertEquals(0, message.getCol());
        assertEquals(3, message.getRow());
        List<Integer> integers = message.getLeaderList();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        List<DepotParams> depotParamsList = message.getDepotRes();
        assertEquals(ResourceType.STONE, depotParamsList.get(1).getResourceType());
        assertEquals(2, depotParamsList.get(1).getQt());
        assertEquals(2, depotParamsList.get(1).getShelf());
        Map leaderMap = message.getLeaderRes();
        assertEquals(2, leaderMap.get(ResourceType.COIN));
        assertEquals(1, leaderMap.get(ResourceType.SERVANT));
        Map stronboxMap = message.getDiscardRes();
        assertEquals(2, stronboxMap.get(ResourceType.STONE));
        assertEquals(2, stronboxMap.get(ResourceType.SERVANT));
    }

    @Test
    void toBuyFromMarket2Message() {
        String string = "row 3; 1, 2, 4; COIN 2 2, STONE 2 2; ; ;";
        BuyFromMarketMessage message = StringToMessage.toBuyFromMarketMessage(string);
        assertEquals(0, message.getCol());
        assertEquals(3, message.getRow());
        List<Integer> integers = message.getLeaderList();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        List<DepotParams> depotParamsList = message.getDepotRes();
        assertEquals(ResourceType.STONE, depotParamsList.get(1).getResourceType());
        assertEquals(2, depotParamsList.get(1).getQt());
        assertEquals(2, depotParamsList.get(1).getShelf());
        Map leaderMap = message.getLeaderRes();
        assertTrue(leaderMap.isEmpty());
        Map discardMap = message.getDiscardRes();
        assertTrue(discardMap.isEmpty());
    }

    @Test
    void toActivateProductionMessage() {
        String string = "1, 2, 4; 2 COIN, 1 SERVANT; TRUE, COIN SERVANT, STONE; COIN 2 2, STONE 2 2;  COIN 2, SERVANT 1; STONE 2, SERVANT 2;";
        ActivateProductionMessage message = StringToMessage.toActivateProductionMessage(string);
        List<Integer> integers = message.getDevCards();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        HashMap<Integer,ResourceType> leaderIdsMap = message.getLeaders();
        assertEquals(ResourceType.COIN, leaderIdsMap.get(2));
        assertEquals(ResourceType.SERVANT, leaderIdsMap.get(1));
        assertTrue(message.getBaseProduction().isActivated());
        assertEquals(ResourceType.COIN, message.getBaseProduction().getBaseInput().get(0));
        assertEquals(ResourceType.SERVANT, message.getBaseProduction().getBaseInput().get(1));
        assertEquals(ResourceType.STONE, message.getBaseProduction().getBaseOutput().get(0));
        List<DepotParams> depotParamsList = message.getDepotInputRes();
        assertEquals(ResourceType.STONE, depotParamsList.get(1).getResourceType());
        assertEquals(2, depotParamsList.get(1).getQt());
        assertEquals(2, depotParamsList.get(1).getShelf());
        HashMap leaderSlotRes = message.getLeaderSlotRes();
        assertEquals(2, leaderSlotRes.get(ResourceType.COIN));
        assertEquals(1, leaderSlotRes.get(ResourceType.SERVANT));
        HashMap stronboxMap = message.getStrongboxInputRes();
        assertEquals(2, stronboxMap.get(ResourceType.STONE));
        assertEquals(2, stronboxMap.get(ResourceType.SERVANT));
    }

    @Test
    void toDepotParamTest() {
        String string = "COIN 2 3";
        DepotParams message = StringToMessage.toDepotParams(string);
        assertEquals(2, message.getQt());
        assertEquals(3, message.getShelf());
        assertEquals(ResourceType.COIN, message.getResourceType());
    }

    @Test
    void toHashMapTest() {
        String string = "COIN 2, SERVANT 1";
        HashMap hashMap = StringToMessage.toResourceHashMap(string);
        assertEquals(2, hashMap.get(ResourceType.COIN));
        assertEquals(1, hashMap.get(ResourceType.SERVANT));
    }

    @Test
    void toBaseProductionParamsTest() {
        String string = "TRUE, COIN SERVANT, STONE";
        BaseProductionParams message = StringToMessage.toBaseProductionParams(string);
        assertTrue(message.isActivated());
        assertEquals(ResourceType.COIN, message.getBaseInput().get(0));
        assertEquals(ResourceType.SERVANT, message.getBaseInput().get(1));
        assertEquals(ResourceType.STONE, message.getBaseOutput().get(0));
    }

    @Test
    void toDiscardLeaderAndExtraResBeginningMessageTest() {
        String string = "1, 2, 4; COIN 2 3, STONE 1 4;";
        DiscardLeaderAndExtraResBeginningMessage message = StringToMessage.toDiscardLeaderAndExtraResBeginningMessage(string);
        List<Integer> integers = message.getLeaderCard();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        List<DepotParams> depotParamsList = message.getDepotRes();
        assertEquals(ResourceType.COIN, depotParamsList.get(0).getResourceType());
        assertEquals(2, depotParamsList.get(0).getQt());
        assertEquals(3, depotParamsList.get(0).getShelf());
        assertEquals(ResourceType.STONE, depotParamsList.get(1).getResourceType());
        assertEquals(1, depotParamsList.get(1).getQt());
        assertEquals(4, depotParamsList.get(1).getShelf());
    }

    @Test
    void toDiscardLeaderAndExtraResBeginningMessageTestEmptyList() {
        String string = " ; ;";
        DiscardLeaderAndExtraResBeginningMessage message = StringToMessage.toDiscardLeaderAndExtraResBeginningMessage(string);
        assertTrue(message.getLeaderCard().isEmpty());
        assertTrue(message.getDepotRes().isEmpty());
    }

    @Test
    void toGetProductionCostMessageTest() {
        String string = "1, 2, 4; 1, 2, 4; TRUE, COIN SERVANT, STONE;";
        GetProductionCostMessage message = StringToMessage.toGetProductionCostMessage(string);
        List<Integer> integers = message.getDevCards();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        integers = message.getLeaders();
        assertEquals(1, integers.get(0));
        assertEquals(2, integers.get(1));
        assertEquals(4, integers.get(2));
        BaseProductionParams baseProductionParams = message.getBaseProd();
        assertTrue(baseProductionParams.isActivated());
        assertEquals(ResourceType.COIN, baseProductionParams.getBaseInput().get(0));
        assertEquals(ResourceType.SERVANT, baseProductionParams.getBaseInput().get(1));
        assertEquals(ResourceType.STONE, baseProductionParams.getBaseOutput().get(0));

    }
}