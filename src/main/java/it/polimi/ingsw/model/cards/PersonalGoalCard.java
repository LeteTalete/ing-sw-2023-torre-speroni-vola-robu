package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Couple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class PersonalGoalCard extends GoalCard {
    //This Method calculates each player's score of his Personal Card
    public int scorePersonalGoalCard(Shelf shelf, int numPersonalCard){
        Couple[][] myShelf = shelf.getShelfsMatrix();
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
            InputStream inputStream = PersonalGoalCard.class.getClassLoader().getResourceAsStream("JSON/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode cardNode = rootNode.get(String.valueOf(numPersonalCard));
            //Save the values in the Variable: positionCards
            for (JsonNode node : cardNode) {
                positionCards.add(node.fields().next().getValue().asText());
            }
            for(int column = 0; column < 6; column++){
                // As the score of the Personal Card depends on how many Tiles are found correct
                positionTileColummn = positionCards.get(column).split(":");
                lenPos = positionTileColummn.length;
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
