package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


class ShelfMaxTest {

    private Shelf s;

    private Couple dummy;

    /**
     * Test insertTilesTest checks if the tiles are inserted correctly in the shelf.
     */
    @Test
    public void insertTilesTest(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(new Tile(T_Type.CAT,1));
        hand.add(new Tile(T_Type.BOOK,2));
        hand.add(new Tile(T_Type.TROPHY,3));
        shelf.insertTiles(0,hand);
        shelf.printShelf();

        assertEquals(shelf.getCouple(new Position(5,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(shelf.getCouple(new Position(4,0)).getTile().getTileType(),T_Type.BOOK);
        assertEquals(shelf.getCouple(new Position(3,0)).getTile().getTileType(),T_Type.TROPHY);
    }

    /**
     * Test checkEnoughSpaceTest checks if the shelf has enough space to insert the tiles.
     */
    @Test
    public void checkEnoughSpaceTest(){
        Shelf shelf = new Shelf();
        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(new Tile(T_Type.CAT,1));
        hand.add(new Tile(T_Type.BOOK,1));
        hand.add(new Tile(T_Type.TROPHY,1));
        hand.add(new Tile(T_Type.PLANT,1));
        hand.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,hand);
        shelf.insertTiles(1,hand);
        shelf.insertTiles(2,hand);
        shelf.insertTiles(3,hand);
        shelf.insertTiles(4,hand);
        shelf.printShelf();

        ArrayList<Position> choice = new ArrayList<>();
        choice.add(new Position(4,4));
        assertTrue(shelf.checkEnoughSpace(choice));

        choice.add(new Position(4,3));
        assertFalse(shelf.checkEnoughSpace(choice));

        choice.add(new Position(4,2));
        assertFalse(shelf.checkEnoughSpace(choice));

        shelf.setFreeSlots(1,0);
        shelf.setFreeSlots(2,0);
        shelf.printShelf();
        assertTrue(shelf.checkEnoughSpace(choice));
    }

    /**
     * Test setCoordinateTest checks if the tile is inserted in the correct position.
     */
    @Test
    public void setCoordinateTest(){
        Tile tile = new Tile(T_Type.CAT,1);
        Couple couple = new Couple(tile);

        Shelf shelf = new Shelf();
        shelf.setCoordinate(5,0,couple);
        shelf.printShelf();

        assertEquals(shelf.getCouple(new Position(5,0)).getTile().getTileType(),T_Type.CAT);
    }

    @Test
    void testMaxFree() {
        s = new Shelf();
        dummy = new Couple();
        dummy.setState(State.PICKED);
        for(int i=0; i<Shelf.COLUMNS; i++)
        {
            s.getShelfsMatrix()[0][i]=dummy;
        }
        assertEquals(5,s.getMaxFree(5));
    }

    @Test
    void checkShelfFullTest() {
        s = new Shelf();
        dummy = new Couple();
        dummy.setState(State.PICKED);
        for(int i=0; i<Shelf.ROWS; i++) {
            for(int j=0; j<Shelf.COLUMNS; j++) {
                s.getShelfsMatrix()[i][j] = dummy;
            }
        }
        assertTrue(s.checkShelfFull());
    }

    @Test
    public void getFreeColTest()
    {
        Shelf shelf = new Shelf();

        shelf.printShelf();
        assertEquals(6,shelf.getFreeCol(0));

        ArrayList<Tile> tiles = new ArrayList<Tile>();

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,2));
        tiles.add(new Tile(T_Type.TROPHY,3));
        shelf.insertTiles(0,tiles);
        shelf.printShelf();
        assertEquals(3,shelf.getFreeCol(0));

        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(2,tiles);
        shelf.printShelf();
        assertEquals(5,shelf.getFreeCol(2));


        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,3));
        tiles.add(new Tile(T_Type.FRAME,2));
        shelf.insertTiles(3,tiles);
        shelf.printShelf();
        assertEquals(4,shelf.getFreeCol(3));
    }

    @Test
    public void getMaxFreeTest()
    {
        Shelf shelf = new Shelf();

        shelf.printShelf();
        assertEquals(6,shelf.getMaxFree(5));
        assertEquals(6,shelf.getMaxFree(0));
        assertEquals(6,shelf.getMaxFree(2));
        assertEquals(6,shelf.getMaxFree(4));

        ArrayList<Tile> tiles = new ArrayList<Tile>();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4,tiles);

        shelf.printShelf();
        assertEquals(5,shelf.getMaxFree(5));
        assertEquals(0,shelf.getMaxFree(1));
        assertEquals(2,shelf.getMaxFree(0));
        assertEquals(5,shelf.getMaxFree(3));

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(3,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4,tiles);

        shelf.printShelf();
        assertEquals(0,shelf.getMaxFree(2));
        assertEquals(0,shelf.getMaxFree(5));
    }

    @Test
    public void additionalPointsTest()
    {
        s = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        s.printShelf();
        assertEquals(0,s.additionalPoints());

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,2));
        s.insertTiles(0, tiles);
        s.printShelf();
        assertEquals(0,s.additionalPoints());

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        s.insertTiles(0, tiles);
        s.printShelf();
        assertEquals(2,s.additionalPoints());

        //this is the same case shown in the rulebook
        s.clearShelf();
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        s.insertTiles(0, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        s.insertTiles(1, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        s.insertTiles(2, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        s.insertTiles(3, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        s.insertTiles(4, tiles);
        s.printShelf();
        assertEquals(21,s.additionalPoints());


        //trying with shelf full
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        s.insertTiles(2, tiles);
        s.insertTiles(3, tiles);
        tiles.add(new Tile(T_Type.TROPHY,1));
        s.insertTiles(4, tiles);
        s.printShelf();
        assertEquals(24,s.additionalPoints());

        //trying with isolated tiles
        s.setFreeSlots(0,3);
        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        s.insertTiles(3,tiles);
        s.printShelf();
        assertEquals(21,s.additionalPoints());
    }
}