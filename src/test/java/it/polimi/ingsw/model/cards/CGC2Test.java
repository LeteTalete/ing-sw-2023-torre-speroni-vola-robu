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
        assertEquals(0, CGC2.getMirror());
        assertEquals(0, CGC2.getStairs());
        assertEquals(0, CGC2.getPositions().get(0).getX());
        assertEquals(0, CGC2.getPositions().get(0).getY());
        assertEquals(0, CGC2.getPositions().get(1).getX());
        assertEquals(1, CGC2.getPositions().get(1).getY());
        assertEquals(1, CGC2.getPositions().get(2).getX());
        assertEquals(1, CGC2.getPositions().get(2).getY());
        assertEquals(1, CGC2.getPositions().get(3).getX());
        assertEquals(0, CGC2.getPositions().get(3).getY());
    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

        System.out.println("emptyShelfTest");
        for ( int i = 0; i < shelf.ROWS; i++){
            for ( int j = 0; j < shelf.COLUMNS; j++){
                if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ){
                    System.out.print( " " + " ");
                } else {
                    System.out.print(shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
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
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);

        Tile tile1 = new Tile(T_Type.CAT, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(1, 1, couple1);

        Tile tile2 = new Tile(T_Type.CAT, 3);
        Couple couple2 = new Couple(tile2);
        shelf.setCoordinate(2, 0, couple2);

        Tile tile3 = new Tile(T_Type.CAT, 3);
        Couple couple3 = new Couple(tile3);
        shelf.setCoordinate(2, 1, couple3);

        Tile tile4 = new Tile(T_Type.CAT, 3);
        Couple couple4 = new Couple(tile4);
        shelf.setCoordinate(4, 4, couple4);

        Tile tile5 = new Tile(T_Type.CAT, 3);
        Couple couple5 = new Couple(tile5);
        shelf.setCoordinate(4, 3, couple5);

        Tile tile6 = new Tile(T_Type.CAT, 3);
        Couple couple6 = new Couple(tile6);
        shelf.setCoordinate(3, 4, couple6);

        Tile tile7 = new Tile(T_Type.CAT, 3);
        Couple couple7 = new Couple(tile7);
        shelf.setCoordinate(3, 3, couple7);

        System.out.println("squareTest1");
        for ( int i = 0; i < shelf.ROWS; i++){
            for ( int j = 0; j < shelf.COLUMNS; j++){
                if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ){
                    System.out.print( " " + " ");
                } else {
                    System.out.print(shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();

        assertEquals(1, CGC2.checkConditions(shelf));
    }

    /** Test squareTest2 checks if two 2x2 squares with the same tile type are correctly identified
     *  when they are adjacent */
    @Test
    public void squareTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC2 = new CG_Shape(2);

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
        shelf.setCoordinate(1, 0, couple0);

        Tile tile1 = new Tile(T_Type.CAT, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(1, 1, couple1);

        Tile tile2 = new Tile(T_Type.CAT, 3);
        Couple couple2 = new Couple(tile2);
        shelf.setCoordinate(2, 0, couple2);

        Tile tile3 = new Tile(T_Type.CAT, 3);
        Couple couple3 = new Couple(tile3);
        shelf.setCoordinate(2, 1, couple3);

        Tile tile4 = new Tile(T_Type.CAT, 3);
        Couple couple4 = new Couple(tile4);
        shelf.setCoordinate(1, 2, couple4);

        Tile tile5 = new Tile(T_Type.CAT, 3);
        Couple couple5 = new Couple(tile5);
        shelf.setCoordinate(2, 2, couple5);

        Tile tile6 = new Tile(T_Type.CAT, 3);
        Couple couple6 = new Couple(tile6);
        shelf.setCoordinate(1, 3, couple6);

        Tile tile7 = new Tile(T_Type.CAT, 3);
        Couple couple7 = new Couple(tile7);
        shelf.setCoordinate(2, 3, couple7);

        System.out.println("squareTest2");
        for ( int i = 0; i < shelf.ROWS; i++){
            for ( int j = 0; j < shelf.COLUMNS; j++){
                if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ){
                    System.out.print( " " + " ");
                } else {
                    System.out.print(shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
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
                shelf.setCoordinate(i, j, couple);
                assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
            }
        }

        Tile tile0 = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile0);
        shelf.setCoordinate(1, 0, couple0);

        Tile tile1 = new Tile(T_Type.CAT, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(1, 1, couple1);

        Tile tile2 = new Tile(T_Type.CAT, 3);
        Couple couple2 = new Couple(tile2);
        shelf.setCoordinate(2, 0, couple2);

        Tile tile3 = new Tile(T_Type.CAT, 3);
        Couple couple3 = new Couple(tile3);
        shelf.setCoordinate(2, 1, couple3);

        Tile tile4 = new Tile(T_Type.CAT, 3);
        Couple couple4 = new Couple(tile4);
        shelf.setCoordinate(1, 2, couple4);

        Tile tile5 = new Tile(T_Type.CAT, 3);
        Couple couple5 = new Couple(tile5);
        shelf.setCoordinate(2, 2, couple5);

        System.out.println("failTest1");
        for ( int i = 0; i < shelf.ROWS; i++){
            for ( int j = 0; j < shelf.COLUMNS; j++){
                if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ){
                    System.out.print( " " + " ");
                } else {
                    System.out.print(shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
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

        Tile tile1 = new Tile(T_Type.CAT, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(4, 0, couple1);

        Tile tile2 = new Tile(T_Type.CAT, 3);
        Couple couple2 = new Couple(tile2);
        shelf.setCoordinate(5, 1, couple2);

        Tile tile3 = new Tile(T_Type.CAT, 3);
        Couple couple3 = new Couple(tile3);
        shelf.setCoordinate(4, 1, couple3);

        System.out.println("failTest2");
        for ( int i = 0; i < shelf.ROWS; i++){
            for ( int j = 0; j < shelf.COLUMNS; j++){
                if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ){
                    System.out.print( " " + " ");
                } else {
                    System.out.print(shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();

        assertEquals(0, CGC2.checkConditions(shelf));
    }

}
