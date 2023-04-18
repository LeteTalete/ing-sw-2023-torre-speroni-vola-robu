package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CGC7Test {

    /** Test createCardTest creates an instance of CGC7 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Shape CGC7 = new CG_Shape(7);
        assertEquals(7, CGC7.getID());
        assertEquals("Shape", CGC7.getType());
        assertEquals(8, CGC7.getNumOfOccurrences());
        assertEquals(0, CGC7.getDiffType());
        assertEquals(0, CGC7.getStairs());

        assertEquals(0, CGC7.getPositions().get(0).get(0).getX());
        assertEquals(0, CGC7.getPositions().get(0).get(0).getY());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC7 = new CG_Shape(7);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC7.checkConditions(shelf));
    }

    /** Test eightOccTest1 checks if eight occurrences of 1x1 squares with the same tile type are correctly identified
     *  when nothing else is inside the shelf */
    @Test
    public void eightOccTest1(){

        Shelf shelf = new Shelf();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC7 = new CG_Shape(7);

        Tile tile = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(4, 0, couple0);
        shelf.setCoordinate(4, 1, couple0);
        shelf.setCoordinate(4, 2, couple0);
        shelf.setCoordinate(5, 0, couple0);
        shelf.setCoordinate(5, 1, couple0);
        shelf.setCoordinate(5, 2, couple0);
        shelf.setCoordinate(5, 3, couple0);
        shelf.setCoordinate(5, 4, couple0);


        System.out.println("eightOccTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC7.checkConditions(shelf));
    }

    /** Test eightOccTest2 checks if eight occurrences of 1x1 squares with the same tile type are correctly identified
     *  when the shelf is full (the shelf has been created so that only 8 cat tiles are inside) */
    @Test
    public void eightOccTest2(){

        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC7 = new CG_Shape(7);
        int[] check = new int[6];

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT)){
                    couple = new Couple(deck.draw());
                }
                if ( couple.getTile().getTileType().equals(T_Type.PLANT) && check[0] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[0]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.FRAME) && check[1] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[1]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.TROPHY) && check[2] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[2]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.BOOK) && check[3] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[3]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.GAMES) && check[4] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[4]++;
                }
            }
        }

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                    if ( check[0] < 6 ){
                        Tile tile = new Tile(T_Type.PLANT, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[0]++;
                    } else if ( check[1] < 6 ){
                        Tile tile = new Tile(T_Type.FRAME, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[1]++;
                    } else if ( check[2] < 6 ){
                        Tile tile = new Tile(T_Type.TROPHY, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[2]++;
                    } else if ( check[3] < 6 ){
                        Tile tile = new Tile(T_Type.BOOK, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[3]++;
                    } else if ( check[4] < 6 ){
                        Tile tile = new Tile(T_Type.GAMES, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[4]++;
                    }
                }
            }
        }


        Tile tile = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(0, 4, couple0);
        shelf.setCoordinate(5, 2, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(4, 3, couple0);
        shelf.setCoordinate(4, 2, couple0);
        shelf.setCoordinate(3, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);
        shelf.setCoordinate(5, 4, couple0);


        System.out.println("eightOccTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC7.checkConditions(shelf));
    }

    /** Test failTest checks that checkConditions returns 0 when inside the shelf there are less than 8 occurrences
     *  of 1x1 squares with the same tile type
     *  The shelf has been created so that for every tile type the max num of occurrences is 7
     *  */
    @Test
    public void failTest(){

        Shelf shelf = new Shelf();
        Deck deck = new Deck();
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        CG_Shape CGC7 = new CG_Shape(7);
        int[] check = new int[6];

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                Couple couple = new Couple(deck.draw());
                while ( couple.getTile().getTileType().equals(T_Type.CAT)){
                    couple = new Couple(deck.draw());
                }
                if ( couple.getTile().getTileType().equals(T_Type.PLANT) && check[0] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[0]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.FRAME) && check[1] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[1]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.TROPHY) && check[2] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[2]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.BOOK) && check[3] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[3]++;
                } else  if ( couple.getTile().getTileType().equals(T_Type.GAMES) && check[4] < 6 ){
                    shelf.setCoordinate(i, j, couple);
                    check[4]++;
                }
            }
        }

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if (shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                    if ( check[0] < 6 ){
                        Tile tile = new Tile(T_Type.PLANT, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[0]++;
                    } else if ( check[1] < 6 ){
                        Tile tile = new Tile(T_Type.FRAME, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[1]++;
                    } else if ( check[2] < 6 ){
                        Tile tile = new Tile(T_Type.TROPHY, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[2]++;
                    } else if ( check[3] < 6 ){
                        Tile tile = new Tile(T_Type.BOOK, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[3]++;
                    } else if ( check[4] < 6 ){
                        Tile tile = new Tile(T_Type.GAMES, 3);
                        Couple couple0 = new Couple(tile);
                        shelf.setCoordinate(i, j, couple0);
                        check[4]++;
                    }
                }
            }
        }

        Tile tile = new Tile(T_Type.CAT, 3);
        Couple couple0 = new Couple(tile);
        shelf.setCoordinate(0, 4, couple0);
        shelf.setCoordinate(5, 2, couple0);
        shelf.setCoordinate(1, 1, couple0);
        shelf.setCoordinate(4, 3, couple0);
        shelf.setCoordinate(4, 2, couple0);
        shelf.setCoordinate(3, 0, couple0);
        shelf.setCoordinate(2, 1, couple0);

        System.out.println("failTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC7.checkConditions(shelf));
    }


}
