package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC1Test {

    /** Test createCardTest creates an instance of CGC1 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC1 = new CG_Shape(1,4);
        assertEquals(1, CGC1.getID());
        assertEquals("Shape", CGC1.getType());
        assertEquals(1, CGC1.getNumOfOccurrences());
        assertEquals(0, CGC1.getDiffType());
        assertEquals(0, CGC1.getStairs());
        assertEquals(0, CGC1.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC1.getPositions().get(0).get(0).getY());
        assertEquals(4, CGC1.getPositions().get(0).get(1).getX());
        assertEquals(0, CGC1.getPositions().get(0).get(1).getY());
        assertEquals(0, CGC1.getPositions().get(0).get(2).getX());
        assertEquals(5, CGC1.getPositions().get(0).get(2).getY());
        assertEquals(4, CGC1.getPositions().get(0).get(3).getX());
        assertEquals(5, CGC1.getPositions().get(0).get(3).getY());
    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC1 = new CG_Shape(1,4);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC1.checkConditions(shelf));
    }

    /** Test cornerTest checks if 4 corners with the same tile type are correctly identified*/
    @Test
    public void cornersTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC1 = new CG_Shape(1,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == 0 && j == 0 || i == 0 && j == 4 || i == 5 && j == 0 || i == 5 && j == 4 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple1 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple1);
                    assertEquals(couple1.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple1.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("cornerTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC1.checkConditions(shelf));
    }

    /** Test differentCornerTest checks that checkConditions returns 0 when we have 3 corners with the same tile type
     * and 1 corner with a different tile type */
    @Test
    public void differentCornerTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC1 = new CG_Shape(1,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == 0 && j == 0 || i == 0 && j == 4 || i == 5 && j == 0 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple1 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple1);
                    assertEquals(couple1.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple1.getState(), shelfsMatrix[i][j].getState());
                } else if ( i == 5 && j == 4 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple2 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple2);
                    assertEquals(couple2.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple2.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("differentCornerTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC1.checkConditions(shelf));
    }

    /** Test emptyCornerTest checks that checkConditions returns 0 when we have 2 empty corners and 2 corners with the
     *  same tile type */
    @Test
    public void emptyCornerTest(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC1 = new CG_Shape(1,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == 0 && j == 0 || i == 0 && j == 4 ) {

                } else if( i == 5 && j == 0 || i == 5 && j == 4 ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple1 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple1);
                    assertEquals(couple1.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple1.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("emptyCornerTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC1.checkConditions(shelf));
    }

}
