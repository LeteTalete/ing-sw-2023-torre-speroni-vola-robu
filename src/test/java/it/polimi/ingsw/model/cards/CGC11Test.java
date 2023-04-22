package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CGC11Test {

    /** Test createCardTest creates an instance of CGC11 and checks if all parameters are correct */
    @Test
    public void createCardTest(){
        CG_Group CGC11 = new CG_Group(11);
        assertEquals(11, CGC11.getID());
        assertEquals("Group", CGC11.getType());
        assertEquals(2, CGC11.getNumOfOccurrences());
        assertEquals(0, CGC11.getDiffUpTo());
        assertEquals(0, CGC11.getVertical());
        assertEquals(1, CGC11.getHorizontal());

    }

    /** Test emptyShelfTest checks that the card is not accepted when the shelf is empty */
    @Test
    public void emptyShelfTest(){
        Shelf shelf = new Shelf();
        CG_Group CGC11 = new CG_Group(11);

        System.out.println("emptyShelfTest");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC11.checkConditions(shelf));
    }

    /** Test horizontalTest1 checks if 2 rows each with 5 different tile types are correctly identified */
    @Test
    public void horizontalTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC11 = new CG_Group(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        System.out.println("horizontalTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC11.checkConditions(shelf));
    }

    /** Test horizontalTest2 checks if 2 rows each with 5 different tile types are correctly identified while the others
     *  are randomly generated each with up to 4 different tile types */
    @Test
    public void horizontalTest2(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC11 = new CG_Group(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        T_Type[] values = T_Type.values();
        for ( int i = 0; i < 5; i++ ){
            int[] index = new Random().ints(0, 6).distinct().limit(4).toArray();
            for ( int j = 0; j < 5; j++) {
                tiles.clear();
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
                shelf.insertTiles(j, tiles);
            }
        }

        System.out.println("horizontalTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(1, CGC11.checkConditions(shelf));
    }

    /** Test failTest1 checks if checkConditions returns 0 when one row satisfies the card requirements while the
     *  other one is randomly generated with up to 4 different tile types */
    @Test
    public void failTest1(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC11 = new CG_Group(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        T_Type[] values = T_Type.values();
            int[] index = new Random().ints(0, 6).distinct().limit(4).toArray();
            for ( int j = 0; j < 5; j++) {
                tiles.clear();
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
                shelf.insertTiles(j, tiles);
            }


        System.out.println("failTest1");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC11.checkConditions(shelf));
    }

    /** Test failTest2 checks if checkConditions returns 0 when one row satisfies the card requirements while the
     *  others are randomly generated each with up to 4 different tile types */
    @Test
    public void failTest2(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();
        CG_Group CGC11 = new CG_Group(11);

        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(1, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(2, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(4, tiles);

        T_Type[] values = T_Type.values();
        for ( int i = 0; i < 5; i++ ) {
            int[] index = new Random().ints(0, 6).distinct().limit(4).toArray();
            for (int j = 0; j < 5; j++) {
                tiles.clear();
                tiles.add(new Tile(values[index[new Random().nextInt(index.length)]], 1));
                shelf.insertTiles(j, tiles);
            }
        }

        System.out.println("failTest2");
        shelf.printShelf();
        System.out.println();

        assertEquals(0, CGC11.checkConditions(shelf));
    }


}
