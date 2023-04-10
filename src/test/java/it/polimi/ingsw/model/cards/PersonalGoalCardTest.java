package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.GoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class PersonalGoalCardTest extends GoalCard{

    @Test
    public int scorePersonalGoalCard(Couple[][] myShelf, int numPersonalCard) {
        assertTrue(numPersonalCard > 0 && numPersonalCard <= 12);
        assertNotNull(myShelf);
        String[] positionTileColummn; // the Array of String containing the position and the type of Tile to search for in a given column
        int[] scorePersCard = {0, 1, 2, 3, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int row; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read from JSON File
        T_Type typeTile; //Type of Tile read from myShelf
        ArrayList<String> positionCards = new ArrayList<String>(); //ArrayList used to save player personal card, read from JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //Read JSON file
            File jsonFile = new File("src/main/java/JSONFile/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            assertNotNull(cardNode);
            //Save the values in the Variable: positionCards
            for (JsonNode node : cardNode) {
                positionCards.add(node.fields().next().getValue().asText());
            }
            assertNotNull(positionCards);
            for(int column = 0; column < 6; column++){
                // As the score of the Personal Card depends on how many Tiles are found correct
                positionTileColummn = positionCards.get(column).split(":");
                assertNotNull(positionTileColummn);
                lenPos = positionTileColummn.length;
                assertNotEquals(0, lenPos);
                // if the lenPos is not greater than 1, it means that there is no tile to read in that column
                for(int index = 0; lenPos > 1 && index < lenPos; index = index + 2){
                    row = Integer.parseInt(positionTileColummn[index]);
                    if(myShelf[row][column].getState() != State.EMPTY){
                        typeTile = myShelf[row][column].getTile().getTileType();
                        type = positionTileColummn[index + 1].charAt(0);
                        if(typeTile == T_Type.CAT && type == 'C'){
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
        } catch (IOException e){
            e.printStackTrace();
        }
        return scorePersCard[tilesCorrect];
    }
}