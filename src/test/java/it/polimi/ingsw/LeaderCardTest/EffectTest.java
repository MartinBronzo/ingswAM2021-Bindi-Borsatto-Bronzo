package it.polimi.ingsw.LeaderCardTest;

import it.polimi.ingsw.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.marble.WhiteMarble;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EffectTest {
    private static final ResourceType[] resources = new ResourceType[]{ResourceType.COIN, ResourceType.STONE, ResourceType.SERVANT, ResourceType.FAITHPOINT,  ResourceType.SHIELD};

    @Test
    //Tests that fake effects don't do anything special
    public void ctrlFakeEffect(){
        Effect effect = new Effect();
        HashMap<ResourceType, Integer> input = new HashMap<>();
        input.put(ResourceType.COIN, 3);
        input.put(ResourceType.SHIELD, 2);
        HashMap<ResourceType, Integer> inputClone = new HashMap<>(input);

        //Tests that the WhiteMarbleEffect doesn't modify the input map
        try {
            assertTrue(effect.whiteMarbleEffect(input));
        } catch (NegativeQuantityException e) {
            assertTrue(false);
        }
        for(ResourceType tP: resources)
            assertEquals(input.get(tP), inputClone.get(tP));

        //Tests the effects related to the extra slots return default values
        assertEquals(effect.extraSlotGetType(), null);
        assertEquals(effect.extraSlotGetResourceNumber(), 0);

        //Tests the discount effect doesn't change the given cost
        assertTrue(effect.discountEffect(input));
        for(ResourceType tP: resources)
            assertEquals(input.get(tP), inputClone.get(tP));

        //Tests the extra production returns an empty map
        for(ResourceType tP: resources) {
            assertTrue(effect.extraProductionEffect(tP).isEmpty());
        }
    }

    @Test
    //Tests the WhiteMarbleEffect works
    public void ctrlWhiteMarbleEffect(){
        WhiteMarbleLeaderEffect effect;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new WhiteMarbleLeaderEffect(ResourceType.FAITHPOINT));
        assertEquals(exception.getMessage(),"The WhiteMarble can't give extra FaithPoints!");

        HashMap<ResourceType, Integer> input = new HashMap<>();
        for(ResourceType tP: resources)
            input.put(tP, 1);
        HashMap<ResourceType, Integer> inputClone = new HashMap<>(input);


        for(int j = 0; j < resources.length; j++){
            if(!resources[j].equals(ResourceType.FAITHPOINT)) {
                effect = new WhiteMarbleLeaderEffect(resources[j]);
                try {
                    assertTrue(effect.whiteMarbleEffect(input));
                    for (int i = 0; i <= j; i++)
                        if(!resources[i].equals(ResourceType.FAITHPOINT))
                            assertEquals(input.get(resources[i]), inputClone.get(resources[i]) + 1);
                        else
                            assertEquals(input.get(resources[i]), inputClone.get(resources[i]));
                    for (int t = j + 1; t < resources.length; t++)
                        assertEquals(input.get(resources[t]), inputClone.get(resources[t]));
                    ;
                } catch (NegativeQuantityException e) {
                    e.printStackTrace();
                }
            }else
                assertThrows(IllegalArgumentException.class, () -> new WhiteMarbleLeaderEffect(ResourceType.FAITHPOINT));
        }

        WhiteMarbleLeaderEffect finalEffect = new WhiteMarbleLeaderEffect(resources[0]);
        assertThrows(NullPointerException.class, () -> finalEffect.whiteMarbleEffect(null));
    }

    @Test
    //Tests the ExtraSlotLeaderEffect works
    public void ctrlExtraSlotEffect(){
        ExtraSlotLeaderEffect effect;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExtraSlotLeaderEffect(ResourceType.FAITHPOINT, 1));
        assertEquals(exception.getMessage(), "FaithPoints can't be stored!");
        exception = assertThrows(IllegalArgumentException.class, () -> new ExtraSlotLeaderEffect(ResourceType.FAITHPOINT, 0));
        assertTrue(exception.getMessage().equals("FaithPoints can't be stored!") || exception.getMessage().equals("The number of slots must be a positive integer greater than 0!"));
        exception = assertThrows(IllegalArgumentException.class, () -> new ExtraSlotLeaderEffect(ResourceType.COIN, -1));
        assertEquals(exception.getMessage(), "The number of slots must be a positive integer greater than 0!");

        int i = 1;
        for(ResourceType tP: resources){
            if(!tP.equals(ResourceType.FAITHPOINT)) {
                effect = new ExtraSlotLeaderEffect(tP, i);
                assertEquals(effect.extraSlotGetType(), tP);
                assertEquals(effect.extraSlotGetResourceNumber(), i);
            }
            i++;
        }

    }

    @Test
    //Tests the DiscountLeaderEffect works
    public void ctrlDiscountLeaderEffect(){
        DiscountLeaderEffect effect;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new DiscountLeaderEffect(ResourceType.FAITHPOINT, 1));
        assertEquals(exception.getMessage(), "The cost of DevCards is not in FaithPoints!");
        exception = assertThrows(IllegalArgumentException.class, () -> new DiscountLeaderEffect(ResourceType.FAITHPOINT, 0));
        assertTrue(exception.getMessage().equals("The cost of DevCards is not in FaithPoints!") || exception.getMessage().equals("The amount of the discount must be a positive integer greater than 0!"));
        exception = assertThrows(IllegalArgumentException.class, () -> new DiscountLeaderEffect(ResourceType.COIN, -1));
        assertEquals(exception.getMessage(), "The amount of the discount must be a positive integer greater than 0!");

        effect = new DiscountLeaderEffect(ResourceType.COIN, 1);
        DiscountLeaderEffect finalEffect = effect;
        NullPointerException nException = assertThrows(NullPointerException.class, () -> finalEffect.discountEffect(null));
        assertEquals(nException.getMessage(), "Cost is a null pointer!");
        HashMap<ResourceType, Integer> cost = new HashMap<>();

        //There is no need to apply the cost
        assertFalse(effect.discountEffect(cost));
        cost.put(ResourceType.SERVANT, 2);
        assertFalse(effect.discountEffect(cost));

        //Discount is applied
        cost.put(ResourceType.COIN, 10);
        HashMap<ResourceType, Integer> costClone = new HashMap<>(cost);
        assertTrue(effect.discountEffect(cost)); //It discounts one coin
        assertEquals(cost.get(ResourceType.COIN), costClone.get(ResourceType.COIN) - 1);
        for(ResourceType tP: resources)
            if(!tP.equals(ResourceType.COIN))
            assertEquals(cost.get(tP), costClone.get(tP));

        costClone = new HashMap<>(cost);
        effect = new DiscountLeaderEffect(ResourceType.COIN, 20);
        assertTrue(effect.discountEffect(cost)); //It discount is greater than the original cost
        assertEquals(cost.get(ResourceType.COIN), 0);
        for(ResourceType tP: resources)
            if(!tP.equals(ResourceType.COIN))
                assertEquals(cost.get(tP), costClone.get(tP));

    }

    @Test
    //Tests the ExtraProductionLeaderEffect works
    public void ctrlExtraProductionEffect(){
        ExtraProductionLeaderEffect effect;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExtraProductionLeaderEffect(ResourceType.FAITHPOINT, 1));
        assertEquals(exception.getMessage(), "FaithPoints can't be required in order to activate the extra production!");
        exception = assertThrows(IllegalArgumentException.class, () -> new ExtraProductionLeaderEffect(ResourceType.FAITHPOINT, 0));
        assertTrue(exception.getMessage().equals("FaithPoints can't be required in order to activate the extra production!") || exception.getMessage().equals("The required amount of resources can't be a number less than or equal to 0!"));
        exception = assertThrows(IllegalArgumentException.class, () -> new ExtraProductionLeaderEffect(ResourceType.COIN, -1));
        assertEquals(exception.getMessage(), "The required amount of resources can't be a number less than or equal to 0!");

        effect = new ExtraProductionLeaderEffect(ResourceType.COIN, 4);
        HashMap<ResourceType, Integer> input = effect.getRequiredInput();
        for(ResourceType tP: resources)
            if(!tP.equals(ResourceType.COIN))
                assertEquals(input.get(tP), null);
            else
                assertEquals(input.get(tP), 4);

        exception = assertThrows(IllegalArgumentException.class, () -> effect.extraProductionEffect(ResourceType.FAITHPOINT));
        assertEquals(exception.getMessage(), "This effect can't produce a a FaithPoint!");

        //By default, it produces one resource of the desired type and one extra FaitPoint
        HashMap<ResourceType, Integer> output = effect.extraProductionEffect(ResourceType.SERVANT);
        assertFalse(output.isEmpty());
        assertEquals(output.get(ResourceType.SERVANT), 1);
        assertEquals(output.get(ResourceType.FAITHPOINT), 1);
        for(ResourceType tP: resources)
            if(!tP.equals(ResourceType.SERVANT) && !tP.equals(ResourceType.FAITHPOINT))
                assertEquals(output.get(tP), null);
    }
}
