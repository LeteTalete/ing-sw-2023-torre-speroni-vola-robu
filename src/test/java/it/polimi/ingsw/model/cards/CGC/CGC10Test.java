package it.polimi.ingsw.model.cards.CGC;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.CG_RowCol;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * test to check the correct behaviour of CGC10 with all the possible scenarios
 * */

public class CGC10Test {
    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_RowCol CGC10;
    private Shelf testShelf;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC10 before each test.
     */
    @BeforeEach
    public void setUp() {
        Player player = new Player();
        shelf = player.getMyShelf();
        tiles = new ArrayList<>();
        CGC10 = new CG_RowCol(10);
    }

    /**
     * Test createCardTest creates an instance of CGC10 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC10.getDescription());
        assertEquals(10, CGC10.getID());
        assertEquals("RowCol", CGC10.getType());
        assertEquals(2, CGC10.getNumOfOccurrences());
        assertEquals(0, CGC10.getDiffUpTo());
        assertEquals(1, CGC10.getVertical());
        assertEquals(0, CGC10.getHorizontal());
        assertEquals("Two columns each formed by 6 different types of tiles.", CGC10.getDescription());

    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC10.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC10.getID()));
    }

    /**
     * Test verticalTest1 checks if 2 columns each with 6 different tile types are correctly identified when inside an
     * empty shelf.
     */
    @Test
    public void verticalTest1(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        testShelf = shelf;

        System.out.println("verticalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC10.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC10.getID()));
        assertEquals(0, CGC10.checkConditions(shelf));
    }

    /**
     * Test verticalTest2 checks if 2 columns each with 6 different tile types are correctly identified while the
     * others are each with less than 6 different tile types.
     */
    @Test
    public void verticalTest2(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(4, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(1, tiles);

        System.out.println("verticalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC10.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC10.getID()));
        assertEquals(0, CGC10.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks if checkConditions returns 0 when one column satisfies the card requirements while the
     * other doesn't.
     */
    @Test
    public void failTest1(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC10.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC10.getID()));
    }

    /**
     * Test failTest2 checks if checkConditions returns 0 when one column satisfies the card requirements while the
     * others don't.
     */
    @Test
    public void failTest2() {

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC10.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC10.getID()));
    }

    /**
     * getTestShelf method returns the shelf used for the tests.
     * @return the shelf used for the tests.
     * */
    public Shelf getTestShelf() {
        return testShelf;
    }
}
