package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC7Test {

    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_Shape CGC7;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC7 before each test.
     */
    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
        tiles = new ArrayList<>();
        CGC7 = new CG_Shape(7);
    }
    /**
     * Test createCardTest creates an instance of CGC7 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC7.getDescription());
        assertEquals(7, CGC7.getID());
        assertEquals("Shape", CGC7.getType());
        assertEquals(8, CGC7.getNumOfOccurrences());
        assertEquals(0, CGC7.getDiffType());
        assertEquals(0, CGC7.getStairs());
        assertEquals("Eight tiles of the same type. There’s no restriction about the position of these tiles.", CGC7.getDescription());

        assertEquals(0, CGC7.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC7.getPositions().get(0).get(0).getY());

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
        assertEquals(0, CGC7.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC7.getID()));
    }

    /**
     * Test eightOccTest1 checks if eight occurrences of 1x1 squares with the same tile type are correctly identified
     * when nothing else is inside the shelf.
     */
    @Test
    public void eightOccTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("eightOccTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC7.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC7.getID()));
        assertEquals(0, CGC7.checkConditions(shelf));
    }

    /**
     * Test eightOccTest2 checks if eight occurrences of 1x1 squares with the same tile type are correctly identified
     * when the shelf is full (the shelf has been set up so that the only tile type that appears 8 times is CAT).
     */
    @Test
    public void eightOccTest2(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("eightOccTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC7.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC7.getID()));
        assertEquals(0, CGC7.checkConditions(shelf));
    }

    /**
     * Test failTest checks that checkConditions returns 0 when inside the shelf there are less than 8 occurrences
     * of 1x1 squares with the same tile type.
     * The shelf has been set up so that for every tile type the max num of occurrences is 7.
     */
    @Test
    public void failTest(){

        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC7.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC7.getID()));
    }


}
