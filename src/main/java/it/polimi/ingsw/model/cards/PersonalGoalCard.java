package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


public class PersonalGoalCard extends GoalCard {

    private int numPGC;
    private ArrayList<String> positionTilePGC; //Location of personal goal card tiles

    //This Method calculates each player's score of his Personal Card
    public int scorePersonalGoalCard(Shelf myShelf){
        int[] scorePersCard = {0, 1, 2, 4, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int positionColumn; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read: positionTilePC
        T_Type typeTile; //Type of Tile read from myShelf
        String[] positionTileRow; // the Array of String containing the position and the type of Tile to search for in a given row
        Couple[][] shelfCopy = myShelf.getShelfsMatrix();
        for (int row = 0; row < Shelf.ROWS; row++) {
            positionTileRow = this.positionTilePGC.get(row).split(":"); // Split the string to have the tile position and type, separately.
            lenPos = positionTileRow.length; //To know the number of Tiles that are in a row
            // if the lenPos is not greater than 1, it means that there is no tile to read in that row
            // As the score of the Personal Card depends on how many Tiles are found correct
            for (int index = 0; lenPos > 1 && index < lenPos; index = index + 2) {
                positionColumn = Integer.parseInt(positionTileRow[index]);
                if (shelfCopy[row][positionColumn].getState() != State.EMPTY) {
                    typeTile = shelfCopy[row][positionColumn].getTile().getTileType();
                    type = positionTileRow[index + 1].charAt(0);
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
        this.positionTilePGC = new ArrayList<>();
        try{
            //Read JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = PersonalGoalCard.class.getClassLoader().getResourceAsStream("JSON/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            for(int row = 0; row < Shelf.ROWS; row++){
                readJSON = cardNode.get(row).asText(); //Save the positions of the Tiles that are on that column
                this.positionTilePGC.add(readJSON);// Save the Personal Card
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPositionTilePC() {
        return this.positionTilePGC;
    }

    public int getNumPGC() {
        return numPGC;
    }

}
