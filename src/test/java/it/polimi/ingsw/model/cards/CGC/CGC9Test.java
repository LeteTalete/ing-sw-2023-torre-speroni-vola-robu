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


public class CGC9Test {
    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_RowCol CGC9;
    private Shelf testShelf;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC9 before each test.
     */
    @BeforeEach
    public void setUp() {
        Player player = new Player();
        shelf = player.getMyShelf();
        tiles = new ArrayList<>();
        CGC9 = new CG_RowCol(9);
    }

    /**
     * Test createCardTest creates an instance of CGC9 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC9.getDescription());
        assertEquals(9, CGC9.getID());
        assertEquals("RowCol", CGC9.getType());
        assertEquals(4, CGC9.getNumOfOccurrences());
        assertEquals(3, CGC9.getDiffUpTo());
        assertEquals(0, CGC9.getVertical());
        assertEquals(1, CGC9.getHorizontal());
        assertEquals("Four lines each formed by 5 tiles of maximum three different types.\nOne line can show the same or a different combination of another line.", CGC9.getDescription());

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
        assertEquals(0, CGC9.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
    }

    /**
     * Test threeHorizontalTest1 checks if 4 rows each made with 3 different tile types are correctly identified.
     */
    @Test
    public void threeHorizontalTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(4,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(1,tiles);
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(2,tiles);

        testShelf = shelf;

        System.out.println("threeHorizontalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC9.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
        assertEquals(0, CGC9.checkConditions(shelf));
    }

    /**
     * Test threeHorizontalTest2 checks if 4 rows each made with 1 tile type are correctly identified.
     */
    @Test
    public void threeHorizontalTest2(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(1,tiles);
        shelf.insertTiles(2,tiles);
        shelf.insertTiles(3,tiles);
        shelf.insertTiles(4,tiles);

        System.out.println("threeHorizontalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC9.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
        assertEquals(0, CGC9.checkConditions(shelf));
    }

    /**
     * Test threeHorizontalTest3 checks if 4 rows are correctly identified when 3 are made each with one tile type
     * and the last one is made with 3 tile types.
     */
    @Test
    public void threeHorizontalTest3(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(1,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2,tiles);
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(4,tiles);

        System.out.println("threeHorizontalTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(1, CGC9.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
        assertEquals(0, CGC9.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks if checkConditions return 0 when there are only 3 rows that satisfy the card requirements.
     */
    @Test
    public void failTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(4,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(1,tiles);
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(2,tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC9.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
    }

    /**
     * Test failTest2 checks if checkConditions return 0 when there is only one row that satisfies the card requirements.
     */
    @Test
    public void failTest2(){

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(0,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(4,tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC9.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
    }

    /**
     * Test failTest3 checks if checkConditions return 0 when there are only 3 rows that satisfy the card requirements
     * but the last one doesn't.
     */
    @Test
    public void failTest3(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(4,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(1,tiles);
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(0,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(4,tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyClaimed().isEmpty());
        assertEquals(0, CGC9.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyClaimed().contains(CGC9.getID()));
    }


    public Shelf getTestShelf() {
        return testShelf;
    }
}
