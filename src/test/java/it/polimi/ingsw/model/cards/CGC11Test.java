package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class CGC11Test {

    /**
     * Test createCardTest creates an instance of CGC11 and checks if all parameters are correct.
     */
    @Test
    public void createCardTest(){
        CG_RowCol CGC11 = new CG_RowCol(11);
        System.out.println(CGC11.getDescription());
        assertEquals(11, CGC11.getID());
        assertEquals("RowCol", CGC11.getType());
        assertEquals(2, CGC11.getNumOfOccurrences());
        assertEquals(0, CGC11.getDiffUpTo());
        assertEquals(0, CGC11.getVertical());
        assertEquals(1, CGC11.getHorizontal());
        assertEquals("Two lines each formed by 5 different types of tiles.\nOne line can show the same or a different combination of the other line.", CGC11.getDescription());

    }

    /**
     * Test emptyShelfTest checks that the card is not accepted when the shelf is empty.
     */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_RowCol CGC11 = new CG_RowCol(11);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC11.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC11.getID()));
    }

    /**
     * Test horizontalTest1 checks if 2 rows each with 5 different tile types are correctly identified.
     */
    @Test
    public void horizontalTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_RowCol CGC11 = new CG_RowCol(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        System.out.println("horizontalTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC11.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC11.getID()));
        assertEquals(0, CGC11.checkConditions(shelf));
    }

    /**
     * Test horizontalTest2 checks if 2 rows each with 5 different tile types are correctly identified inside a full shelf.
     */
    @Test
    public void horizontalTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_RowCol CGC11 = new CG_RowCol(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        System.out.println("horizontalTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(1, CGC11.checkConditions(shelf));
        assertTrue(shelf.getCardsAlreadyChecked().contains(CGC11.getID()));
        assertEquals(0, CGC11.checkConditions(shelf));
    }

    /**
     * Test failTest1 checks if checkConditions returns 0 when one row satisfies the card requirements while the
     * other one is randomly generated with up to 4 different tile types.
     */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_RowCol CGC11 = new CG_RowCol(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC11.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC11.getID()));
    }

    /**
     * Test failTest2 checks if checkConditions returns 0 when one row satisfies the card requirements while the
     * others don't.
     */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_RowCol CGC11 = new CG_RowCol(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(4, tiles);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertTrue(shelf.getCardsAlreadyChecked().isEmpty());
        assertEquals(0, CGC11.checkConditions(shelf));
        assertFalse(shelf.getCardsAlreadyChecked().contains(CGC11.getID()));
    }


}
