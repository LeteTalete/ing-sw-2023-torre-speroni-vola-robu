package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


public class PersonalGoalCard extends GoalCard {

    private int numPGC;
    private HashMap<Integer, String> positionTilePGC; //Location of personal goal card tiles

    //This Method calculates each player's score of his Personal Card
    public int scorePersonalGoalCard(Shelf myShelf){
        int[] scorePersCard = {0, 1, 2, 3, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int positionRow; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read: positionTilePC
        T_Type typeTile; //Type of Tile read from myShelf
        String[] positionTileColumn; // the Array of String containing the position and the type of Tile to search for in a given column
        Couple[][] shelfCopy = myShelf.getShelfsMatrix();
        for (int column = 0; column < Shelf.COLUMNS; column++) {
            positionTileColumn = this.positionTilePGC.get(column).split(":"); // Split the string to have the tile position and type, separately.
            lenPos = positionTileColumn.length; //To know the number of Tiles that are in a column
            // if the lenPos is not greater than 1, it means that there is no tile to read in that column
            // As the score of the Personal Card depends on how many Tiles are found correct
            for (int index = 0; lenPos > 1 && index < lenPos; index = index + 2) {
                positionRow = Integer.parseInt(positionTileColumn[index]);
                if (shelfCopy[positionRow][column].getState() != State.EMPTY) {
                    typeTile = shelfCopy[positionRow][column].getTile().getTileType();
                    type = positionTileColumn[index + 1].charAt(0);
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
        return scorePersCard[tilesCorrect];
    }


    public PersonalGoalCard(int numPersonalCard) {
        this.numPGC = numPersonalCard;

        String[] positionTileColummn; // the Array of String containing the position and the type of Tile to search for in a given column
        String readJSON;
        this.positionTilePGC = new HashMap<>();
        try{
            //Read JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = PersonalGoalCard.class.getClassLoader().getResourceAsStream("JSON/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            for(int column = 0; column < Shelf.COLUMNS; column++){
                readJSON = cardNode.get(column).asText(); //Save the positions of the Tiles that are on that column
                this.positionTilePGC.put(column, readJSON); // Save the Personal Card
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, String> getPositionTilePC() {
        return this.positionTilePGC;
    }

    public int getNumPGC() {
        return numPGC;
    }

}
