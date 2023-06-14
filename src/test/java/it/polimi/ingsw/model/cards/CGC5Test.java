package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC5Test {

    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_Groups CGC5;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC5 before each test.
     */
    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
        tiles = new ArrayList<>();
        CGC5 = new CG_Groups(5);
    }

    /**
     * Test createCardTest creates an instance of CGC5 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC5.getDescription());
        assertEquals(5, CGC5.getID());
        assertEquals("Groups", CGC5.getType());
        assertEquals(6, CGC5.getNumOfOccurrences());
        assertEquals(2, CGC5.getAtLeast());
        assertEquals("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).\nThe tiles of one group can be different from those of another group.", CGC5.getDescription());

    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }

    /**
     * Test sixOccTest1 checks if 6 groups each with a different tile type from the others and each with exactly 2
     * elements are correctly identified.
     */
    @Test
    public void sixOccTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest2 checks if 6 groups each with a different tile type from the others and each with at least 2
     * elements are correctly identified.
     */
    @Test
    public void sixOccTest2(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest3 checks if 6 groups regardless of their tile type each with at least 2 elements are correctly identified.
     */
    @Test
    public void sixOccTest3(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks if checkConditions returns 0 when there are 5 groups with exactly 2 elements and one group
     * with only one element.
     */
    @Test
    public void failTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }
    /**
     * Test failTest2 checks if checkConditions returns 0 when there are 5 groups with at least 2 elements and one group
     * with only one element.
     */
    @Test
    public void failTest2(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }

    /**
     * Test failTest3 checks if checkConditions returns 0 when there are only 5 groups with at least 2 elements.
     */
    @Test
    public void failTest3(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }

}
