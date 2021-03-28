package it.polimi.ingsw;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTypeTest {
    @Test
    public void toStringTestTrue()
    {
        ResourceType newFaithPoint= ResourceType.FAITHPOINT;
        assertEquals("FAITHPOINT", newFaithPoint.toString());
    }

    @Test
    public void isCoinTestTrue()
    {
        ResourceType newResource= ResourceType.COIN;
        assertTrue(newResource.isCoin());
    }

    @Test
    public void isCoinTestFalse()
    {
        ResourceType newResource= ResourceType.FAITHPOINT;
        assertFalse(newResource.isCoin());
    }

    @Test
    public void isServantTestTrue()
    {
        ResourceType newResource= ResourceType.SERVANT;
        assertTrue(newResource.isServant());
    }

    @Test
    public void isServantTestFalse()
    {
        ResourceType newResource= ResourceType.FAITHPOINT;
        assertFalse(newResource.isServant());
    }

    @Test
    public void isShieldTestTrue()
    {
        ResourceType newResource= ResourceType.SHIELD;
        assertTrue(newResource.isShield());
    }

    @Test
    public void isShieldTestFalse()
    {
        ResourceType newResource= ResourceType.FAITHPOINT;
        assertFalse(newResource.isShield());
    }

    @Test
    public void isStoneTestTrue()
    {
        ResourceType newResource= ResourceType.STONE;
        assertTrue(newResource.isStone());
    }

    @Test
    public void isStoneTestFalse()
    {
        ResourceType newResource= ResourceType.FAITHPOINT;
        assertFalse(newResource.isStone());
    }

    @Test
    public void isFaithPointTestTrue()
    {
        ResourceType newResource= ResourceType.FAITHPOINT;
        assertTrue(newResource.isFaithPoint());
    }

    @Test
    public void isFaithPointTestFalse()
    {
        ResourceType newResource= ResourceType.COIN;
        assertFalse(newResource.isFaithPoint());
    }
}
