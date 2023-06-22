package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


public class PersonalGoalCard extends GoalCard implements Serializable {

    private int numPGC;
    private ArrayList<String> positionTilePGC; //Location of personal goal card tiles

    /**
     * Constructor PersonalGoalCard creates a new PersonalGoalCard instance, given the number of the card it finds
     * the corresponding card in PersonalGoals.json and saves PGC parameters. For more information on the parameters
     * see the documentation. //TODO: Davide should write the documentation
     * @param numPersonalCard - the number of the Personal Goal Card.
     */
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

    /**
     * Method scorePersonalGoalCard given the Shelf of the player it calculates how many points the player has earned
     * with his Personal Goal Card.
     * @param myShelf - the Shelf of the player.
     * @return - the number of points earned by the player.
     */
    public int scorePersonalGoalCard(Shelf myShelf){
        int[] scorePersCard = {0, 1, 2, 4, 6, 9, 12}; // the Score Table
        int tilesCorrect = 0; //Number of correct Tiles found
        int positionColumn; // Position of the tile (row) in which to search it.
        int lenPos; // length of positionTileColummn,
        char type; // Type of Tile to search, read: positionTilePC
        T_Type typeTile; //Type of Tile read from myShelf
        String[] positionTileRow; // the Array of String containing the position and the type of Tile to search for in a given row
        Couple[][] shelfCopy = myShelf.getShelfMatrix();
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

    /**
     * Method getPositionTilePC returns information about the position of the tiles in the personal goal card.
     * @return - the position of the tiles in the personal goal card.
     */
    public ArrayList<String> getPositionTilePC() {
        return this.positionTilePGC;
    }

    /**
     * Method getNumPGC returns the number of the Personal Goal Card.
     * @return - the number of the Personal Goal Card.
     */
    public int getNumPGC() {
        return numPGC;
    }

}
