package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
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
    public void negativeQuantity(){
        Depot depot = new Depot();
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(coin, -1, 1));
    }

    @Test
    public void shelfOutOfBound(){
        Depot depot = new Depot();
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(stone, 1, 4));
        assertThrows(IllegalParameterException.class, () -> depot.addToShelf(stone, 1, 0));
    }

    @Test
    public void alreadyInDepot() throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
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
    public void notEnoughSpace1() {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(coin, 6, 3));
        assertEquals(exception.getAvailableSpace(), 3);
    }

    @Test
    public void notEnoughSpace2() {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(coin, 2, 1));
        assertEquals(exception.getAvailableSpace(), 1);
    }

    @Test
    public void notEnoughSpace3() throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        NotEnoughSpaceException exception;
        Depot depot = new Depot();

        depot.addToShelf(shield, 2, 3);
        exception = assertThrows(NotEnoughSpaceException.class, () -> depot.addToShelf(shield, 2, 3));
        assertEquals(exception.getAvailableSpace(), 1);
    }




}