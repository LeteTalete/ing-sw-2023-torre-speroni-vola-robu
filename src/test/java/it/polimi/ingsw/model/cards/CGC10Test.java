package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CGC10Test {

    /** Test createCardTest creates an instance of CGC10 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Group CGC10 = new CG_Group(10);
        assertEquals(10, CGC10.getID());
        assertEquals("Group", CGC10.getType());
        assertEquals(2, CGC10.getNumOfOccurrences());
        assertEquals(0, CGC10.getDiffUpTo());
        assertEquals(1, CGC10.getVertical());
        assertEquals(0, CGC10.getHorizontal());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Group CGC10 = new CG_Group(10);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC10.checkConditions(shelf));
    }

    /** Test verticalTest1 checks if 2 columns each with 6 different tile types are correctly identified */
    @Test
    public void verticalTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC10 = new CG_Group(10);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        System.out.println("verticalTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC10.checkConditions(shelf));
    }

    /** Test verticalTest2 checks if 2 columns each with 6 different tile types are correctly identified while the
     *  others are randomly generated each with 5 different tile types */
    @Test
    public void verticalTest2(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC10 = new CG_Group(10);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);
        shelf.insertTiles(1, tiles);

        T_Type[] values = T_Type.values();
        for (int i = 2; i < 5; i++) {
            tiles.clear();
            int[] index = new Random().ints(0, 6).distinct().limit(5).toArray();
            for (int j = 0; j < shelf.ROWS; j++) {
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
            }
            shelf.insertTiles(i, tiles);
        }

        System.out.println("verticalTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC10.checkConditions(shelf));
    }

    /** Test failTest1 checks if checkConditions returns 0 when one column satisfies the card requirements while the
     *  is randomly generated with up to 5 different tile types */
    @Test
    public void failTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC10 = new CG_Group(10);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(0, tiles);

        T_Type[] values = T_Type.values();
        tiles.clear();
        int[] index = new Random().ints(0, 6).distinct().limit(5).toArray();
        for (int j = 0; j < shelf.ROWS; j++) {
            tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
        }
        shelf.insertTiles(1, tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC10.checkConditions(shelf));
    }

    /** Test failTest2 checks if checkConditions returns 0 when one column satisfies the card requirements while the
     *  others are randomly generated each with up to 5 different tile types */
    @Test
    public void failTest2() {

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC10 = new CG_Group(10);

        tiles.add(new Tile(T_Type.PLANT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.BOOK, 1));
        tiles.add(new Tile(T_Type.TROPHY, 1));
        tiles.add(new Tile(T_Type.FRAME, 1));
        shelf.insertTiles(0, tiles);

        T_Type[] values = T_Type.values();
        for (int i = 1; i < 5; i++) {
            tiles.clear();
            int[] index = new Random().ints(0, 6).distinct().limit(5).toArray();
            for (int j = 0; j < shelf.ROWS; j++) {
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
            }
            shelf.insertTiles(i, tiles);
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC10.checkConditions(shelf));
    }

}
