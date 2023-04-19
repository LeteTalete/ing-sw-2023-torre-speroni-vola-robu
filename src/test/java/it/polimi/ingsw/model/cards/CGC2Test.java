package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC2Test {

    /** Test createCardTest creates an instance of CGC2 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC2 = new CG_Shape(2);
        assertEquals(2, CGC2.getID());
        assertEquals("Shape", CGC2.getType());
        assertEquals(2, CGC2.getNumOfOccurrences());
        assertEquals(0, CGC2.getDiffType());
        assertEquals(0, CGC2.getStairs());
        assertEquals(0, CGC2.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC2.getPositions().get(0).get(0).getY());
        assertEquals(0, CGC2.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC2.getPositions().get(0).get(1).getY());
        assertEquals(1, CGC2.getPositions().get(0).get(2).getX());
        assertEquals(1, CGC2.getPositions().get(0).get(2).getY());
        assertEquals(1, CGC2.getPositions().get(0).get(3).getX());
        assertEquals(0, CGC2.getPositions().get(0).get(3).getY());
    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /** Test squareTest1 checks if two 2x2 squares with the same tile type are correctly identified
     *  when they are far apart */
    @Test
    public void squareTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                    couple = new Couple(deck.draw());
                }
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(2, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);
        shelf.setCoordinate(4, 4, couple0);
        shelf.setCoordinate(4, 3, couple0);
        shelf.setCoordinate(3, 4, couple0);
        shelf.setCoordinate(3, 3, couple0);

        System.out.println("squareTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC2.checkConditions(shelf));
    }

    /** Test squareTest2 checks if checkConditions can correctly identify two not overlapping 2x2 squares when tiles are
     *  placed so that multiple overlapping 2x2 square can be counted
     *  In the test tiles are placed so that you can count four overlapping 2x2 squares but since we need two not
     *  overlapping squares the algorithm checks if those two squares can fit inside that space */
    @Test
    public void squareTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                    couple = new Couple(deck.draw());
                }
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(2, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);
        shelf.setCoordinate(1, 2, couple0);
        shelf.setCoordinate(2, 2, couple0);
        shelf.setCoordinate(1, 3, couple0);
        shelf.setCoordinate(2, 3, couple0);
        shelf.setCoordinate(0, 1, couple0);
        shelf.setCoordinate(0, 2, couple0);

        System.out.println("squareTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC2.checkConditions(shelf));
    }

    /** Test failTest1 checks that checkConditions returns 0 when there's a 2x3 rectangle with all its couples of the
     * same tile type */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);


        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                    couple = new Couple(deck.draw());
                }
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(2, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);
        shelf.setCoordinate(1, 2, couple0);
        shelf.setCoordinate(2, 2, couple0);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /** Test failTest2 checks that checkConditions returns 0 when there is only one 2x2 square of the same tile type*/
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(5, 0, couple0);
        shelf.setCoordinate(4, 0, couple0);
        shelf.setCoordinate(5, 1, couple0);
        shelf.setCoordinate(4, 1, couple0);

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /** Test failTest3 checks that checkConditions returns 0 when two squares with the same tile type share
     *  one single couple (it counts only as one square) */
    @Test
    public void failTest3(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                    couple = new Couple(deck.draw());
                }
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(2, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);
        shelf.setCoordinate(3, 1, couple0);
        shelf.setCoordinate(2, 2, couple0);
        shelf.setCoordinate(3, 2, couple0);

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

    /** Test failTest4 checks that checkConditions returns 0 when there's a 3x3 square of the same tile type
     *  Even though you can count multiple squares inside in the end only one counts  */
    @Test
    public void failTest4(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                    couple = new Couple(deck.draw());
                }
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

        System.out.println("failTest4");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

}
