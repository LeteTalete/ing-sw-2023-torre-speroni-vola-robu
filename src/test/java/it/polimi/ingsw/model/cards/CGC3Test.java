package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC3Test {

    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_Shape CGC3;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC3 before each test.
     */
    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
        tiles = new ArrayList<>();
        CGC3 = new CG_Shape(3);
    }

    /**
     * Test createCardTest creates an instance of CGC3 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC3.getDescription());
        assertEquals(3, CGC3.getID());
        assertEquals("Shape", CGC3.getType());
        assertEquals(1, CGC3.getNumOfOccurrences());
        assertEquals(0, CGC3.getDiffType());
        assertEquals(0, CGC3.getStairs());
        assertEquals("Five tiles of the same type forming an X.", CGC3.getDescription());

        assertEquals(0, CGC3.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC3.getPositions().get(0).get(0).getY());
        assertEquals(1, CGC3.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC3.getPositions().get(0).get(1).getY());
        assertEquals(2, CGC3.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC3.getPositions().get(0).get(2).getY());
        assertEquals(2, CGC3.getPositions().get(0).get(3).getX());
        assertEquals(0, CGC3.getPositions().get(0).get(3).getY());
        assertEquals(0, CGC3.getPositions().get(0).get(4).getX());
        assertEquals(2, CGC3.getPositions().get(0).get(4).getY());
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
        assertEquals(0, CGC3.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC3.getID()));
    }

    /**
     * Test xTest1 checks if an X shape with all couples of the same tile type is correctly identified.
     */
    @Test
    public void xTest1(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("xTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC3.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC3.getID()));
        assertEquals(0, CGC3.checkConditions(shelf));
    }

    /**
     * Test xTest2 checks if an X shape with all couples of the same tile type is correctly identified
     * when inside a 3x3 square with all of its couples of the same tile type.
     */
    @Test
    public void xTest2(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("xTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC3.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC3.getID()));
        assertEquals(0, CGC3.checkConditions(shelf));
    }

    /**
     * Test failTest checks that checkConditions returns 0 when the X shape has one couple at its center that differs
     * from the others.
     */
    @Test
    public void failTest(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC3.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC3.getID()));
    }


}
