package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.GoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


class PersonalGoalCardTest extends GoalCard{

    @Test
    public void scoreTest() {
        ThreadLocalRandom numRandom = ThreadLocalRandom.current();
        int[] scorePersCard = {0, 1, 2, 3, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int positionRow; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read from JSON File
        int numPersonalCard = numRandom.nextInt(1, 12); //Randomly choose the personal card
        T_Type printTile;

        T_Type typeTile; //Type of Tile read from myShelf
        String[] positionTileColummn; // the Array of String containing the position and the type of Tile to search for in a given column
        String readJSON;
        Shelf myShelf = new Shelf();
        Deck deck = new Deck();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Integer> numRandomRow = new ArrayList<>(); // It will contain the number of boxes in a shelf column that should be left empty, for each row

        System.out.println("Personal card number chosen:" + numPersonalCard);
        System.out.println("My Shelf:");
        //To create the shelf, randomly, and so that it is not necessarily completely filled and print it on terminal
        for (int row = 0; row < myShelf.ROWS; row++) {
            System.out.print(((int) row + 1) + "   |");
            for (int col = 0; col < myShelf.COLUMNS; col++) {
                if (row == 0) {
                    /*
                    Is the number of boxes in a column of the shelf that must be left empty
                    The random number of rows to leave empty has a range that must be from 0 to a maximum of 6,
                    to ensure that the shelf does not risk being too empty, I have chosen a value from 0 to 3
                    */
                    numRandomRow.add(numRandom.nextInt(0, 3));
                }
                Couple couple = new Couple(deck.draw());
                myShelf.setCoordinate(row, col, couple);
                int numR = numRandomRow.get(col);
                /*
                If numR were to be equal to 0 then it would mean that there is no longer any box on the shelf of that
                column to be kept empty but the remaining boxes must be filled with tiles. The code not only sets the
                shelf to test but prints it to me on the terminal. This makes it easier to check the test
                */
                if (numR == 0) {
                    printTile = myShelf.getCoordinate(row, col).getTile().getTileType();
                    if (printTile == T_Type.CAT) {
                        System.out.print(" C |");
                    } else if (printTile == T_Type.BOOK) {
                        System.out.print(" B |");
                    } else if (printTile == T_Type.GAMES) {
                        System.out.print(" G |");
                    } else if (printTile == T_Type.PLANT) {
                        System.out.print(" P |");
                    } else if (printTile == T_Type.FRAME) {
                        System.out.print(" F |");
                    } else if (printTile == T_Type.TROPHY) {
                        System.out.print(" T |");
                    }
                } else {
                    numRandomRow.set(col, numR - 1);
                    System.out.print("   |");
                }
            }
            System.out.println();
        }
        System.out.println("      1   2   3   4   5  ");

        assertTrue(numPersonalCard > 0 && numPersonalCard <= 12);
        assertNotNull(myShelf);
        Couple[][] shelfCopy = myShelf.getShelfsMatrix();
        try {
            //Read JSON file
            InputStream inputStream = model.cards.PersonalGoalCard.class.getClassLoader().getResourceAsStream("JSON/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            assertNotNull(cardNode);
            assertNotNull(rootNode);
            for (int column = 0; column < 5; column++) {
                // As the score of the Personal Card depends on how many Tiles are found correct
                readJSON = cardNode.get(column).asText(); //Save the positions of the Tiles that are on that column
                assertNotNull(readJSON);
                positionTileColummn = readJSON.split(":"); // Split the string to have the tile position and type, separately.
                lenPos = positionTileColummn.length; //To know the number of Tiles that are in a column
                assertNotEquals(0, lenPos);
                // If the lenPos is not greater than 1, it means that there is no tile to read in that column
                for (int index = 0; lenPos > 1 && index < lenPos; index = index + 2) {
                    positionRow = Integer.parseInt(positionTileColummn[index]);
                    assertNotNull(positionRow);
                    if (shelfCopy[positionRow][column].getState() != State.EMPTY) {
                        typeTile = shelfCopy[positionRow][column].getTile().getTileType();
                        type = positionTileColummn[index + 1].charAt(0);
                        if (typeTile == T_Type.CAT && type == 'C') {
                            tilesCorrect++;
                        } else if (typeTile == T_Type.PLANT && type == 'P') {
                            tilesCorrect++;
                        } else if (typeTile == T_Type.FRAME && type == 'F') {
                            tilesCorrect++;
                        } else if (typeTile == T_Type.TROPHY && type == 'T') {
                            tilesCorrect++;
                        } else if (typeTile == T_Type.BOOK && type == 'B') {
                            tilesCorrect++;
                        } else if (typeTile == T_Type.GAMES && type == 'G') {
                            tilesCorrect++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Number tile found: " + tilesCorrect);
        //the value of the tiles found cannot be greater than 6
        assertTrue(tilesCorrect >= 0 && tilesCorrect <= 6);
        System.out.print("Score: " + scorePersCard[tilesCorrect]);
    }
}