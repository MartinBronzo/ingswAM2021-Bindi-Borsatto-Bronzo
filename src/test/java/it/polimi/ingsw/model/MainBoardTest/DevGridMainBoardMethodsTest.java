package it.polimi.ingsw.model.MainBoardTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.DiscountLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DevGridMainBoardMethodsTest {
    MainBoard mainBoard;

    @BeforeEach
    public void setup() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        mainBoard = new MainBoard(4); //The number of players here is irrelevant
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevCard with position
    public void ctrlGetDevCardPosition(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getDevCardFromDeckInDevGrid(155, 155));
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevCard with level and color
    public void ctrlGetDevCardLevCol(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getDevCardFromDeckInDevGrid(155, DevCardColour.PURPLE));
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of DevGrid)
    public void ctrlDrawDevCardPosInsideException(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(155, 155));
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of main board)
    public void ctrlDrawDevCardPosOutsideException(){
        boolean tmp = false;
        while(!tmp){
            try {
                this.mainBoard.drawDevCardFromDeckInDevGrid(1,1);
            }catch(IllegalActionException e){
                tmp = true;
            }
        }
        //Now we are sure that the DevDeck at [1][1] is empty
        Exception e = assertThrows(IllegalActionException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(1, 1));
        assertEquals(e.getMessage(), "drawDevCardFromDeck: deck is empty");
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of DevGrid)
    public void ctrlDrawDevCardLevColInsideException(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(155, DevCardColour.PURPLE));
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDrawableCards
    public void ctrlGetDrawableCards(){
        //When the main board is not changed, yet, (like at the beginning of the game) there are still drawable cards
        assertFalse(mainBoard.getDrawableCardsInDevGrid().isEmpty());
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevDeckSize
    public void ctrlGetDevDeckSize(){
        //When the main board is not changed, yet, (like at the beginning of the game) there are still cards with the specified color
        assertTrue(mainBoard.getDevDeckSizeInDevGrid(DevCardColour.PURPLE) > 0);
    }

    @Test
    //Tests whether the list of effects are applied correctly to the cost of a DevCard: all the effects change the cost of the card
    public void ctrlDiscountEffectsMeaningfulEffects() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> originalCost = new HashMap<>();
        originalCost.put(ResourceType.COIN, 3);
        originalCost.put(ResourceType.SERVANT, 2);
        DevCard card = new DevCard(1, DevCardColour.PURPLE, 5, new HashMap<>(), new HashMap<>(), originalCost, "abc");

        List<Effect> effects = new ArrayList<>();
        effects.add(new DiscountLeaderEffect(ResourceType.COIN, 3));
        effects.add(new DiscountLeaderEffect(ResourceType.SERVANT, 1));

        HashMap<ResourceType, Integer> discountedCost = mainBoard.applyDiscountToDevCard(card, effects);

        assertEquals(discountedCost.get(ResourceType.SERVANT), 1);
        assertFalse(discountedCost.containsKey(ResourceType.COIN));
    }

    @Test
    //Tests whether the list of effects are applied correctly to the cost of a DevCard: the effects don't change the cost of the card
    public void ctrlDiscountEffectsMeaninglessEffects() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> originalCost = new HashMap<>();
        originalCost.put(ResourceType.COIN, 3);
        originalCost.put(ResourceType.SERVANT, 2);
        DevCard card = new DevCard(1, DevCardColour.PURPLE, 5, new HashMap<>(), new HashMap<>(), originalCost, "abc");

        List<Effect> effects = new ArrayList<>();
        effects.add(new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        effects.add(new ExtraSlotLeaderEffect(ResourceType.COIN, 2));

        HashMap<ResourceType, Integer> discountedCost = mainBoard.applyDiscountToDevCard(card, effects);

        assertNotSame(originalCost, discountedCost);
        assertEquals(discountedCost.get(ResourceType.COIN), 3);
        assertEquals(discountedCost.get(ResourceType.SERVANT), 2);
    }




}
