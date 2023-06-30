package it.polimi.ingsw.model.cards.CGC;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.CG_Shape;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * test to check the correct behaviour of CGC1 with all the possible scenarios
 * */

public class CGC1Test {

    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_Shape CGC1;
    private Shelf testShelf;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC1 before each test.
     */
    @BeforeEach
    public void setUp() {
        Player player = new Player();
        shelf = player.getMyShelf();
        tiles = new ArrayList<>();
        CGC1 = new CG_Shape(1);
    }

    /**
     * Test createCardTest creates an instance of CGC1 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC1.getDescription());
        assertEquals(1, CGC1.getID());
        assertEquals("Shape", CGC1.getType());
        assertEquals(1, CGC1.getNumOfOccurrences());
        assertEquals(0, CGC1.getDiffType());
        assertEquals(0, CGC1.getStairs());
        assertEquals("Four tiles of the same type in the four corners of the bookshelf.", CGC1.getDescription());

        assertEquals(0, CGC1.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC1.getPositions().get(0).get(0).getY());
        assertEquals(4, CGC1.getPositions().get(0).get(1).getX());
        assertEquals(0, CGC1.getPositions().get(0).get(1).getY());
        assertEquals(0, CGC1.getPositions().get(0).get(2).getX());
        assertEquals(5, CGC1.getPositions().get(0).get(2).getY());
        assertEquals(4, CGC1.getPositions().get(0).get(3).getX());
        assertEquals(5, CGC1.getPositions().get(0).get(3).getY());
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
        assertEquals(0, CGC1.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC1.getID()));
    }

    /**
     * Test cornerTest checks if 4 corners with the same tile type are correctly identified.
     */
    @Test
    public void cornersTest(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        testShelf = shelf;

        System.out.println("cornerTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC1.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC1.getID()));
        assertEquals(0, CGC1.checkConditions(shelf));
    }

    /**
     * Test differentCornerTest checks that checkConditions returns 0 when we have 3 corners with the same tile type
     * and 1 corner with a different tile type.
     */
    @Test
    public void differentCornerTest(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("differentCornerTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC1.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC1.getID()));
    }

    /**
     * Test emptyCornerTest checks that checkConditions returns 0 when we have 2 empty corners and 2 corners with the
     * same tile type.
     */
    @Test
    public void emptyCornerTest(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("emptyCornerTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC1.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC1.getID()));
    }

    /**
     * getTestShelf method returns the shelf used for the tests.
     * @return the shelf used for the tests.
     * */
    public Shelf getTestShelf() {
        return testShelf;
    }
}
