package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC5Test {

    /** Test createCardTest creates an instance of CGC5 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC5 = new CG_Shape(5);
        assertEquals(5, CGC5.getID());
        assertEquals("Shape", CGC5.getType());
        assertEquals(6, CGC5.getNumOfOccurrences());
        assertEquals(1, CGC5.getDiffType());
        assertEquals(0, CGC5.getStairs());

        assertEquals(0, CGC5.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC5.getPositions().get(0).get(0).getY());
        assertEquals(0, CGC5.getPositions().get(0).get(1).getX());
        assertEquals(1, CGC5.getPositions().get(0).get(1).getY());

        assertEquals(0, CGC5.getPositions().get(1).get(0).getX());
        assertEquals(0, CGC5.getPositions().get(1).get(0).getY());
        assertEquals(1, CGC5.getPositions().get(1).get(1).getX());
        assertEquals(0, CGC5.getPositions().get(1).get(1).getY());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest1 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others
     * 2. All of them are vertically placed
     * */
    @Test
    public void sixOccTest1(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 4 && i == shelf.ROWS - 1 ) || ( j == 4 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 0 && i == shelf.ROWS - 4 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest2 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others
     * 2. 5 of them are vertically placed and 1 is horizontal
     * */
    @Test
    public void sixOccTest2(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 4 && i == shelf.ROWS - 1 ) || ( j == 4 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }


    /** Test sixOccTest3 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others
     * 2. One of them is repeated
     * */
    @Test
    public void sixOccTest3(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 4 && i == shelf.ROWS - 1 ) || ( j == 4 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 4 ) || ( j == 4 && i == shelf.ROWS - 4 ) || ( j == 4 && i == shelf.ROWS - 3 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest4 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type
     * 2. 5 of them are vertically placed and 1 is horizontal
     * */
    @Test
    public void sixOccTest4(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Tile tile = new Tile(T_Type.BOOK, 3);
                Couple couple0 = new Couple(tile);
                if ( (( i == shelf.ROWS - 1 ) || ( i == shelf.ROWS - 2)) ){
                    shelf.setCoordinate(i, j, couple0);
                }  else if (( j == 4 && i == shelf.ROWS - 3 ) || ( j == 3 && i == shelf.ROWS - 3  )) {
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest4");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }
    /** Test sixOccTest5 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type
     * 2. All of them are vertically placed
     * */
    @Test
    public void sixOccTest5(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Tile tile = new Tile(T_Type.BOOK, 3);
                Couple couple0 = new Couple(tile);
                if ( (( i == shelf.ROWS - 1 ) || ( i == shelf.ROWS - 2)) ){
                    shelf.setCoordinate(i, j, couple0);
                }  else if (( j == 4 && i == shelf.ROWS - 3 ) || ( j == 4 && i == shelf.ROWS - 4  )) {
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest5");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest6 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type
     * 2. All of them are horizontally placed
     * */
    @Test
    public void sixOccTest6(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( j == 0 || j == 1){
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest6");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest7 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. All of them have the same tile type
     * 2. 5 of them are horizontally placed and 1 is vertical
     * */
    @Test
    public void sixOccTest7(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( ( j == 0 || j == 1 ) && i != 0 ){
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        Tile tile = new Tile(T_Type.BOOK, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(5, 4, couple0);
        shelf.setCoordinate(4, 4, couple0);


        System.out.println("sixOccTest7");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest8 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Each one of them has a different tile type from the others
     * 2. 5 of them are horizontally placed but 1 is vertical
     * */
    @Test
    public void sixOccTest8(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( i == shelf.ROWS - 1 && j == 0 ) || ( i == shelf.ROWS - 1 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 2 && j == 0 ) || ( i == shelf.ROWS - 2 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 3 && j == 0 ) || ( i == shelf.ROWS - 3 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 4 && j == 0 ) || ( i == shelf.ROWS - 4 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 5 && j == 0 ) || ( i == shelf.ROWS - 5 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("sixOccTest8");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest9 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. Two of them share the same tile type
     * 2. They are placed like the zig-zag piece from tetris
     * */
    @Test
    public void sixOccTest9(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 4 && i == shelf.ROWS - 1 ) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 4 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        Tile tile = new Tile(T_Type.BOOK, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(2, 2, couple0);

        Tile tile1 = new Tile(T_Type.GAMES, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(3, 2, couple1);

        System.out.println("sixOccTest9");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }

    /** Test sixOccTest10 checks if 6 occurrences of 1x2 rectangles are correctly identified when:
     * 1. There are two zig-zag pieces
     * 2. One of them is made of one tile type and the other is made from a different tile type
     * 3. One is placed horizontally and the other vertically
     * */
    @Test
    public void sixOccTest10(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 4 && i == shelf.ROWS - 1 ) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 4 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 ) || ( j == 4 && i == shelf.ROWS - 2 ) || ( j == 4 && i == shelf.ROWS - 3 )){
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        Tile tile = new Tile(T_Type.BOOK, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(2, 2, couple0);

        Tile tile1 = new Tile(T_Type.GAMES, 3);
        Couple couple1 = new Couple(tile1);
        shelf.setCoordinate(3, 2, couple1);
        shelf.setCoordinate(5, 2, couple1);

        System.out.println("sixOccTest10");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC5.checkConditions(shelf));
    }


    /** Test failTest1 checks if checkConditions returns 0 when:
     * 1. There are 6 occurrences of 1x2 rectangles
     * 2. Two of them have the same tile type and are overlapping (it counts only once)
     * */
    @Test
    public void failTest1(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if ( j == 4 && i == shelf.ROWS - 1 ) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 4 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /** Test failTest2 checks if checkConditions returns 0 when:
     *  1. Only five 1x2 rectangles are inside the shelf */
    @Test
    public void failTest2(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( j == 0 && i == shelf.ROWS - 1 ) || ( j == 0 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 1 && i == shelf.ROWS - 1 ) || ( j == 1 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 3 && i == shelf.ROWS - 1 ) || ( j == 3 && i == shelf.ROWS - 2 )) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 0 && i == shelf.ROWS - 3 ) || ( j == 1 && i == shelf.ROWS - 3 )) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC5.checkConditions(shelf));
    }

    /** Test failTest3 checks if checkConditions returns 0 when:
     * 1. There are 6 occurrences of 1x2 rectangles
     * 2. Two of them have the same tile type and are overlapping (it counts only once)
     * */
    @Test
    public void failTest3(){

        Shelf shelf = new Shelf();
        CG_Shape CGC5 = new CG_Shape(5);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (( i == shelf.ROWS - 1 && j == 0 ) || ( i == shelf.ROWS - 1 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 2 && j == 0 ) || ( i == shelf.ROWS - 2 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.TROPHY, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 3 && j == 0 ) || ( i == shelf.ROWS - 3 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.PLANT, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 4 && j == 0 ) || ( i == shelf.ROWS - 4 && j == 1 ) ) {
                    Tile tile = new Tile(T_Type.FRAME, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( i == shelf.ROWS - 5 && j == 0 )) {
                    Tile tile = new Tile(T_Type.GAMES, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                } else if (( j == 2 && i == shelf.ROWS - 1 ) || ( j == 2 && i == shelf.ROWS - 2 ) || ( j == 3 && i == shelf.ROWS - 1 ) ) {
                    Tile tile = new Tile(T_Type.BOOK, 3);
                    Couple couple0 = new Couple(tile);
                    shelf.setCoordinate(i, j, couple0);
                }
            }
        }

        System.out.println("failTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC5.checkConditions(shelf));
    }
}
