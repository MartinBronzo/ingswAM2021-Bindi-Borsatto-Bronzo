package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.leaderEffects.ExtraSlotLeaderEffect;
import org.junit.jupiter.api.Test;

class DepotTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faithPoint = ResourceType.FAITHPOINT;

    @Test
    public void ctrlInit() throws IllegalParameterException {
        Depot depot = new Depot();
        assertEquals(depot.getResource(coin),0);
        assertEquals(depot.getResource(stone),0);
        assertEquals(depot.getResource(shield),0);
        assertEquals(depot.getResource(servant),0);
    }

    @Test
    public void add1() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        assertEquals(depot.getResource(coin),2);
        assertEquals(depot.getShelfType(3), coin);
    }

    @Test
    public void add2() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3, 3);
        depot.addToShelf(stone, 1, 1);
        depot.addToShelf(shield, 2, 2);


        assertEquals(depot.getResource(stone),1);
        assertEquals(depot.getResource(shield),2);
        assertEquals(depot.getResource(servant),3);
        assertEquals(depot.getShelfType(1), stone);
        assertEquals(depot.getShelfType(2), shield);
        assertEquals(depot.getShelfType(3), servant);
    }

    @Test
    public void addPartialShelves() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(stone, 1, 2);

        assertEquals(depot.getResource(coin),2);
        assertEquals(depot.getResource(stone),1);

        assertEquals(depot.getShelfType(3), coin);
        assertEquals(depot.getShelfType(2), stone);
    }

    @Test
    public void addFaithPoint(){
        Depot depot = new Depot();
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(faithPoint, 1, 1));
    }

    @Test
    public void addNegativeQuantity(){
        Depot depot = new Depot();
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(coin, -1, 1));
    }

    @Test
    public void shelfAddOutOfBound(){
        Depot depot = new Depot();
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(stone, 1, 4));
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(stone, 1, 0));
    }

    @Test
    public void addAlreadyInDepot() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 1, 1);
        assertThrows(IllegalActionException.class, () -> depot.addToShelf(shield, 2, 2));

        assertEquals(depot.getResource(shield),1);
        assertEquals(depot.getShelfType(1), shield);
    }

    @Test
    public void addZero() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        assertTrue(depot.addToShelf(servant, 0, 1));
        assertNull(depot.getShelfType(1));
        assertEquals(depot.getResource(servant), 0);
    }

    @Test
    public void notEnoughSpace1Add() {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(coin, 6, 3));
        assertEquals(exception.getAvailableSpace(), 3);
    }

    @Test
    public void notEnoughSpace2Add() {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(coin, 2, 1));
        assertEquals(exception.getAvailableSpace(), 1);
    }

    @Test
    public void notEnoughSpace3Add() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        depot.addToShelf(shield, 2, 3);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(shield, 2, 3));
        assertEquals(exception.getAvailableSpace(), 1);
    }

    @Test
    public void removeRes1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 2, 2);
        assertTrue(depot.removeFromDepot(2,1));
        assertEquals(depot.getResource(servant), 1);
        assertEquals(depot.getShelfType(2), servant);
    }

    @Test
    public void removeRes2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(stone, 2, 3);
        depot.removeFromDepot(3,1);
        assertEquals(depot.getResource(stone), 1);
        assertEquals(depot.getShelfType(3), stone);
    }

    @Test
    public void removeRes3() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(stone, 1, 1);
        depot.addToShelf(coin, 2, 2);
        depot.addToShelf(shield, 3, 3);

        assertEquals(depot.getShelfType(1), stone);
        assertEquals(depot.getShelfType(2), coin);
        assertEquals(depot.getShelfType(3), shield);

        depot.removeFromDepot(1,1);
        depot.removeFromDepot(2, 1);
        depot.removeFromDepot(3,2);

        assertEquals(depot.getResource(stone), 0);
        assertEquals(depot.getResource(coin), 1);
        assertEquals(depot.getResource(shield), 1);

        assertNull(depot.getShelfType(1));
        assertEquals(depot.getShelfType(2), coin);
        assertEquals(depot.getShelfType(3), shield);
    }

    @Test
    public void multipleRemoves() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 3, 3);

        depot.removeFromDepot(3,1);
        assertEquals(depot.getResource(coin), 2);
        assertEquals(depot.getShelfType(3), coin);

        depot.removeFromDepot(3, 1);
        assertEquals(depot.getResource(coin), 1);
        assertEquals(depot.getShelfType(3), coin);

        depot.removeFromDepot(3, 1);
        assertEquals(depot.getResource(coin), 0);
        assertNull(depot.getShelfType(3));
    }

    @Test
    public void shelfRemoveOutOfBound1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalParameterException.class, () -> depot.removeFromDepot(0, 3));
    }

    @Test
    public void shelfRemoveOutOfBound2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalParameterException.class, () -> depot.removeFromDepot(4, 3));
    }

    @Test
    public void removeNegativeQuantity() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalParameterException.class, () -> depot.removeFromDepot(3, -1));
    }

    @Test
    public void removeNotInDepot() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalParameterException.class, () -> depot.removeFromDepot(1, 3));
    }

    @Test
    public void removeZeroQuantity() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertTrue(depot.removeFromDepot(3, 0));
        assertEquals(depot.getResource(servant), 3);
        assertEquals(depot.getShelfType(3), servant);
    }

    @Test
    public void notEnouthResourcesRemove() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 2 , 3);
        assertThrows(IllegalActionException.class, () -> depot.removeFromDepot(3, 3));
    }

    @Test
    public void move1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 2);
        depot.addToShelf(stone, 2, 3);

        assertEquals(depot.getShelfType(2), coin);
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(2, 3));
        assertEquals(depot.getShelfType(2), stone);
        assertEquals(depot.getShelfType(3), coin);

        assertEquals(depot.getResource(coin), 2);
        assertEquals(depot.getResource(stone), 2);

    }

    @Test
    public void move2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 1, 1);
        depot.addToShelf(stone, 1, 2);

        assertEquals(depot.getShelfType(1), servant);
        assertEquals(depot.getShelfType(2), stone);

        assertTrue(depot.moveBetweenShelves(1, 2));

        assertEquals(depot.getShelfType(1), stone);
        assertEquals(depot.getShelfType(2), servant);
    }

    @Test
    public void move3() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 1, 1);
        depot.addToShelf(stone, 1, 3);

        assertEquals(depot.getShelfType(1), servant);
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(1, 3));

        assertEquals(depot.getShelfType(3), servant);
        assertEquals(depot.getShelfType(1), stone);
    }

    @Test
    public void move4() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 1, 1);
        depot.addToShelf(stone, 1, 3);

        assertEquals(depot.getShelfType(1), servant);
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(3, 1));

        assertEquals(depot.getShelfType(3), servant);
        assertEquals(depot.getShelfType(1), stone);
    }

    @Test
    public void move5() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(stone, 1, 3);

        assertNull(depot.getShelfType(1));
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(3, 1));

        assertNull(depot.getShelfType(3));
        assertEquals(depot.getShelfType(1), stone);
    }


    @Test
    public void sourceShelfOutOfBound1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalParameterException.class, () -> depot.moveBetweenShelves(0, 2));
    }

    @Test
    public void sourceShelfOutOfBound2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalParameterException.class, () -> depot.moveBetweenShelves(4, 2));
    }

    @Test
    public void destShelfOutOfBound1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalParameterException.class, () -> depot.moveBetweenShelves(1, 0));
    }

    @Test
    public void destShelfOutOfBound2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalParameterException.class, () -> depot.moveBetweenShelves(1, 4));
    }

    @Test
    public void noResourceToMove() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(1, 2));
    }

    @Test
    public void notEnoughSpaceMove1() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();

        depot.addToShelf(stone, 1, 1);
        depot.addToShelf(shield, 3, 3);

        exception = assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(3, 1));
        assertEquals(exception.getMessage(), "Not enough space in destination shelf");
    }

    @Test
    public void notEnoughSpaceMove2() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();

        depot.addToShelf(servant, 2, 2);
        depot.addToShelf(shield, 1, 1);

        exception = assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(1, 2));
        assertEquals(exception.getMessage(), "Not enough space in source shelf");
    }

    @Test
    public void init() throws IllegalParameterException {
        Depot depot = new Depot();
        assertEquals(depot.getExtraDepotValue(coin), 0);
        assertEquals(depot.getExtraDepotValue(shield), 0);
        assertEquals(depot.getExtraDepotValue(servant), 0);
        assertEquals(depot.getExtraDepotValue(stone), 0);

        assertEquals(depot.getExtraDepotLimit(coin), 0);
        assertEquals(depot.getExtraDepotLimit(shield), 0);
        assertEquals(depot.getExtraDepotLimit(servant), 0);
        assertEquals(depot.getExtraDepotLimit(stone), 0);
    }

    @Test
    public void addExtraSlot() throws IllegalParameterException, IllegalActionException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        assertTrue(depot.addExtraSolt(extraSlotLeaderEffect));
        assertEquals(depot.getExtraDepotValue(coin), 0);
        assertEquals(depot.getExtraDepotLimit(coin), 2);
    }

    @Test
    public void addExtraSlot2() throws IllegalParameterException, IllegalActionException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect1 = new ExtraSlotLeaderEffect(servant, 2);
        ExtraSlotLeaderEffect extraSlotLeaderEffect2 = new ExtraSlotLeaderEffect(shield, 2);

        depot.addExtraSolt(extraSlotLeaderEffect1);
        assertEquals(depot.getExtraDepotLimit(servant), 2);
        assertEquals(depot.getExtraDepotValue(servant), 0);

        depot.addExtraSolt(extraSlotLeaderEffect2);
        assertEquals(depot.getExtraDepotLimit(shield), 2);
        assertEquals(depot.getExtraDepotValue(shield), 0);
    }

    @Test
    public void addExtraSlot3() throws IllegalParameterException, IllegalActionException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        assertEquals(depot.getExtraDepotLimit(stone), 2);
        assertEquals(depot.getExtraDepotValue(stone), 0);

        depot.addExtraSolt(extraSlotLeaderEffect);
        assertEquals(depot.getExtraDepotLimit(stone), 4);
        assertEquals(depot.getExtraDepotValue(stone), 0);
    }

    @Test
    public void addFaithPointExtra(){
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(faithPoint, 2);

        assertThrows(IllegalParameterException.class, () -> depot.addExtraSolt(extraSlotLeaderEffect));
    }

    @Test
    public void negativeQuantityExtra(){
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, -1);

        assertThrows(IllegalParameterException.class, () -> depot.addExtraSolt(extraSlotLeaderEffect));
    }

    @Test
    public void tooManyExtra() throws IllegalParameterException, IllegalActionException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        assertEquals(depot.getExtraDepotLimit(servant), 2);
        assertEquals(depot.getExtraDepotValue(servant), 0);
        depot.addExtraSolt(extraSlotLeaderEffect);
        assertEquals(depot.getExtraDepotLimit(servant), 4);
        assertEquals(depot.getExtraDepotValue(servant), 0);
        assertThrows(IllegalActionException.class, () -> depot.addExtraSolt(extraSlotLeaderEffect));
    }



}