package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.Deck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LivingRoomTest {
    private Couple[][] testBoard;
    private int numberofplayers = 3; // Possible values 2 - 3 - 4

    // Method readLivingRoomFromJson tests if the json file is read correctly, if the Matrix is the correct size
    // and if deck is initialized
    // Then it tests if every cell has the correct state and tile
    @Test
    public void readLivingRoomFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = LivingRoom.class.getClassLoader().getResourceAsStream("JSON/LivingRoom.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            int[][] jsonMatrixCopy = mapper.convertValue(rootNode.get("LivingRoomBoardJSON"), int[][].class);

            assertNotNull(rootNode);
            assertNotNull(jsonMatrixCopy);
            assertEquals(9, jsonMatrixCopy.length);
            assertEquals(9, jsonMatrixCopy[0].length);

            this.testBoard = new Couple[jsonMatrixCopy.length][jsonMatrixCopy[0].length];
            Deck deck = new Deck();
            int emptyUnusableCheck;

            assertNotNull(deck);

            for (int i = 0; i < jsonMatrixCopy.length; i++) {
                for (int j = 0; j < jsonMatrixCopy[i].length; j++) {

                    if (jsonMatrixCopy[i][j] == 0) {
                        emptyUnusableCheck = 1;
                        Couple couple = new Couple(emptyUnusableCheck);
                        this.testBoard[i][j] = couple;

                        assertEquals(State.INVALID, this.testBoard[i][j].getState());
                        assertNull(this.testBoard[i][j].getTile());

                    } else if (jsonMatrixCopy[i][j] == 2) {
                        Couple couple = new Couple(deck.draw());
                        this.testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, this.testBoard[i][j].getState());
                        assertNotNull(this.testBoard[i][j].getTile());


                    } else if (( jsonMatrixCopy[i][j] == 3 ) && ( numberofplayers >= 3 )) {
                        Couple couple = new Couple(deck.draw());
                        this.testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, this.testBoard[i][j].getState());
                        assertNotNull(this.testBoard[i][j].getTile());


                    } else if (( jsonMatrixCopy[i][j] == 4 ) && ( numberofplayers == 4 )) {
                        Couple couple = new Couple(deck.draw());
                        this.testBoard[i][j] = couple;

                        assertEquals(State.PICKABLE, this.testBoard[i][j].getState());
                        assertNotNull(this.testBoard[i][j].getTile());


                    } else { // If a space is not INVALID and doesn't meet any of the requirements then it set to EMPTY_AND_UNUSABLE (rework needed)
                        emptyUnusableCheck = 0;
                        Couple couple = new Couple(emptyUnusableCheck);
                        this.testBoard[i][j] = couple;

                        assertEquals(State.EMPTY_AND_UNUSABLE, this.testBoard[i][j].getState());
                        assertNull(this.testBoard[i][j].getTile());

                    }

                    // Remove this "if - else" and every System.out if you don't want to print testBoard
                    if (this.testBoard[i][j].getTile() != null) {
                        System.out.print( jsonMatrixCopy[i][j] + " " + this.testBoard[i][j].getState() + " " + this.testBoard[i][j].getTile().getTileType() + ", ");
                    } else {
                        System.out.print( jsonMatrixCopy[i][j] + " " + this.testBoard[i][j].getState() + ", ");
                    }
                }

                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("Error reading matrix from file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void checkPlayerChoiceTest()
    {
        LivingRoom l = new LivingRoom(4);

        l.printBoard();

        ArrayList<Position> choice = new ArrayList<Position>();
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

    @Test
    public void atLeastOneSideFreeTest()
    {
        LivingRoom l = new LivingRoom(4);
        //l.refill(new Deck());

        l.setCouple(new Position(5,5),null,State.EMPTY);

        l.printBoard();
        assertTrue(l.atLeastOneSideFree(new Position(0,4)));
        assertTrue(l.atLeastOneSideFree(new Position(3,8)));
        assertFalse(l.atLeastOneSideFree(new Position(4,4)));
        assertTrue(l.atLeastOneSideFree(new Position(4,8)));
        assertTrue(l.atLeastOneSideFree(new Position(5,6)));
    }

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
}

