package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CGC5Test {

    /**
     * Test createCardTest creates an instance of CGC5 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){
        CG_Shape CGC5 = new CG_Shape(5);
        System.out.println(CGC5.getDescription());
        assertEquals(5, CGC5.getID());
        assertEquals("Shape", CGC5.getType());
        assertEquals(6, CGC5.getNumOfOccurrences());
        assertEquals(1, CGC5.getDiffType());
        assertEquals(0, CGC5.getStairs());
        assertEquals("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).\nThe tiles of one group can be different from those of another group.", CGC5.getDescription());

        assertEquals(0, CGC5.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC5.getPositions().get(0).get(0).getY());
        assertEquals(0, CGC5.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC5.getPositions().get(0).get(1).getY());

        assertEquals(0, CGC5.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC5.getPositions().get(1).get(0).getY());
        assertEquals(1, CGC5.getPositions().get(1).get(1).getX());
        assertEquals(0, CGC5.getPositions().get(1).get(1).getY());

    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }

    /**
     * Test sixOccTest1 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others.
     * 2. All of them are vertically placed.
     */
    @Test
    public void sixOccTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

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
     * Test sixOccTest2 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others.
     * 2. 5 of them are vertically placed and 1 is horizontal.
     */
    @Test
    public void sixOccTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

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
        tiles.add(new Tile(T_Type.GAMES, 1));
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
     * Test sixOccTest3 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others.
     * 2. One of them is repeated.
     */
    @Test
    public void sixOccTest3(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
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
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
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
     * Test sixOccTest4 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type.
     * 2. 5 of them are vertically placed and 1 is horizontal.
     */
    @Test
    public void sixOccTest4(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest4");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest5 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type.
     * 2. All of them are vertically placed.
     */
    @Test
    public void sixOccTest5(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);
        shelf.insertTiles(2, tiles);
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest5");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest11 checks an edge case.
     */
    @Test
    public void sixOcctest11(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
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
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest11");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest6 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type.
     * 2. All of them are horizontally placed.
     */
    @Test
    public void sixOccTest6(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        System.out.println("sixOccTest6");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest7 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type.
     * 2. 5 of them are horizontally placed and 1 is vertical.
     */
    @Test
    public void sixOccTest7(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest7");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest8 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others.
     * 2. 5 of them are horizontally placed but 1 is vertical.
     */
    @Test
    public void sixOccTest8(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(2, tiles);

        System.out.println("sixOccTest8");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest9 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Two of them share the same tile type.
     * 2. They are placed like the zig-zag piece from tetris.
     */
    @Test
    public void sixOccTest9(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest9");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /**
     * Test sixOccTest10 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. There are two zig-zag pieces.
     * 2. One of them is made of one tile type and the other is made from a different tile type.
     * 3. One is placed horizontally and the other vertically.
     */
    @Test
    public void sixOccTest10(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("sixOccTest10");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC5.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
        assertEquals(0, CGC5.checkConditions(shelf));
    }


    /**
     * Test failTest1 checks if checkConditions returns 0 when:
     * 1. There are 6 occurrences of 1x2 rectangles.
     * 2. Two of them have the same tile type and are overlapping (it counts only once).
     */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
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
     * Test failTest2 checks if checkConditions returns 0 when:
     * 1. Only five 1x2 rectangles are inside the shelf.
     */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

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

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }

    /**
     * Test failTest3 checks if checkConditions returns 0 when:
     * 1. There are 6 occurrences of 1x2 rectangles.
     * 2. Two of them have the same tile type and are overlapping (it counts only once).
     */
    @Test
    public void failTest3(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC5 = new CG_Shape(5);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC5.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC5.getID()));
    }
}
