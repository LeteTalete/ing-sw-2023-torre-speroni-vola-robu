package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC8Test {
    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_RowCol CGC8;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC8 before each test.
     */
    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
        tiles = new ArrayList<>();
        CGC8 = new CG_RowCol(8);
    }

    /**
     * Test createCardTest creates an instance of CGC8 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC8.getDescription());
        assertEquals(8, CGC8.getID());
        assertEquals("RowCol", CGC8.getType());
        assertEquals(3, CGC8.getNumOfOccurrences());
        assertEquals(3, CGC8.getDiffUpTo());
        assertEquals(1, CGC8.getVertical());
        assertEquals(0, CGC8.getHorizontal());
        assertEquals("Three columns each formed by 6 tiles of maximum three different types.\nOne column can show the same or a different combination of another column.", CGC8.getDescription());

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
        assertEquals(0, CGC8.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
    }

    /**
     * Test threeVerticalTest1 checks if 3 columns each with 3 different tile types are correctly identified.
     */
    @Test
    public void threeVerticalTest1(){

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        System.out.println("threeVerticalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC8.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /**
     * Test threeVerticalTest2 checks if 3 columns each with 2 different tile types are correctly identified.
     */
    @Test
    public void threeVerticalTest2(){

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        System.out.println("threeVerticalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC8.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /**
     * Test threeVerticalTest3 checks if 3 columns each made of one tile type are correctly identified.
     */
    @Test
    public void threeVerticalTest3(){

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        System.out.println("threeVerticalTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC8.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /**
     * Test threeVerticalTest4 checks if 3 columns are correctly identified when one has 3 different tile types, one has
     * 2 different tile types and the last has only one tile type.
     */
    @Test
    public void threeVerticalTest4(){

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        System.out.println("threeVerticalTest4");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC8.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks if checkConditions returns 0 when 2 columns follow the requirements on the card
     * but the third one doesn't.
     */
    @Test
    public void failTest1(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(4,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC8.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
    }

    /**
     * Test failTest2 checks if checkConditions returns 0 when 2 columns follow the requirements on the card
     * but none of the others do.
     */
    @Test
    public void failTest2(){

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
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
        tiles.add(new Tile(T_Type.PLANT, 1));
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
        assertEquals(0, CGC8.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC8.getID()));
    }


}
