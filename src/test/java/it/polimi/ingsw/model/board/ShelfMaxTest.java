package it.polimi.ingsw.model.board;

import game.model.board.Shelf;
import game.model.enumerations.Couple;
import game.model.enumerations.State;
import org.junit.jupiter.api.Test;

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
}