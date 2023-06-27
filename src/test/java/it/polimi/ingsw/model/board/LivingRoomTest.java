package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class LivingRoomTest {

    /**
     * Method readLivingRoomFromJson tests if the json file is read correctly, if the Matrix is the correct size
     * and if deck is initialized.
     * Then it tests if every cell has the correct state and if the tile is null or not.
     */
    @Test
    public void readLivingRoomFromJson() {
        Couple[][] testBoard;
        int numOfPlayers = new Random().nextInt(2,5);

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = LivingRoom.class.getClassLoader().getResourceAsStream("JSON/LivingRoom.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            int[][] jsonMatrixCopy = mapper.convertValue(rootNode.get("LivingRoomBoardJSON"), int[][].class);

            assertNotNull(rootNode);
            assertNotNull(jsonMatrixCopy);
            assertEquals(9, jsonMatrixCopy.length);
            assertEquals(9, jsonMatrixCopy[0].length);

            testBoard = new Couple[jsonMatrixCopy.length][jsonMatrixCopy[0].length];
            Deck deck = new Deck();

            assertNotNull(deck);

            for (int i = 0; i < jsonMatrixCopy.length; i++) {
                for (int j = 0; j < jsonMatrixCopy[i].length; j++) {

                    if (jsonMatrixCopy[i][j] == 0) {
                        Couple couple = new Couple();
                        couple.setState(State.INVALID);
                        testBoard[i][j] = couple;

                        assertEquals(State.INVALID, testBoard[i][j].getState());
                        assertNull(testBoard[i][j].getTile());

                    } else if (jsonMatrixCopy[i][j] == 2) {
                        Couple couple = new Couple(deck.draw());
                        testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, testBoard[i][j].getState());
                        assertNotNull(testBoard[i][j].getTile());


                    } else if (( jsonMatrixCopy[i][j] == 3 ) && ( numOfPlayers >= 3 )) {
                        Couple couple = new Couple(deck.draw());
                        testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, testBoard[i][j].getState());
                        assertNotNull(testBoard[i][j].getTile());


                    } else if (( jsonMatrixCopy[i][j] == 4 ) && ( numOfPlayers == 4 )) {
                        Couple couple = new Couple(deck.draw());
                        testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, testBoard[i][j].getState());
                        assertNotNull(testBoard[i][j].getTile());


                    } else {
                        Couple couple = new Couple();
                        couple.setState(State.INVALID);
                        testBoard[i][j] = couple;

                        assertEquals(State.INVALID, testBoard[i][j].getState());
                        assertNull(testBoard[i][j].getTile());

                    }
                }

                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("Error reading matrix from file: " + e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * Method checkPlayerChoiceTest tests if the positions of tiles chosen by the player are valid
     */
    @Test
    public void checkPlayerChoiceTest()
    {
        LivingRoom l = new LivingRoom(4);

        l.printBoard();

        ArrayList<Position> choice = new ArrayList<>();
        choice.add(new Position(0,0));

        assertFalse(l.checkPlayerChoice(choice));

        choice.clear();
        choice.add(new Position(4,4));
        assertFalse(l.checkPlayerChoice(choice));
        choice.add(new Position(4,5));
        assertFalse(l.checkPlayerChoice(choice));

        choice.clear();
        choice.add(new Position(0,3));
        assertTrue(l.checkPlayerChoice(choice));
        choice.add(new Position(0,4));
        assertTrue(l.checkPlayerChoice(choice));

        choice.clear();
        choice.add(new Position(0,3));
        choice.add(new Position(4,4));
        assertFalse(l.checkPlayerChoice(choice));

        choice.clear();
        choice.add(new Position(0,3));
        choice.add(new Position(1,3));
        assertTrue(l.checkPlayerChoice(choice));
        choice.add(new Position(2,3));
        assertFalse(l.checkPlayerChoice(choice));

    }


    /**
     * Method atLeastOneSideFreeTest tests if a position in the game board containing a tile has at least one
     * adjacent position which does not contain a tile
     */
    @Test
    public void atLeastOneSideFreeTest()
    {
        LivingRoom l = new LivingRoom(4);

        l.setCouple(new Position(5,5),null,State.EMPTY);

        l.printBoard();
        assertTrue(l.atLeastOneSideFree(new Position(0,4)));
        assertTrue(l.atLeastOneSideFree(new Position(3,8)));
        assertFalse(l.atLeastOneSideFree(new Position(4,4)));
        assertTrue(l.atLeastOneSideFree(new Position(4,8)));
        assertTrue(l.atLeastOneSideFree(new Position(5,6)));
    }


    /**
     * Method checkForRefillTest tests if the game board needs to be refilled, it happens when there are not
     * at least two adjacent positions containing a tile
     */
    @Test
    public void checkForRefillTest()
    {
        LivingRoom l = new LivingRoom(4);

        assertFalse(l.checkForRefill());

        l.clearBoard();
        l.printBoard();

        assertTrue(l.checkForRefill());
        l.setCouple(new Position(4,4),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertTrue(l.checkForRefill());
        l.setCouple(new Position(5,5),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertTrue(l.checkForRefill());
        l.setCouple(new Position(5,6),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertFalse(l.checkForRefill());
    }

    /**
     * Method refillTest tests if refill method correctly refills the board.
     */
    @Test
    public void refillTest()
    {
        LivingRoom livingRoom = new LivingRoom(4);

        assertFalse(livingRoom.checkForRefill());
        livingRoom.clearBoard();
        livingRoom.printBoard();
        assertTrue(livingRoom.checkForRefill());
        livingRoom.refill();
        livingRoom.printBoard();
        assertFalse(livingRoom.checkForRefill());
    }

    /**
     * Method updateCouplesTest tests if updateCouples correctly deletes the couples from the board.
     */
    @Test
    public void updateCouplesTest(){
        LivingRoom livingRoom = new LivingRoom(4);

        livingRoom.setCouple(new Position(4,4),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertEquals(State.PICKABLE,livingRoom.getCouple(new Position(4,4)).getState());
        assertEquals(T_Type.CAT,livingRoom.getCouple(new Position(4,4)).getTile().getTileType());

        livingRoom.setCouple(new Position(4,5),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertEquals(State.PICKABLE,livingRoom.getCouple(new Position(4,5)).getState());
        assertEquals(T_Type.CAT,livingRoom.getCouple(new Position(4,5)).getTile().getTileType());

        livingRoom.setCouple(new Position(4,6),new Tile(T_Type.CAT,1),State.PICKABLE);
        assertEquals(State.PICKABLE,livingRoom.getCouple(new Position(4,6)).getState());
        assertEquals(T_Type.CAT,livingRoom.getCouple(new Position(4,6)).getTile().getTileType());

        livingRoom.printBoard();

        ArrayList<Position> choice = new ArrayList<>();
        choice.add(new Position(4,4));
        choice.add(new Position(4,5));
        choice.add(new Position(4,6));
        livingRoom.updateCouples(choice);

        livingRoom.printBoard();

        assertEquals(State.EMPTY,livingRoom.getCouple(new Position(4,4)).getState());
        assertEquals(State.EMPTY,livingRoom.getCouple(new Position(4,5)).getState());
        assertEquals(State.EMPTY,livingRoom.getCouple(new Position(4,6)).getState());
    }

    /**
     * Method getBoardTest tests if getBoard method returns the correct board.
     */
    @Test
    public void getBoardTest(){
        LivingRoom livingRoom = new LivingRoom(4);
        assertNotNull(livingRoom.getBoard());
        assertEquals(9, livingRoom.getBoard().length);
        assertEquals(9, livingRoom.getBoard()[0].length);
    }
}

