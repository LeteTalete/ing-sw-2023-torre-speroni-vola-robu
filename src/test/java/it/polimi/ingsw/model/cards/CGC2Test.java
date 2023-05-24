package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC2Test {

    /**
     * Test createCardTest creates an instance of CGC2 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){
        CG_Shape CGC2 = new CG_Shape(2);
        System.out.println(CGC2.getDescription());
        assertEquals(2, CGC2.getID());
        assertEquals("Shape", CGC2.getType());
        assertEquals(2, CGC2.getNumOfOccurrences());
        assertEquals(1, CGC2.getDiffType());
        assertEquals(0, CGC2.getStairs());
        assertEquals("Two groups each containing 4 tiles of the same type in a 2x2 square.\nThe tiles of one square can be different from those of the other square.", CGC2.getDescription());

        assertEquals(0, CGC2.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC2.getPositions().get(0).get(0).getY());
        assertEquals(0, CGC2.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC2.getPositions().get(0).get(1).getY());
        assertEquals(1, CGC2.getPositions().get(0).get(2).getX());
        assertEquals(1, CGC2.getPositions().get(0).get(2).getY());
        assertEquals(1, CGC2.getPositions().get(0).get(3).getX());
        assertEquals(0, CGC2.getPositions().get(0).get(3).getY());
    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Shape CGC2 = new CG_Shape(2);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

    /**
     * Test squareTest1 checks if two 2x2 squares with the same tile type are correctly identified.
     */
    @Test
    public void squareTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("squareTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC2.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /**
     * Test squareTest2 checks if two 2x2 squares with different tile type are correctly identified
     * when they are far apart.
     */
    @Test
    public void squareTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("squareTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC2.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /**
     * Test squareTest2 checks if two 2x2 squares with different tile type are correctly identified
     * when they are close.
     */
    @Test
    public void squareTest3(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("squareTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC2.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /**
     * Test squareTest4 checks if two 2x2 squares with different tile types are correctly identified inside an empty shelf.
     */
    @Test
    public void squareTest4(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(4, tiles);


        System.out.println("squareTest4");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC2.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /**
     * Test failTest5 checks if checkConditions returns 0 when two 2x2 squares with the same tile type are adjacent.
     */
    @Test
    public void failTest5(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest5");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

    /**
     * Test failTest1 checks that checkConditions returns 0 when one of the 2x2 squares has one of its neighbour cells
     * the same tile type of that 2x2 square. (Both squares have the same tile type)
     */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

    /**
     * Test failTest2 checks that checkConditions returns 0 when there is only one 2x2 square of the same tile type.
     */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

    /**
     * Test failTest3 checks that checkConditions returns 0 when one of the 2x2 squares has one of its neighbour cells
     * the same tile type of that 2x2 square. (The two squares have different tile types)
     */
    @Test
    public void failTest3(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

    /**
     * Test failTest4 checks that checkConditions returns 0 when there's a 3x3 square of the same tile type.
     */
    @Test
    public void failTest4(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Shape CGC2 = new CG_Shape(2);

        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest4");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC2.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC2.getID()));
    }

}
