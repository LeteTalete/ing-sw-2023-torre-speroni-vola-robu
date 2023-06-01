package it.polimi.ingsw.model.board;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.enumerations.T_Type;
import org.junit.jupiter.api.Test;

public class TileTest {

    /**
     * Test getFigureTest checks if the method getFigure returns the correct figure.
     */
    @Test
    public void getFigureTest() {
        Tile tile = new Tile(T_Type.CAT, 1);
        assertEquals(1, tile.getFigure());
    }

    /**
     * Test tileTest checks if the constructor of the class Tile works properly.
     */
    @Test
    public void tileTest(){
        Tile tile = new Tile(T_Type.CAT, 1);
        assertEquals(T_Type.CAT, tile.getTileType());
        assertEquals(1, tile.getFigure());
    }

}
