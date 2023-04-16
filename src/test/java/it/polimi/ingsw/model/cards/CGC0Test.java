package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;


public class CGC0Test {

    /** Test createCardTest creates an instance of CGC0 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC0 = new CG_Shape(0);
        assertEquals(0, CGC0.getID());
        assertEquals("Shape", CGC0.getType());
        assertEquals(1, CGC0.getNumOfOccurrences());
        assertEquals(0, CGC0.getDiffType());
        assertEquals(0, CGC0.getStairs());

        assertEquals(0, CGC0.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC0.getPositions().get(0).get(0).getY());
        assertEquals(1, CGC0.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC0.getPositions().get(0).get(1).getY());
        assertEquals(2, CGC0.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC0.getPositions().get(0).get(2).getY());
        assertEquals(3, CGC0.getPositions().get(0).get(3).getX());
        assertEquals(3, CGC0.getPositions().get(0).get(3).getY());
        assertEquals(4, CGC0.getPositions().get(0).get(4).getX());
        assertEquals(4, CGC0.getPositions().get(0).get(4).getY());

        assertEquals(0, CGC0.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC0.getPositions().get(1).get(0).getY());
        assertEquals(-1, CGC0.getPositions().get(1).get(1).getX());
        assertEquals(1, CGC0.getPositions().get(1).get(1).getY());
        assertEquals(-2, CGC0.getPositions().get(1).get(2).getX());
        assertEquals(2, CGC0.getPositions().get(1).get(2).getY());
        assertEquals(-3, CGC0.getPositions().get(1).get(3).getX());
        assertEquals(3, CGC0.getPositions().get(1).get(3).getY());
        assertEquals(-4, CGC0.getPositions().get(1).get(4).getX());
        assertEquals(4, CGC0.getPositions().get(1).get(4).getY());
    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

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

        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /** Test diagonalTest1 checks if the diagonal ( i == j ) is correctly identified */
    @Test
    public void diagonalTest1(){

        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == j ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("diagonalTest1");
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

        assertEquals(1, CGC0.checkConditions(shelf));
    }

    /** Test diagonalTest2 checks if the diagonal ( i - j = 1 ) is correctly identified */
    @Test
    public void diagonalTest2(){

        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i - j == 1 ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("diagonalTest2");
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

        assertEquals(1, CGC0.checkConditions(shelf));
    }

    /** Test mirrorDiagonalTest1 checks if the diagonal ( i + j = 4 ) is correctly identified */
    @Test
    public void mirrorDiagonalTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i + j == 4 ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("mirrorDiagonalTest1");
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

        assertEquals(1, CGC0.checkConditions(shelf));
    }

    /** Test mirrorDiagonalTest2 checks if the diagonal ( i + j = 5 ) is correctly identified */
    @Test
    public void mirrorDiagonalTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i + j == 5 ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        System.out.println("mirrorDiagonalTest2");
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

        assertEquals(1, CGC0.checkConditions(shelf));
    }

    /** Test failTest1 checks that checkConditions returns 0 (diagonal not found) when there is a diagonal
     *  with 4 couples of the same tile type and 1 couple with a different tile type from the others */
    @Test
    public void failTest1(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == j ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        Tile tile = new Tile(T_Type.TROPHY, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(3, 3, couple0);

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

        assertEquals(0, CGC0.checkConditions(shelf));
    }

    /** Test failTest2 checks that checkConditions returns 0 (diagonal not found) when there is a diagonal
     *  with less than 5 couples of the same tile type */
    @Test
    public void failTest2(){
        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC0 = new CG_Shape(0);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i - j == 2 ){
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                } else {
                    Couple couple = new Couple(deck.draw());
                    while ( couple.getTile().getTileType().equals(T_Type.CAT) ){
                        couple = new Couple(deck.draw());
                    }
                    shelf.setCoordinate(i, j, couple);
                    assertEquals(couple.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        Tile tile = new Tile(T_Type.TROPHY, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(3, 3, couple0);

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

        assertEquals(0, CGC0.checkConditions(shelf));
    }

}
