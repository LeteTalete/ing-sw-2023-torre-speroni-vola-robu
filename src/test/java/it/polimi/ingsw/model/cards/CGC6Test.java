package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC6Test {

    /** Test createCardTest creates an instance of CGC6 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC6 = new CG_Shape(6,4);
        assertEquals(6, CGC6.getID());
        assertEquals("Shape", CGC6.getType());
        assertEquals(4, CGC6.getNumOfOccurrences());
        assertEquals(1, CGC6.getDiffType());
        assertEquals(0, CGC6.getStairs());

        assertEquals(0, CGC6.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC6.getPositions().get(0).get(0).getY());
        assertEquals(0, CGC6.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC6.getPositions().get(0).get(1).getY());
        assertEquals(0, CGC6.getPositions().get(0).get(2).getX());
        assertEquals(2, CGC6.getPositions().get(0).get(2).getY());
        assertEquals(0, CGC6.getPositions().get(0).get(3).getX());
        assertEquals(3, CGC6.getPositions().get(0).get(3).getY());

        assertEquals(0, CGC6.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC6.getPositions().get(1).get(0).getY());
        assertEquals(1, CGC6.getPositions().get(1).get(1).getX());
        assertEquals(0, CGC6.getPositions().get(1).get(1).getY());
        assertEquals(2, CGC6.getPositions().get(1).get(2).getX());
        assertEquals(0, CGC6.getPositions().get(1).get(2).getY());
        assertEquals(3, CGC6.getPositions().get(1).get(3).getX());
        assertEquals(0, CGC6.getPositions().get(1).get(3).getY());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC6.checkConditions(shelf));
    }

    /** Test fourOccTest1 checks if four 1x4 rectangles with different tile type are correctly identified */
    @Test
    public void fourOccTest1(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 0  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (j == 1  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 2  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 3  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("fourOccTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC6.checkConditions(shelf));
    }

    /** Test fourOccTest2 checks if four 1x4 rectangles are correctly identified when:
     * 1. All of them have a different tile type from the others
     * 2. Three of them are vertically placed but one is horizontal */
    @Test
    public void fourOccTest2(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);


        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 0  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (j == 1  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 2  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 1 && j != 4 ) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("fourOccTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC6.checkConditions(shelf));
    }


    /** Test fourOccTest3 checks if four 1x4 rectangles are correctly identified when:
     * 1. All of them have the same tile type */
    @Test
    public void fourOccTest3(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Tile tile = new Tile(T_Type.CAT, 3);
                Couple couple0 = new Couple(tile);
                if ( j == 0  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    shelf.setCoordinate(i, j, couple0);
                } else if (j == 1  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 2  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 3  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }


        System.out.println("fourOccTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC6.checkConditions(shelf));
    }

    /** Test fourOccTest4 checks if four 1x4 rectangles are correctly identified when:
     * 1. All of them have a different tile type from the others
     * 2. Three of them are horizontally placed but one is vertical */
    @Test
    public void fourOccTest4(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == shelf.ROWS - 1 && j != shelf.COLUMNS - 1 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 2 && j != shelf.COLUMNS - 1 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 3 && j != shelf.COLUMNS - 1 ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }  else if ( j == 4 && i != shelf.ROWS - 5 && i != shelf.ROWS - 6) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("fourOccTest4");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC6.checkConditions(shelf));
    }

    /** Test failTest1 checks if checkConditions returns 0 when:
     * 1. There are 4 occurrences of 1x4 rectangles
     * 2. Two of them have the same tile type and are overlapping by one square (it counts only once)
     * */
    @Test
    public void failTest1(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 0  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (j == 1  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 2  && i != shelf.ROWS - 1 && i != shelf.ROWS - 6 && i != shelf.ROWS - 5) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 3 && i != shelf.ROWS - 6 && i != shelf.ROWS - 5) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 1 && j != 4 ) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC6.checkConditions(shelf));
    }

    /** Test failTest2 checks if checkConditions returns 0 when:
     *  1. Only three 1x4 rectangles are inside the shelf */
    @Test
    public void failTest2(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 0  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (j == 1  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 2  && i != shelf.ROWS - 5 && i != shelf.ROWS - 6 ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC6.checkConditions(shelf));
    }

    /** Test failTest3 checks if checkConditions returns 0 when:
     * 1. There are 4 occurrences of 1x4 rectangles
     * 2. Two of them have the same tile type and are overlapping by one square (it counts only once)
     * */
    @Test
    public void failTest3(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC6 = new CG_Shape(6,4);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i == shelf.ROWS - 1 && j != shelf.COLUMNS - 1 ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 2 && j != shelf.COLUMNS - 1 ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( i == shelf.ROWS - 3 && j != shelf.COLUMNS - 1 && j != 0 ) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }  else if ( j == 4 && i != shelf.ROWS - 5 && i != shelf.ROWS - 6) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC6.checkConditions(shelf));
    }
}
