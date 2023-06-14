package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC0Test {

    private Shelf shelf;
    private ArrayList<Tile> tiles;
    private CG_Shape CGC0;

    /**
     * Method setUp creates a new shelf, a new ArrayList of tiles and a new CGC0 before each test.
     */
    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
        tiles = new ArrayList<>();
        CGC0 = new CG_Shape(0);
    }

    /**
     * Test createCardTest creates an instance of CGC0 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){

        System.out.println(CGC0.getDescription());
        assertEquals(0, CGC0.getID());
        assertEquals("Shape", CGC0.getType());
        assertEquals(1, CGC0.getNumOfOccurrences());
        assertEquals(0, CGC0.getDiffType());
        assertEquals(0, CGC0.getStairs());
        assertEquals("Five tiles of the same type forming a diagonal.", CGC0.getDescription());

        assertEquals(0, CGC0.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC0.getPositions().get(0).get(0).getY());
        assertEquals(1, CGC0.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC0.getPositions().get(0).get(1).getY());
        assertEquals(2, CGC0.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC0.getPositions().get(0).get(2).getY());
        assertEquals(3, CGC0.getPositions().get(0).get(3).getX());
        assertEquals(3, CGC0.getPositions().get(0).get(3).getY());
        assertEquals(4, CGC0.getPositions().get(0).get(4).getX());
        assertEquals(4, CGC0.getPositions().get(0).get(4).getY());

        assertEquals(0, CGC0.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC0.getPositions().get(1).get(0).getY());
        assertEquals(-1, CGC0.getPositions().get(1).get(1).getX());
        assertEquals(1, CGC0.getPositions().get(1).get(1).getY());
        assertEquals(-2, CGC0.getPositions().get(1).get(2).getX());
        assertEquals(2, CGC0.getPositions().get(1).get(2).getY());
        assertEquals(-3, CGC0.getPositions().get(1).get(3).getX());
        assertEquals(3, CGC0.getPositions().get(1).get(3).getY());
        assertEquals(-4, CGC0.getPositions().get(1).get(4).getX());
        assertEquals(4, CGC0.getPositions().get(1).get(4).getY());
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
        assertEquals(0, CGC0.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
    }

    /**
     * Test diagonalTest1 checks if the diagonal ( i == j ) is correctly identified.
     */
    @Test
    public void diagonalTest1(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("diagonalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC0.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /**
     * Test diagonalTest2 checks if the diagonal ( i - j = 1 ) is correctly identified.
     */
    @Test
    public void diagonalTest2(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("diagonalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC0.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /**
     * Test diagonalTest3 checks if when two diagonals ( i - j = 1  and i == j ) are present inside the shelf then the
     * card requirements are still correctly met.
     */
    @Test
    public void diagonalTest3(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("diagonalTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC0.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /**
     * Test mirrorDiagonalTest1 checks if the diagonal ( i + j = 4 ) is correctly identified.
     */
    @Test
    public void mirrorDiagonalTest1(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("mirrorDiagonalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC0.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /**
     * Test mirrorDiagonalTest2 checks if the diagonal ( i + j = 5 ) is correctly identified.
     */
    @Test
    public void mirrorDiagonalTest2(){

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("mirrorDiagonalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC0.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks that checkConditions returns 0 (diagonal not found) when there is a diagonal
     * with 4 couples of the same tile type and 1 couple with a different tile type from the others.
     */
    @Test
    public void failTest1(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC0.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
    }

    /**
     * Test failTest2 checks that checkConditions returns 0 (diagonal not found) when there is a diagonal
     * with less than 5 couples of the same tile type.
     */
    @Test
    public void failTest2(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
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
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC0.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
    }

    /**
     * Test failTest3 checks that checkConditions returns 0 (diagonal not found) when there is a mirrored diagonal
     * with less than 5 couples of the same tile type.
     */
    @Test
    public void failTest3(){

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
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
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC0.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC0.getID()));
    }

}
