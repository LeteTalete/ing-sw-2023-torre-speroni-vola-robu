package it.polimi.ingsw.model.cards;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CG_Shape extends CommonGoalCard {
    private int ID;
    private String type;
    private List<Position> positions;
    int numOfOccurrences;
    private int mirror;
    private int stairs;

    /** Constructor for CG_Shape saves the CGC parameters given the card ID
     * id - Card ID used to identify the card
     * type - Shape or Group. In this case Shape
     * numOfOccurrences - Specifies how many occurrences of the same shape are needed to satisfy the card conditions
     * mirror - Default 0. If set to 1 then the shape must be checked both normally and mirrored
     * stairs - Default 0. If set to 1 it identifies the 12th CGC on the rulebook.
     * coordinates - This set of coordinates identifies the shape itself that the card requires
     *             - Those coordinates are saved in positions
    **/
    public CG_Shape(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_Shape.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            this.ID = id;
            this.positions = new ArrayList<>();

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    this.type = cardNode.get("type").asText();
                    this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();
                    this.mirror = cardNode.get("mirror").asInt();
                    this.stairs = cardNode.get("stairs").asInt();


                    JsonNode coordinatesNode = cardNode.get("coordinates");
                    for (JsonNode singleCoordinateNode : coordinatesNode) {
                        int x = singleCoordinateNode.get("x").asInt();
                        int y = singleCoordinateNode.get("y").asInt();
                        this.positions.add(new Position(x, y));
                    }

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
        int notFound = 0; // This flag is set to 1 when one of the shelf matrix's couple doesn't meet one of the
        // requirements on the common goal card
        // ex: I've found three cells with a cat and I need the last one to make
        // a 2x2 square, if this last cell is not a cat then the flag is set to 1
        // When the flag is set to 1 a break is triggered to skip to the next couple
        // and restart the search for a 2x2 square

        int numOfOccFound = 0; // This flag is only used when the card's param numOfOccurrences > 1
        // It is incremented when an occurrence (shape) is found inside the shelf's matrix

        T_Type tile = null; // Saves the type of the first occurrence for cards with param numOfOccurrences > 1
        // ex: The card that requires two 2x2 square with the same tile type

        // Deep copy is not necessary
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        int[] cardsAlreadyChecked = shelf.getCardsAlreadyChecked(); // This array keeps track of all the cards which
        // conditions have already been met inside the player's shelf

        if (cardsAlreadyChecked[this.ID] != 1) { // When set to 1 the check is skipped

            // The first 2 for cycles identify a couple (let's call it coupleZero) which becomes the
            // first piece of the shape described on the card
            for (int i = 0; i < shelf.ROWS && cardsAlreadyChecked[this.ID] != 1 ; i++) {
                for (int j = 0; j < shelf.COLUMNS && cardsAlreadyChecked[this.ID] != 1 ; j++) {
                    notFound = 0; // This flag is reset for every new couple allowing to restart the search

                    if ( ( i == 0 && j == 0 ) || !shelfsMatrix[i][j].getState().equals(State.EMPTY) ) {
                        if (tile == null || shelfsMatrix[i][j].getTile().getTileType().equals(tile)) {
                            // The check is skipped if none of those conditions are true
                            // Ideally we would skip every empty couple but we need to allow the (0,0) couple regardless
                            // of its contents. This is needed because the 12th card on the rulebook needs the (0,0) couple
                            // to start its check, this couple might be empty so we make an exception
                            // 1. We allow shelfsMatrix[0][0] regardless
                            // 2. For a faster check we skip every couple with state == EMPTY
                            // 3. For every card with param numOfOccurrences == 1 we always have tile == null
                            //    When tile != null then we have a card with numOfOccurrences > 1
                            // 4. For a faster check we skip every couple that doesn't have the same tile type as tile

                            for (Position position : this.positions) { // This for cycle adds the coordinates inside
                                // positions to the coordinates of coupleZero and checks if the corresponding couple inside
                                // shelfsMatrix is the same type of CoupleZero

                                int rowCheck = i + position.getY();
                                int columnChecK = j + position.getX();
                                // rowCheck and columnCheck are used to not allow out of bounds indexes inside shelfsMatrix

                                if (rowCheck < shelf.ROWS && columnChecK < shelf.COLUMNS) {
                                    if (this.stairs != 1) {
                                        if (shelfsMatrix[i][j].getState().equals(State.EMPTY) || shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY)) {
                                            // If coupleZero or ( coupleZero + position coordinates ) is empty then we skip
                                            // to the next couple and restart
                                            notFound = 1;
                                            break;
                                        } else if (!shelfsMatrix[i][j].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                            // If coupleZero and ( coupleZero + position coordinates ) have a different
                                            // tileType then we skip to the next couple and restart
                                            notFound = 1;
                                            break;
                                        }
                                    } else { // When this.stairs == 1 we check for the 12th card on the rulebook
                                        // To find an occurrence of stairs the algorithm needs only the (0,0) couple
                                        // It searches for a not empty diagonal which is shifted one position downwards
                                        // This diagonal represents the "steps" of the stair
                                        // Then it checks if each couple above every step is empty

                                        if (( i == 0 && j == 0 ) && shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                                            // The (0,0) couple must be empty
                                            if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) || !( shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY) )) {
                                                // The only case we don't enter this if statement is when
                                                // ( coupleZero + position coordinates ) is not empty and the couple above it
                                                // is empty
                                                notFound = 1;
                                                break;
                                            }
                                        } else { // When the couple is not (0,0) we skip
                                            notFound = 1;
                                            break;
                                        }
                                    }
                                } else { // If the indexes of ( coupleZero + position coordinates ) are out of bounds
                                    // then we skip to the next couple and restart
                                    notFound = 1;
                                    break;
                                }
                            }
                            if (notFound != 1) { // We enter this if statement when notFound == 0 which means all
                                // conditions inside the card have been met
                                if (this.numOfOccurrences > 1) { // This means the conditions are still to be met
                                    numOfOccFound++;
                                    tile = shelfsMatrix[i][j].getTile().getTileType(); // Saves the tile type for the next
                                    // shape's occurrence
                                    if (numOfOccFound == this.numOfOccurrences) {
                                        cardsAlreadyChecked[this.ID] = 1; //When the conditions are met we save the card id
                                    }
                                } else if (this.numOfOccurrences == 1) {
                                    cardsAlreadyChecked[this.ID] = 1; // When the conditions are met we save the card id
                                }
                            }
                        }
                    }
                }
            }
        }

        // Some cards might be asymmetrical and require an additional check from right to left to see if
        // there is an occurrence inside shelfsMatrix
        // The code is almost identical to the one above, the biggest difference being that it checks shelfsMatrix
        // starting from top right instead of top left
        if (this.mirror == 1 && cardsAlreadyChecked[this.ID] != 1 ) {
            for (int r = 0; r < shelf.ROWS && cardsAlreadyChecked[this.ID] != 1; r++) {
                for (int c = shelf.COLUMNS - 1; c >= 0 && cardsAlreadyChecked[this.ID] != 1; c--) {
                    notFound = 0;

                    if (( r == 0 && c == shelf.COLUMNS - 1 ) || !shelfsMatrix[r][c].getState().equals(State.EMPTY)) {
                        if (tile == null || shelfsMatrix[r][c].getTile().getTileType().equals(tile)) {

                            for (Position position : this.positions) {
                                int rowCheck = r + position.getY();
                                int columnChecK = c - position.getX(); // This allows to mirror the shape
                                if (rowCheck < shelf.ROWS && columnChecK >= 0) {
                                    if (this.stairs != 1) {
                                        if (shelfsMatrix[r][c].getState().equals(State.EMPTY) || shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY)) {
                                            notFound = 1;
                                            break;
                                        } else if (!shelfsMatrix[r][c].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                            notFound = 1;
                                            break;
                                        }
                                    } else {
                                        if (( r == 0 && c == shelf.COLUMNS - 1 ) && shelfsMatrix[r][c].getState().equals(State.EMPTY)) {
                                            if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) || !( shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY) )) {
                                                notFound = 1;
                                                break;
                                            }
                                        } else {
                                            notFound = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    notFound = 1;
                                    break;
                                }
                            }
                            if (notFound != 1) {
                                cardsAlreadyChecked[this.ID] = 1;
                            }
                        }
                    }
                }
            }
        }



        if (cardsAlreadyChecked[this.ID] == 1) {
            return 1;
        } else return 0;
    }

    public int getID() {
        return this.ID;
    }

    public int getMirror() {
        return this.mirror;
    }

    public int getStairs() {
        return this.stairs;
    }

    public String getType() {
        return this.type;
    }
    public List<Position> getPositions() {
        return this.positions;
    }
    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }
}
