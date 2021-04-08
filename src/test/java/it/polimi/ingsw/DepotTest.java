package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import org.junit.jupiter.api.Test;

class DepotTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faithPoint = ResourceType.FAITHPOINT;

    @Test
    public void ctrlInit() throws IllegalArgumentException {
        Depot depot = new Depot();
        assertEquals(depot.getResourceFromDepot(coin),0);
        assertEquals(depot.getResourceFromDepot(stone),0);
        assertEquals(depot.getResourceFromDepot(shield),0);
        assertEquals(depot.getResourceFromDepot(servant),0);
    }

    @Test
    public void add1() throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        assertEquals(depot.getResourceFromDepot(coin),2);
        assertEquals(depot.getShelfType(3), coin);
    }

    @Test
    public void add2() throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3, 3);
        depot.addToShelf(stone, 1, 1);
        depot.addToShelf(shield, 2, 2);


        assertEquals(depot.getResourceFromDepot(stone),1);
        assertEquals(depot.getResourceFromDepot(shield),2);
        assertEquals(depot.getResourceFromDepot(servant),3);
        assertEquals(depot.getShelfType(1), stone);
        assertEquals(depot.getShelfType(2), shield);
        assertEquals(depot.getShelfType(3), servant);
    }

    @Test
    public void addPartialShelves() throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(stone, 1, 2);

        assertEquals(depot.getResourceFromDepot(coin),2);
        assertEquals(depot.getResourceFromDepot(stone),1);

        assertEquals(depot.getShelfType(3), coin);
        assertEquals(depot.getShelfType(2), stone);
    }

    @Test
    public void addFaithPoint(){
        Depot depot = new Depot();
        assertThrows(IllegalArgumentException.class, () -> depot.addToShelf(faithPoint, 1, 1));
    }

    @Test
    public void addNegativeQuantity(){
        Depot depot = new Depot();
        assertThrows(IllegalArgumentException.class, () -> depot.addToShelf(coin, -1, 1));
    }

    @Test
    public void shelfAddOutOfBound(){
        Depot depot = new Depot();
        assertThrows(IllegalArgumentException.class, () -> depot.addToShelf(stone, 1, 4));
        assertThrows(IllegalArgumentException.class, () -> depot.addToShelf(stone, 1, 0));
    }

    @Test
    public void addAlreadyInDepot() throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 1, 1);
        assertThrows(IllegalActionException.class, () -> depot.addToShelf(shield, 2, 2));

        assertEquals(depot.getResourceFromDepot(shield),1);
        assertEquals(depot.getShelfType(1), shield);
    }

    @Test
    public void addZero() throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        Depot depot = new Depot();

        assertTrue(depot.addToShelf(servant, 0, 1));
        assertNull(depot.getShelfType(1));
        assertEquals(depot.getResourceFromDepot(servant), 0);
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
    public void notEnoughSpace3Add() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        depot.addToShelf(shield, 2, 3);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(shield, 2, 3));
        assertEquals(exception.getAvailableSpace(), 1);
    }

    @Test
    public void removeRes1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 2, 2);
        assertTrue(depot.removeFromDepot(2,1));
        assertEquals(depot.getResourceFromDepot(servant), 1);
        assertEquals(depot.getShelfType(2), servant);
    }

    @Test
    public void removeRes2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(stone, 2, 3);
        depot.removeFromDepot(3,1);
        assertEquals(depot.getResourceFromDepot(stone), 1);
        assertEquals(depot.getShelfType(3), stone);
    }

    @Test
    public void removeRes3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
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

        assertEquals(depot.getResourceFromDepot(stone), 0);
        assertEquals(depot.getResourceFromDepot(coin), 1);
        assertEquals(depot.getResourceFromDepot(shield), 1);

        assertNull(depot.getShelfType(1));
        assertEquals(depot.getShelfType(2), coin);
        assertEquals(depot.getShelfType(3), shield);
    }

    @Test
    public void multipleRemoves() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 3, 3);

        depot.removeFromDepot(3,1);
        assertEquals(depot.getResourceFromDepot(coin), 2);
        assertEquals(depot.getShelfType(3), coin);

        depot.removeFromDepot(3, 1);
        assertEquals(depot.getResourceFromDepot(coin), 1);
        assertEquals(depot.getShelfType(3), coin);

        depot.removeFromDepot(3, 1);
        assertEquals(depot.getResourceFromDepot(coin), 0);
        assertNull(depot.getShelfType(3));
    }

    @Test
    public void shelfRemoveOutOfBound1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalArgumentException.class, () -> depot.removeFromDepot(0, 3));
    }

    @Test
    public void shelfRemoveOutOfBound2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalArgumentException.class, () -> depot.removeFromDepot(4, 3));
    }

    @Test
    public void removeNegativeQuantity() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalArgumentException.class, () -> depot.removeFromDepot(3, -1));
    }

    @Test
    public void removeNotInDepot() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertThrows(IllegalArgumentException.class, () -> depot.removeFromDepot(1, 3));
    }

    @Test
    public void removeZeroQuantity() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(servant, 3 , 3);
        assertTrue(depot.removeFromDepot(3, 0));
        assertEquals(depot.getResourceFromDepot(servant), 3);
        assertEquals(depot.getShelfType(3), servant);
    }

    @Test
    public void notEnouthResourcesRemove() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 2 , 3);
        assertThrows(IllegalActionException.class, () -> depot.removeFromDepot(3, 3));
    }

    @Test
    public void move1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 2);
        depot.addToShelf(stone, 2, 3);

        assertEquals(depot.getShelfType(2), coin);
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(2, 3));
        assertEquals(depot.getShelfType(2), stone);
        assertEquals(depot.getShelfType(3), coin);

        assertEquals(depot.getResourceFromDepot(coin), 2);
        assertEquals(depot.getResourceFromDepot(stone), 2);

    }

    @Test
    public void move2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
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
    public void move3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
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
    public void move4() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
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
    public void move5() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(stone, 1, 3);

        assertNull(depot.getShelfType(1));
        assertEquals(depot.getShelfType(3), stone);

        assertTrue(depot.moveBetweenShelves(3, 1));

        assertNull(depot.getShelfType(3));
        assertEquals(depot.getShelfType(1), stone);
    }


    @Test
    public void sourceShelfOutOfBound1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> depot.moveBetweenShelves(0, 2));
    }

    @Test
    public void sourceShelfOutOfBound2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> depot.moveBetweenShelves(4, 2));
    }

    @Test
    public void destShelfOutOfBound1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> depot.moveBetweenShelves(1, 0));
    }

    @Test
    public void destShelfOutOfBound2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(coin, 2, 3);
        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> depot.moveBetweenShelves(1, 4));
    }

    @Test
    public void noResourceToMove() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();

        depot.addToShelf(shield, 2, 2);

        assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(1, 2));
    }

    @Test
    public void notEnoughSpaceMove1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();

        depot.addToShelf(stone, 1, 1);
        depot.addToShelf(shield, 3, 3);

        exception = assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(3, 1));
        assertEquals(exception.getMessage(), "Not enough space in destination shelf");
    }

    @Test
    public void notEnoughSpaceMove2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();

        depot.addToShelf(servant, 2, 2);
        depot.addToShelf(shield, 1, 1);

        exception = assertThrows(IllegalActionException.class, () -> depot.moveBetweenShelves(1, 2));
        assertEquals(exception.getMessage(), "Not enough space in source shelf");
    }

    @Test
    public void init() throws IllegalArgumentException {
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
    public void addExtraSlot() throws IllegalArgumentException, IllegalActionException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        assertTrue(depot.addExtraSolt(extraSlotLeaderEffect));
        assertEquals(depot.getExtraDepotValue(coin), 0);
        assertEquals(depot.getExtraDepotLimit(coin), 2);
    }

    @Test
    public void addExtraSlot2() throws IllegalArgumentException, IllegalActionException {
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
    public void addExtraSlot3() throws IllegalArgumentException, IllegalActionException {
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

        assertThrows(IllegalArgumentException.class, () -> depot.addExtraSolt(extraSlotLeaderEffect));
    }

    @Test
    public void negativeQuantityExtra(){
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, -1);

        assertThrows(IllegalArgumentException.class, () -> depot.addExtraSolt(extraSlotLeaderEffect));
    }

    @Test
    public void tooManyExtra() throws IllegalArgumentException, IllegalActionException {
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

    @Test
    public void addToLeader1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        assertTrue(depot.addToLeader(stone, 1));
        assertEquals(depot.getExtraDepotValue(stone), 1);
    }

    @Test
    public void addToLeader2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addExtraSolt(extraSlotLeaderEffect);
        assertTrue(depot.addToLeader(stone, 3));
        assertEquals(depot.getExtraDepotValue(stone), 3);
    }

    @Test
    public void addToLeader3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(stone, 2);
        assertEquals(depot.getExtraDepotValue(stone), 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(stone, 1);
        assertEquals(depot.getExtraDepotValue(stone), 3);
    }

    @Test
    public void addToLeader4() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(shield, 2);
        ExtraSlotLeaderEffect extraSlotLeaderEffect2 = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(shield, 2);
        assertEquals(depot.getExtraDepotValue(shield), 2);

        depot.addExtraSolt(extraSlotLeaderEffect2);
        depot.addToLeader(coin, 1);
        assertEquals(depot.getExtraDepotValue(coin), 1);
    }

    @Test
    public void addFaithPointLeader() throws IllegalArgumentException, IllegalActionException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        exception = assertThrows(IllegalArgumentException.class, () -> depot.addToLeader(faithPoint, 1));
        assertEquals(exception.getMessage(), "Depot can't handle faith points");
    }

    @Test
    public void negativeQuantityLeader() throws IllegalArgumentException, IllegalActionException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        exception = assertThrows(IllegalArgumentException.class, () -> depot.addToLeader(servant, -1));
        assertEquals(exception.getMessage(), "Negative quantity");
    }

    @Test
    public void addWrongResource() throws IllegalArgumentException, IllegalActionException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        exception = assertThrows(IllegalActionException.class, () -> depot.addToLeader(coin, 1));
        assertEquals(exception.getMessage(), "No existing extra slot for this resource");
    }

    @Test
    public void notEnoughSpaceLeader1() throws IllegalArgumentException, IllegalActionException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToLeader(servant, 3));
        assertEquals(exception.getAvailableSpace(), 2);
        assertEquals(exception.getMessage(), "Not enough space in extra slot");
    }

    @Test
    public void notEnoughSpaceLeader2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 1);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToLeader(servant, 2));
        assertEquals(exception.getAvailableSpace(), 1);
        assertEquals(exception.getMessage(), "Not enough space in extra slot");
    }

    @Test
    public void addZeroToLeader() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        assertTrue(depot.addToLeader(stone, 0));
        assertEquals(depot.getExtraDepotValue(stone), 0);
    }

    @Test
    public void removeFromLeader1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(coin, 2);

        assertTrue(depot.removeFromLeader(coin, 1));
        assertEquals(depot.getExtraDepotValue(coin), 1);
    }

    @Test
    public void removeFromLeader2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(coin, 2);

        assertTrue(depot.removeFromLeader(coin, 2));
        assertEquals(depot.getExtraDepotValue(coin), 0);
    }

    @Test
    public void removeFromLeader3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);
        ExtraSlotLeaderEffect extraSlotLeaderEffect2 = new ExtraSlotLeaderEffect(stone, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(coin, 2);
        depot.removeFromLeader(coin, 1);

        depot.addExtraSolt(extraSlotLeaderEffect2);
        depot.addToLeader(stone, 1);
        depot.removeFromLeader(stone, 1);

        assertEquals(depot.getExtraDepotValue(coin), 1);
        assertEquals(depot.getExtraDepotValue(stone), 0);
    }

    @Test
    public void removeFaithPointLeader() {
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.removeFromLeader(faithPoint, 1));
        assertEquals(exception.getMessage(), "Depot can't handle faith points");
    }

    @Test
    public void removeNegativeQuantityLeader() {
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.removeFromLeader(stone, -1));
        assertEquals(exception.getMessage(), "Negative quantity");
    }

    @Test
    public void removeWrongResource() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(shield, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(shield, 2);

        exception = assertThrows(IllegalActionException.class, () -> depot.removeFromLeader(stone, 1));
        assertEquals(exception.getMessage(), "No existing extra slot for this resource");
    }

    @Test
    public void removeTooManyResourcesLeader() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(shield, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(shield, 2);

        exception = assertThrows(IllegalActionException.class, () -> depot.removeFromLeader(shield, 3));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void removeZeroFromLeader() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(coin, 2);

        assertTrue(depot.removeFromLeader(coin, 0));
        assertEquals(depot.getExtraDepotValue(coin), 2);
    }

    @Test
    public void moveToLeader1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 2, 2);
        assertTrue(depot.moveToLeader(2, 1));
        assertEquals(depot.getExtraDepotValue(coin), 1);
        assertEquals(depot.getResourceFromDepot(coin), 1);
    }

    @Test
    public void moveToLeader2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 2, 2);
        assertTrue(depot.moveToLeader(2, 2));
        assertEquals(depot.getExtraDepotValue(coin), 2);
        assertEquals(depot.getResourceFromDepot(coin), 0);
        assertNull(depot.getShelfType(2));
    }

    @Test
    public void moveToLeader3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 2, 3);
        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 1, 3);

        depot.moveToLeader(3, 3);
        assertEquals(depot.getExtraDepotValue(coin), 3);
        assertEquals(depot.getResourceFromDepot(coin), 0);
    }

    @Test
    public void moveTooManyResToLeader() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 2, 3);

        exception = assertThrows(IllegalActionException.class, () -> depot.moveToLeader(3, 3));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void moveTooManyResToLeader2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 3, 3);

        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.moveToLeader(3, 3));
        assertEquals(exception.getMessage(), "Not enough space in extra slot");
        assertEquals(exception.getAvailableSpace(), 2);
    }

    @Test
    public void moveToLeaderNegativeQuantity() {
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToLeader(3, -1));
        assertEquals(exception.getMessage(), "Negative quantity");
    }

    @Test
    public void moveToLeaderShelfOutOfBound1() {
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToLeader(4, 2));
        assertEquals(exception.getMessage(), "ShelfNum out of bound");
    }

    @Test
    public void moveToLeaderShelfOutOfBound2() {
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToLeader(0, 2));
        assertEquals(exception.getMessage(), "ShelfNum out of bound");
    }

    @Test
    public void moveZeroToLeader() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(coin, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToShelf(coin, 2, 2);
        assertTrue(depot.moveToLeader(2, 0));
        assertEquals(depot.getExtraDepotValue(coin), 0);
        assertEquals(depot.getResourceFromDepot(coin), 2);
    }

    @Test
    public void moveToShelf1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        assertTrue(depot.moveToShelf(servant, 1, 1));
        assertEquals(depot.getExtraDepotValue(servant), 1);
        assertEquals(depot.getResourceFromDepot(servant), 1);
    }

    @Test
    public void moveToShelf2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        assertTrue(depot.moveToShelf(servant, 2, 2));
        assertEquals(depot.getExtraDepotValue(servant), 0);
        assertEquals(depot.getResourceFromDepot(servant), 2);
    }

    @Test
    public void moveToShelf3() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        assertTrue(depot.moveToShelf(servant, 1, 3));
        assertEquals(depot.getExtraDepotValue(servant), 1);
        assertEquals(depot.getResourceFromDepot(servant), 1);
    }

    @Test
    public void moveTooManyResToShelf1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        exception = assertThrows(IllegalActionException.class, () ->depot.moveToShelf(servant, 3, 3));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void moveTooManyResToShelf2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.moveToShelf(servant, 2, 1));
        assertEquals(exception.getMessage(), "Not enought space in depot");
        assertEquals(exception.getAvailableSpace(), 1);
    }

    @Test
    public void moveFaithPointToShelf(){
        Exception exception;
        Depot depot = new Depot();

        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToShelf(faithPoint, 1, 1));
        assertEquals(exception.getMessage(), "Depot can't handle faith points");
    }

    @Test
    public void moveNegativeQuantityToShelf() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToShelf(coin, -5, 1));
        assertEquals(exception.getMessage(), "Negative quantity");
    }

    @Test
    public void moveResToWrongShelf1() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToShelf(servant, 2, 0));
        assertEquals(exception.getMessage(), "ShelfNum out of bound");
    }

    @Test
    public void moveResToWrongShelf2() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Exception exception;
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        exception = assertThrows(IllegalArgumentException.class, () -> depot.moveToShelf(servant, 2, 4));
        assertEquals(exception.getMessage(), "ShelfNum out of bound");
    }

    @Test
    public void moveZeroToShelf() throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        Depot depot = new Depot();
        ExtraSlotLeaderEffect extraSlotLeaderEffect = new ExtraSlotLeaderEffect(servant, 2);

        depot.addExtraSolt(extraSlotLeaderEffect);
        depot.addToLeader(servant, 2);
        assertTrue(depot.moveToShelf(servant, 0, 1));
        assertEquals(depot.getExtraDepotValue(servant), 2);
        assertEquals(depot.getResourceFromDepot(servant), 0);
    }












}