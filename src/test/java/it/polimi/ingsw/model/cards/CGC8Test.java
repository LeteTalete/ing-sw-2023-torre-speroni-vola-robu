package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CGC8Test {

    /** Test createCardTest creates an instance of CGC8 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Group CGC8 = new CG_Group(8);
        assertEquals(8, CGC8.getID());
        assertEquals("Group", CGC8.getType());
        assertEquals(3, CGC8.getNumOfOccurrences());
        assertEquals(3, CGC8.getDiffUpTo());
        assertEquals(1, CGC8.getVertical());
        assertEquals(0, CGC8.getHorizontal());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Group CGC8 = new CG_Group(8);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /** Test threeVerticalTest1 checks if 3 randomly generated columns with up to 3 different tile types
     * are correctly identified */
    @Test
    public void threeVerticalTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        T_Type[] values = T_Type.values();
        for (int i = 0; i < 3; i++) {
            tiles.clear();
            int[] index = new Random().ints(0, 6).distinct().limit(3).toArray();
            for (int j = 0; j < shelf.ROWS; j++) {
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
            }
            shelf.insertTiles(i, tiles);
        }

        System.out.println("threeVerticalTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC8.checkConditions(shelf));
    }

    /** Test threeVerticalTest2 checks if 3 randomly generated columns with up to 2 different tile types
     * are correctly identified */
    @Test
    public void threeVerticalTest2(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        T_Type[] values = T_Type.values();
        for (int i = 0; i < 3; i++) {
            tiles.clear();
            int[] index = new Random().ints(0, 6).distinct().limit(2).toArray();
            for (int j = 0; j < shelf.ROWS; j++) {
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
            }
            shelf.insertTiles(i, tiles);
        }

        System.out.println("threeVerticalTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC8.checkConditions(shelf));
    }

    /** Test threeVerticalTest3 checks if 3 randomly generated columns each made of one tile type
     *  are correctly identified */
    @Test
    public void threeVerticalTest3(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        T_Type[] values = T_Type.values();

        for ( int i = 0; i < 3; i++) {
            tiles.clear();
            int ind = new Random().nextInt((values.length));
            for (int j = 0; j < shelf.ROWS; j++) {
                tiles.add(new Tile(values[ind], 1));
            }
            shelf.insertTiles(i, tiles);
        }


        System.out.println("threeVerticalTest3");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC8.checkConditions(shelf));
    }

    /** Test threeVerticalTest4 checks if 3 randomly generated columns are correctly identified when:
     *  - The first one is generated with up to 3 different tile types
     *  - The second one in made of one tile type
     *  - The third one is generated with up to 2 different tile types */
    @Test
    public void threeVerticalTest4(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        T_Type[] values = T_Type.values();

        tiles.clear();
        int[] index = new Random().ints(0, 6).distinct().limit(3).toArray();
        for (int j = 0; j < shelf.ROWS; j++) {
            tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
        }
        shelf.insertTiles(0, tiles);

        tiles.clear();
        index = new Random().ints(0, 6).distinct().limit(2).toArray();
        for (int j = 0; j < shelf.ROWS; j++) {
            tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
        }
        shelf.insertTiles(2, tiles);

        tiles.clear();
        int ind = new Random().nextInt((values.length));
        for (int j = 0; j < shelf.ROWS; j++) {
            tiles.add(new Tile(values[ind], 1));
        }
        shelf.insertTiles(1, tiles);


        System.out.println("threeVerticalTest4");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC8.checkConditions(shelf));
    }

    /** Test failTest1 checks if checkConditions returns 0 when 2 columns follow the requirements on the card but
     *  the third one doesn't */
    @Test
    public void failTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(4,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2,tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);

        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC8.checkConditions(shelf));
    }

    /** Test failTest2 checks if checkConditions returns 0 when 2 columns (0 and 4) follow the requirements on the card
     * but none of the others do while each of them is generated randomly with 6 different tile types */
    @Test
    public void failTest2(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC8 = new CG_Group(8);

        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(0,tiles);
        shelf.insertTiles(4,tiles);

        T_Type[] values = T_Type.values();
        for (int i = 1; i < 4; i++) {
            tiles.clear();
            int[] index = new Random().ints(0, 6).distinct().limit(6).toArray();
            for (int ind : index) {
                tiles.add(new Tile(values[ind], 1));
            }
            shelf.insertTiles(i, tiles);
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC8.checkConditions(shelf));
    }


}
