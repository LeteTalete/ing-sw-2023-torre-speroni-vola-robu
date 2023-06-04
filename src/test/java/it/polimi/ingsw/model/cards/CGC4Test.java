package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC4Test {

    /**
     * Test createCardTest creates an instance of CGC4 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){
        CG_Shape CGC4 = new CG_Shape(4);
        System.out.println(CGC4.getDescription());
        assertEquals(4, CGC4.getID());
        assertEquals("Shape", CGC4.getType());
        assertEquals(1, CGC4.getNumOfOccurrences());
        assertEquals(0, CGC4.getDiffType());
        assertEquals(1, CGC4.getStairs());
        assertEquals("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right,\neach next column must be made of exactly one more tile. Tiles can be of any type.", CGC4.getDescription());

        assertEquals(0, CGC4.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC4.getPositions().get(0).get(0).getY());
        assertEquals(1, CGC4.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC4.getPositions().get(0).get(1).getY());
        assertEquals(2, CGC4.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC4.getPositions().get(0).get(2).getY());
        assertEquals(3, CGC4.getPositions().get(0).get(3).getX());
        assertEquals(3, CGC4.getPositions().get(0).get(3).getY());
        assertEquals(4, CGC4.getPositions().get(0).get(4).getX());
        assertEquals(4, CGC4.getPositions().get(0).get(4).getY());

        assertEquals(0, CGC4.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC4.getPositions().get(1).get(0).getY());
        assertEquals(-1, CGC4.getPositions().get(1).get(1).getX());
        assertEquals(1, CGC4.getPositions().get(1).get(1).getY());
        assertEquals(-2, CGC4.getPositions().get(1).get(2).getX());
        assertEquals(2, CGC4.getPositions().get(1).get(2).getY());
        assertEquals(-3, CGC4.getPositions().get(1).get(3).getX());
        assertEquals(3, CGC4.getPositions().get(1).get(3).getY());
        assertEquals(-4, CGC4.getPositions().get(1).get(4).getX());
        assertEquals(4, CGC4.getPositions().get(1).get(4).getY());
    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Shape CGC4 = new CG_Shape(4);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

    /**
     * Test stairsTest checks if the stairs are correctly identified.
     */
    @Test
    public void stairsTest(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("stairsTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC4.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /**
     * Test mirrorStairsTest checks if the stairs when mirrored are correctly identified.
     */
    @Test
    public void mirrorStairsTest(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("mirrorStairsTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC4.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks that checkConditions returns 0 when the stairs are missing a step.
     */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

    /**
     * Test failTest2 checks that checkConditions returns 0 when the mirrored stairs are missing a step.
     */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(3, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

    /**
     * Test failTest3 checks that checkConditions returns 0 when the stairs don't follow the pattern described
     * on the rulebook 5<-4<-3<-2<-1 where each number represents the number of tiles required inside the columns.
     */
    @Test
    public void failTest3(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

    /**
     * Test failTest4 checks that checkConditions returns 0 when the stairs don't follow the pattern described
     * on the rulebook 1->2->3->4->5 where each number represents the number of tiles required inside the columns.
     */
    @Test
    public void failTest4(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest4");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

    /**
     * Test failTest5 checks that checkConditions returns 0 when one step is higher than the others.
     */
    @Test
    public void failTest5(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC4 = new CG_Shape(4);

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest5");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC4.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC4.getID()));
    }

}
