package model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.GoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


public class PersonalGoalCard extends GoalCard {
    //This Method calculates each player's score of his Personal Card
    public int scorePersonalGoalCard(Shelf myShelf, int numPersonalCard){
        int[] scorePersCard = {0, 1, 2, 3, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int positionRow; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read from JSON File
        T_Type typeTile; //Type of Tile read from myShelf
        ObjectMapper objectMapper = new ObjectMapper();
        String[] positionTileColummn; // the Array of String containing the position and the type of Tile to search for in a given column
        String readJSON;
        Couple[][] shelfCopy = myShelf.getShelfsMatrix();
        try {
            //Read JSON file
            InputStream inputStream = PersonalGoalCard.class.getClassLoader().getResourceAsStream("JSON/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            for (int column = 0; column < 5; column++) {
                // As the score of the Personal Card depends on how many Tiles are found correct
                readJSON = cardNode.get(column).asText(); //Save the positions of the Tiles that are on that column
                positionTileColummn = readJSON.split(":"); // Split the string to have the tile position and type, separately.
                lenPos = positionTileColummn.length; //To know the number of Tiles that are in a column
                // if the lenPos is not greater than 1, it means that there is no tile to read in that column
                for (int index = 0; lenPos > 1 && index < lenPos; index = index + 2) {
                    positionRow = Integer.parseInt(positionTileColummn[index]);
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
        return scorePersCard[tilesCorrect];
    }
}
