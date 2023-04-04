package model.board;

import model.enumerations.Couple;
import model.enumerations.State;
import model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfMaxTest {

    private Shelf s;

    private Couple[][] shelfsMatrix;
    private Couple dummy;

    @Test
    void testMaxFree() {
        dummy.setState(State.PICKED);
        for(int i=0; i<5; i++)
        {
            this.shelfsMatrix[i][0]=dummy;
        }
        assertEquals(4,s.getMaxFree(5));
    }

    @Test
    void testShelfFull() {
        dummy.setState(State.PICKED);
        for(int i=0; i<5; i++)
        {
            this.shelfsMatrix[i][0]=dummy;
            this.shelfsMatrix[i][1]=dummy;
            this.shelfsMatrix[i][2]=dummy;
            this.shelfsMatrix[i][3]=dummy;
            this.shelfsMatrix[i][4]=dummy;

        }
        assertEquals(0,s.getMaxFree(5));
    }
}