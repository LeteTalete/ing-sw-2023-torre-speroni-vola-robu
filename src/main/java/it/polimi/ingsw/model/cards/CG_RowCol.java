package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CG_RowCol extends CommonGoalCard implements Serializable {
    private int ID;
    private Stack<Integer> points;

    private String type;
    private int numOfOccurrences;
    private int diffUpTo;
    private int horizontal;
    private int vertical;
    private String description;

    /**
     * Constructor for CG_RowCol saves the CGC parameters given the card ID.
     *
     * @param id - Card ID used to identify the card.
     * type - The card's type.
     * numOfOccurrences - Specifies how many occurrences of the same shape are needed to satisfy the card conditions.
     * diffUpTo - Given a row or a column it specifies the maximum number of different tile types inside it.
     * horizontal - When set to 1 it means the card asks for rows.
     * vertical - When set to 1 it means the card asks for columns.
     * description - The card description.
     */
    public CG_RowCol(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_RowCol.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            this.ID = id;
            this.points = new Stack<>();

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    if ( !cardNode.get("type").asText().equals("RowCol") ) {
                        throw new IllegalArgumentException("The card ID does not correspond to a card of this type");
                    } else this.type = cardNode.get("type").asText();

                    if ( cardNode.get("numOfOccurrences").asInt() < 1 ) {
                        throw new IllegalArgumentException("The number of occurrences must be greater than 0");
                    } else this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();

                    if ( cardNode.get("diffUpTo").asInt() < 0 || cardNode.get("diffUpTo").asInt() > T_Type.values().length ) {
                        throw new IllegalArgumentException("The number of different tile types must be between 0 and " + T_Type.values().length);
                    } else this.diffUpTo = cardNode.get("diffUpTo").asInt();

                    if ( cardNode.get("diffUpTo").asInt() == 0 ){
                        if (Shelf.COLUMNS > T_Type.values().length || Shelf.ROWS > T_Type.values().length) {
                            throw new IllegalArgumentException("The number of tile types (enum) must be equal or less to max(ROWS, COLUMNS) or vice-versa");
                        }
                    }

                    if ( cardNode.get("horizontal").asInt() != 0 && cardNode.get("horizontal").asInt() != 1 ) {
                        throw new IllegalArgumentException("The horizontal value must be either 0 or 1");
                    } else this.horizontal = cardNode.get("horizontal").asInt();

                    if ( cardNode.get("vertical").asInt() != 0 && cardNode.get("vertical").asInt() != 1 ) {
                        throw new IllegalArgumentException("The vertical value must be either 0 or 1");
                    } else this.vertical = cardNode.get("vertical").asInt();

                    if ( cardNode.get("description").asText().equals("") || cardNode.get("description") == null ) {
                        throw new IllegalArgumentException("The description cannot be empty");
                    } else this.description = cardNode.get("description").asText();

                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method checkConditions overrides checkConditions in class CommonGoalCards.
     * This method is called upon a common goal card and takes as param the shelf of the current player.
     * It checks if the card's conditions are met inside the player's shelf.
     * @param shelf - The shelf of the current player.
     * @return - If the card's conditions have been met it returns 1 else 0.
     */
    @Override
    public int checkConditions(Shelf shelf){

        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        List<Integer> cardsAlreadyChecked = shelf.getCardsAlreadyChecked(); // This array keeps track of all the cards which
        // conditions have already been met inside the player's shelf.
        List<T_Type> typesInside = new ArrayList<>(); // This list keeps track of all the tile types that have been found
        // inside the current column/row.
        int flag; // // This flag is set to 1 when one of the couples inside the column/row has the same tile type
        // of one of the tile types in typesInside.
        int found = 0; // When the card requirements are met we set this flag to 1.
        int count = 0; // Keeps track of all the columns/rows that have satisfied the card requirements.
        int Full = 0; // When set to 1 it means that the current column/row has an empty tile, it triggers a break
        // to the next column/row.

        if ( !cardsAlreadyChecked.contains(this.ID) ) {

            if (this.vertical == 1) {
                for (int i = 0; i < Shelf.COLUMNS; i++) {
                    typesInside.clear(); // typesInside gets reset before checking each column.
                    for (int j = 0; j < Shelf.ROWS; j++) {
                        Full = 0; // Reset.
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
                            Full = 1; // The current column is not full, break and go to the next one.
                            break;
                        }
                    }
                    if ( Full == 0 ) { // If the column is full we check if it satisfies the card requirements.
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
                for (int i = 0; i < Shelf.ROWS; i++) {
                    typesInside.clear(); // typesInside gets reset before checking each row.
                    for (int j = 0; j < Shelf.COLUMNS; j++) {
                        Full = 0; // Reset.
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
                            Full = 1; // The current row is not full, break and go to the next one,
                            break;
                        }
                    }
                    if ( Full == 0 ) { // If the row is full we check if it satisfies the card requirements.
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
