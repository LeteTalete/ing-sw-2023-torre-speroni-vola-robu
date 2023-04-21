package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC3Test {

    /** Test createCardTest creates an instance of CGC3 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC3 = new CG_Shape(3);
        assertEquals(3, CGC3.getID());
        assertEquals("Shape", CGC3.getType());
        assertEquals(1, CGC3.getNumOfOccurrences());
        assertEquals(0, CGC3.getDiffType());
        assertEquals(0, CGC3.getStairs());
        assertEquals(0, CGC3.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC3.getPositions().get(0).get(0).getY());
        assertEquals(1, CGC3.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC3.getPositions().get(0).get(1).getY());
        assertEquals(2, CGC3.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC3.getPositions().get(0).get(2).getY());
        assertEquals(2, CGC3.getPositions().get(0).get(3).getX());
        assertEquals(0, CGC3.getPositions().get(0).get(3).getY());
        assertEquals(0, CGC3.getPositions().get(0).get(4).getX());
        assertEquals(2, CGC3.getPositions().get(0).get(4).getY());
    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Shape CGC3 = new CG_Shape(3);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC3.checkConditions(shelf));
    }

    /** Test xTest checks if an X shape with all couples of the same tile type is correctly identified */
    @Test
    public void xTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC3 = new CG_Shape(3);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(2, 4, couple0);
        shelf.setCoordinate(2, 2, couple0);
        shelf.setCoordinate(3, 3, couple0);
        shelf.setCoordinate(4, 4, couple0);
        shelf.setCoordinate(4, 2, couple0);

        System.out.println("xTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC3.checkConditions(shelf));
    }

    /** Test xTest checks if an X shape with all couples of the same tile type is correctly identified
     * when inside a 3x3 square with all of its couples of the same tile type */
    @Test
    public void xTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC3 = new CG_Shape(3);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(2, 4, couple0);
        shelf.setCoordinate(2, 2, couple0);
        shelf.setCoordinate(3, 3, couple0);
        shelf.setCoordinate(4, 4, couple0);
        shelf.setCoordinate(4, 2, couple0);
        shelf.setCoordinate(2, 3, couple0);
        shelf.setCoordinate(3, 4, couple0);
        shelf.setCoordinate(3, 2, couple0);
        shelf.setCoordinate(4, 3, couple0);

        System.out.println("xTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC3.checkConditions(shelf));
    }

    /** Test failTest checks that checkConditions returns 0 when the X shape has one couple at its center that differs
     *  from the others */
    @Test
    public void failTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC3 = new CG_Shape(3);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(2, 4, couple0);
        shelf.setCoordinate(2, 2, couple0);
        shelf.setCoordinate(4, 4, couple0);
        shelf.setCoordinate(4, 2, couple0);

        Tile tile1 = new Tile(T_Type.TROPHY, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(3, 3, couple1);

        System.out.println("failTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC3.checkConditions(shelf));
    }


}
