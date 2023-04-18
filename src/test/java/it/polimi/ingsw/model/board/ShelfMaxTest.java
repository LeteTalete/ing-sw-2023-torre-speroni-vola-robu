package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShelfMaxTest {

    private Shelf s;

    private Couple dummy;

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
    void testShelfFull() {
        s = new Shelf();
        dummy = new Couple();
        dummy.setState(State.PICKED);
        for(int i=0; i<Shelf.ROWS; i++) {
            for(int j=0; j<Shelf.COLUMNS; j++) {
                s.getShelfsMatrix()[i][j] = dummy;
            }
        }
        assertEquals(0,s.getMaxFree(5));
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
}