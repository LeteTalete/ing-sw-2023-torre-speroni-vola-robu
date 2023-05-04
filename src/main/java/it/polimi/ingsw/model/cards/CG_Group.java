package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CG_Group extends CommonGoalCard {
    private int ID;
    private Stack<Integer> points;

    private String type;
    private int numOfOccurrences;
    private int diffUpTo;
    private int horizontal;
    private int vertical;
    private String description;

    /** The constructor given the card ID as a parameter retrieves the card's type from the json
     * file "CommonGoalCards.json"
     *
     * @param id - card ID
     */
    public CG_Group(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_Group.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            this.ID = id;

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    this.type = cardNode.get("type").asText();
                    this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();
                    this.diffUpTo = cardNode.get("diffUpTo").asInt();
                    this.horizontal = cardNode.get("horizontal").asInt();
                    this.vertical = cardNode.get("vertical").asInt();
                    this.description = cardNode.get("description").asText();
                    this.points = new Stack<>();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * Method checkConditions overrides checkConditions in class CommonGoalCards
     * This method is called upon a common goal card and takes as param the shelf of the current player
     * It checks if the card's conditions are met inside the player's shelf
     *
     * @param shelf - The shelf of the current player
     * @return - If the card's conditions have been met it returns 1 else 0
     */
    @Override
    public int checkConditions(Shelf shelf){

        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        List<Integer> cardsAlreadyChecked = shelf.getCardsAlreadyChecked(); // This array keeps track of all the cards which
        // conditions have already been met inside the player's shelf.
        List<T_Type> typesInside = new ArrayList<>(); // This list keeps track of all the tile types that have been found
        // inside the current column/row.
        int flag = 0; // // This flag is set to 1 when one of the couples inside the column/row has the same tile type
        // of one of the tile types in typesInside.
        int found = 0; // When the card requirements are met we set this flag to 1.
        int count = 0; // Keeps track of all the columns/rows that have satisfied the card requirements.
        int notFull = 0; // When set to 1 it means that the current column/row has an empty tile, it triggers a break
        // to the next column/row.

        if ( !cardsAlreadyChecked.contains(this.ID) ) {

            if (this.vertical == 1) {
                for (int i = 0; i < shelf.COLUMNS; i++) {
                    typesInside.clear(); // typesInside gets reset before checking each column.
                    for (int j = 0; j < shelf.ROWS; j++) {
                        notFull = 0; // Reset.
                        flag = 0; // Reset.
                        if (!shelfsMatrix[j][i].getState().equals(State.EMPTY)) {
                            for (T_Type t_type : typesInside) { // We check if the current couple in the column has
                                // the same tile type of one of the tile types in typesInside.
                                if (shelfsMatrix[j][i].getTile().getTileType() == t_type) { // If it does then we don't
                                    // save it again.
                                    flag = 1; // We skip to the next couple inside the column.
                                    break;
                                }
                            }
                            if (flag == 0) { // If the current couple's tile type is different from all the tile types
                                // in typesInside then we save it.
                                typesInside.add(shelfsMatrix[j][i].getTile().getTileType());
                            }
                        } else {
                            notFull = 1; // The current column is not full, break and go to the next one.
                            break;
                        }
                    }
                    if ( notFull == 0 ) { // If the column is full we check if it satisfies the card requirements.
                        if ( this.diffUpTo == 0 && typesInside.size() == T_Type.values().length ) { // When diffUpTo
                            // is equal to 0 it means that the card requires the column to have one of each tile type.
                            // Since the game has 6 tile types then the column must have 6 different tile types.
                            count++; // This column counts towards the card requirements.
                        } else if (typesInside.size() <= this.diffUpTo) { // When diffUpTo != 0 then it means that
                            // diffUpTo is the max number of different tile types inside the column so that the column
                            // counts towards the card requirements.
                            // if diffUpTo = 3 then each column can only have up to 3 different tile types.
                            count++; // This column counts towards the card requirements.
                        }
                    }
                }
            }

            if (this.horizontal == 1) {
                for (int i = 0; i < shelf.ROWS; i++) {
                    typesInside.clear(); // typesInside gets reset before checking each row.
                    for (int j = 0; j < shelf.COLUMNS; j++) {
                        notFull = 0; // Reset.
                        flag = 0; // Reset.
                        if (!shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                            for (T_Type t_type : typesInside) { // We check if the current couple in the row has
                                // the same tile type of one of the tile types in typesInside.
                                if (shelfsMatrix[i][j].getTile().getTileType() == t_type) { // If it does then we don't
                                    // save it again.
                                    flag = 1; // We skip to the next couple inside the row.
                                    break;
                                }
                            }
                            if (flag == 0) { // If the current couple's tile type is different from all the tile types
                                // in typesInside then we save it.
                                typesInside.add(shelfsMatrix[i][j].getTile().getTileType());
                            }
                        } else {
                            notFull = 1; // The current row is not full, break and go to the next one,
                            break;
                        }
                    }
                    if ( notFull == 0 ) { // If the row is full we check if it satisfies the card requirements.
                        if ( this.diffUpTo == 0 && typesInside.size() == T_Type.values().length - 1 ) { // When diffUpTo
                            // is equal to 0 it means that the card requires the row to have 5 different tile types.
                            count++; // This column counts towards the card requirements.
                        } else if (typesInside.size() <= this.diffUpTo) { // When diffUpTo != 0 then it means that
                            // diffUpTo is the max number of different tile types inside the row so that the row
                            // counts towards the card requirements.
                            // If diffUpTo = 3 then each row can only have up to 3 different tile types.
                            count++; // This column counts towards the card requirements.
                        }
                    }
                }
            }

            if ( count >= this.numOfOccurrences ) { // If the number of columns/rows that satisfy the card requirements
                // is equal or greater than the number required then we can conclude that the conditions have been met.
                found = 1;
            }
        }

        if ( found == 1 && !cardsAlreadyChecked.contains(this.ID) ) { // checkConditions returns 1 only when the card requirements
            // are met for the first time.
            cardsAlreadyChecked.add(this.ID);
            return 1;
        } else return 0;
    }

    public String getType() {
        return this.type;
    }

    public int getID() {
        return this.ID;
    }
    public Stack<Integer> getPoints() { return this.points; }


    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }
    public int getDiffUpTo() { return this.diffUpTo;}
    public int getHorizontal() { return this.horizontal;}
    public int getVertical() { return this.vertical;}
    public String getDescription() {
        return this.description;
    }
}
