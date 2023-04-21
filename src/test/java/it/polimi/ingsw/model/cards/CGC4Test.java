package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC4Test {

    /** Test createCardTest creates an instance of CGC4 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC4 = new CG_Shape(4,4);
        assertEquals(4, CGC4.getID());
        assertEquals("Shape", CGC4.getType());
        assertEquals(1, CGC4.getNumOfOccurrences());
        assertEquals(0, CGC4.getDiffType());
        assertEquals(1, CGC4.getStairs());

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

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /** Test stairsTest checks if the stairs are correctly identified */
    @Test
    public void stairsTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS && j < i ; j++) {
                Couple couple = new Couple(deck.draw());
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        System.out.println("stairsTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC4.checkConditions(shelf));
    }

    /** Test mirrorStairsTest checks if the stairs when mirrored are correctly identified */
    @Test
    public void mirrorStairsTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS ; j++) {
                if (i + j >= 5) {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("mirrorStairsTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC4.checkConditions(shelf));
    }

    /** Test failTest1 checks that checkConditions returns 0 when the stairs are missing a step */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS && j < i ; j++) {
                if ( j == 2 ) {

                } else {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /** Test failTest2 checks that checkConditions returns 0 when the mirrored stairs are missing a step */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 4 ) {

                } else if (i + j >= 5) {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /** Test failTest3 checks that checkConditions returns 0 when the stairs don't follow the pattern described
     *  on the rulebook 5<-4<-3<-2<-1 where each number represents the number of tiles required inside the columns */
    @Test
    public void failTest3(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS && j <= i ; j++) {
                Couple couple = new Couple(deck.draw());
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC4.checkConditions(shelf));
    }

    /** Test failTest4 checks that checkConditions returns 0 when the stairs don't follow the pattern described
     *  on the rulebook 1->2->3->4->5 where each number represents the number of tiles required inside the columns */
    @Test
    public void failTest4(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC4 = new CG_Shape(4,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (i + j >= 4 ) {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("failTest4");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC4.checkConditions(shelf));
    }
}
