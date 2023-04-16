package it.polimi.ingsw.model.cards;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CG_Shape extends CommonGoalCard {
    private int ID;
    private String type;
    private List<List<Position>> positions;
    int numOfOccurrences;
    private int diffType;
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
                    this.diffType = cardNode.get("diffType").asInt();
                    this.stairs = cardNode.get("stairs").asInt();


                    JsonNode allShapes = cardNode.get("coordinates");
                    for (JsonNode singleShape : allShapes ) {
                        List<Position> dummy = new ArrayList<>();
                        for (JsonNode singleCoordinateNode : singleShape) {
                            int x = singleCoordinateNode.get("x").asInt();
                            int y = singleCoordinateNode.get("y").asInt();
                            dummy.add(new Position(x, y));
                        }
                        this.positions.add(dummy);
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
        // requirements on the common goal card.
        // ex: I've found three cells with a cat and I need the last one to make
        // a 2x2 square, if this last cell is not a cat then the flag is set to 1.
        // When the flag is set to 1 a break is triggered to skip to the next couple
        // and restart the search for a 2x2 square.

        T_Type tile = null; // Saves the current tile type that is going to be searched inside the shelf.

        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        int[] cardsAlreadyChecked = shelf.getCardsAlreadyChecked(); // This array keeps track of all the cards which
        // conditions have already been met inside the player's shelf.

        List<List<Position>> allOccPos = new ArrayList<>(); // allOccPos is a list where each element is a collection
        // of position objects. The collection of position objects corresponds to a shape's occurrence inside the shelf.
        // The single position objects are the coordinates on the board of the shape's occurrence.
        // This way allOccPos saves every shape's occurrence which becomes an element inside its list.
        // Then to access a single position of a shape's occurrence we first iter allOccPos and when we find the occurrence
        // we are interested in we access its positions.
        // When this.numOfOccurrences > 1 we save every occurrence.

        int count = 0; // This flag is used when shapes are overlapping.
        // When a shape is found we take all its positions and run a check on them, we are looking if one of its positions
        // is already saved inside allOccPos.
        // If the position is already inside then we increment count, we do the same for all positions of the current shape found
        // Relevant values: 0 and 1.

        if (cardsAlreadyChecked[this.ID] != 1) { // When set to 1 the check is skipped.


            for (List<Position> singleShape : this.positions) { // Some cards might be mirrored or even rotated by 90°
                // If the card requires to check also the mirrored or rotated shape then like the normal shape its
                // positions are specified inside the json file.
                // While a card might check only one shape it is required to write inside the json file also the
                // positions for the mirrored/rotated version if those are needed to be checked as well.

                for (int k = 0; k < shelfsMatrix.length/2 ; k++) { // In some edge cases it is needed to run the check
                    // multiple times.
                    // shelfsMatrix.length/2 is set in case the length of the shelf needs to be changed.

                    for (T_Type t_type : T_Type.values()) { // 6 tile types have been defined, we search inside the shelf looking
                        // one tile type at the time.
                        tile = t_type; // tile saves the current tile type.


                        // The first 2 for cycles identify a couple (let's call it coupleZero) which becomes the
                        // first piece of the shape described on the card.
                        for (int i = 0; i < shelf.ROWS && cardsAlreadyChecked[this.ID] != 1; i++) {
                            for (int j = 0; j < shelf.COLUMNS && cardsAlreadyChecked[this.ID] != 1; j++) {
                                notFound = 0; // This flag is reset for every new couple allowing to restart the search.
                                count = 0;
                                if (!shelfsMatrix[i][j].getState().equals(State.EMPTY) && shelfsMatrix[i][j].getTile().getTileType().equals(tile)) {
                                    // 1. For a faster check we skip every couple with state == EMPTY.
                                    // 2. For a faster check we skip every couple that doesn't have the same tile type as tile.

                                    for (Position position : singleShape) { // This for cycle adds the coordinates of the current
                                        // shape we are looking for to the coordinates of coupleZero and checks if the
                                        // corresponding couple inside shelfsMatrix is the same type of CoupleZero.

                                        int rowCheck = i + position.getY();
                                        int columnChecK = j + position.getX();
                                        // rowCheck and columnCheck are used to not allow out of bounds indexes inside shelfsMatrix.

                                        if (rowCheck < shelf.ROWS && columnChecK < shelf.COLUMNS && rowCheck >= 0 && columnChecK >= 0 && notFound != 1) {
                                            if (this.stairs != 1) {
                                                if (shelfsMatrix[i][j].getState().equals(State.EMPTY) || shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY)) {
                                                    // If coupleZero or ( coupleZero + position coordinates ) is empty then we skip
                                                    // to the next couple and restart.
                                                    notFound = 1;
                                                    break;
                                                } else if (!shelfsMatrix[i][j].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                                    // If coupleZero and ( coupleZero + position coordinates ) have a different
                                                    // tileType then we skip to the next couple and restart.
                                                    notFound = 1;
                                                    break;
                                                }
                                            } else {
                                                if (rowCheck >= 1) {
                                                    if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) || !( shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY) )) {
                                                        // The only case we don't enter this if statement is when
                                                        // ( coupleZero + position coordinates ) is not empty and the couple above it
                                                        // is empty.
                                                        notFound = 1;
                                                        break;
                                                    }
                                                } else {
                                                    notFound = 1;
                                                    break;
                                                }
                                            }
                                        } else { // If the indexes of ( coupleZero + position coordinates ) are out of bounds
                                            // then we skip to the next couple and restart.
                                            notFound = 1;
                                            break;
                                        }
                                    }

                                    if (notFound != 1) { // We enter this if statement when notFound == 0 which means all
                                        // conditions inside the card have been met, we've found one shape occurrence.
                                        if (this.numOfOccurrences > 1) { // This means the conditions are still to be met.
                                            count = 0; // Reset.

                                            if (allOccPos.size() > 0) { // If at least one shape has been found we enter this if.
                                                List<List<Position>> toRemove = new ArrayList<>();
                                                List<Position> singleShapeToRemove = new ArrayList<>();
                                                for (Position position : singleShape) { // We iter through the coordinates
                                                    // of the shape we have found inside the shelf.
                                                    // We are checking if one of the coordinates is overlapping with another
                                                    // shape's occurrence that has been saved inside allOccPos.
                                                    int y = i + position.getY();
                                                    int x = j + position.getX();
                                                    // x and y give the shape's coordinates inside the shelf.
                                                    for (List<Position> singleShape1 : allOccPos) { // We do this for every shape
                                                        // occurrence that has been saved.
                                                        for (Position position1 : singleShape1) { // We check if the coordinates
                                                            // are overlapping.
                                                            if (x == position1.getX() && y == position1.getY()) {
                                                                count++; // Count is incremented for every overlap.
                                                                singleShapeToRemove = singleShape1; // We save the shape that is overlapping
                                                                // and later decide if it gets eliminated from allOccPos.
                                                            }
                                                        }
                                                    }
                                                }
                                                // When count == 1 it means that inside allOccPos there's a shape that
                                                // overlaps only by one square with the current shape that we have found.
                                                if (count == 1) {
                                                    toRemove.add(singleShapeToRemove);
                                                    allOccPos.removeAll(toRemove); // We remove from allOccPos
                                                    // the shape that is overlapping.
                                                    List<Position> dummy3 = new ArrayList<>();
                                                    for (Position position : singleShape) { // We create a new shape element and
                                                        // save the current shape positions inside it.
                                                        int saveY = i + position.getY();
                                                        int saveX = j + position.getX();
                                                        Position newPosition = new Position(saveX, saveY);
                                                        dummy3.add(newPosition);
                                                    }
                                                    allOccPos.add(dummy3); // This new shape element is then added to allOccPos.
                                                    // The reason behind this is that if two shape's occurrences are
                                                    // sharing only one tile inside the shelf then we can't have both of them
                                                    // inside allOccPos. When such cases appear then the shape that is
                                                    // lower inside the shelf is the one between the two that gets added
                                                    // to allOccPos in the end.
                                                } else if (count == 0) {
                                                    // If count == 0 then we simply create a new shape element and add
                                                    // it to allOccPos.
                                                    List<Position> dummy1 = new ArrayList<>();
                                                    for (Position position : singleShape) {
                                                        int saveY = i + position.getY();
                                                        int saveX = j + position.getX();
                                                        Position newPosition = new Position(saveX, saveY);
                                                        dummy1.add(newPosition);
                                                    }
                                                    allOccPos.add(dummy1);
                                                }
                                            } else { // If allOccPos.size() == 0 then this is the first shape we have found.
                                                List<Position> dummy2 = new ArrayList<>();
                                                for (Position position : singleShape) {
                                                    int saveY = i + position.getY();
                                                    int saveX = j + position.getX();
                                                    Position newPosition = new Position(saveX, saveY);
                                                    dummy2.add(newPosition);
                                                }
                                                allOccPos.add(dummy2);
                                            }

                                            if (allOccPos.size() == this.numOfOccurrences) { // When inside allOccPos all
                                                // the occurrences needed have been added it means the card requirements have been met
                                                cardsAlreadyChecked[this.ID] = 1; // When the conditions are met we save this info.
                                            }

                                        } else if (this.numOfOccurrences == 1) {
                                            cardsAlreadyChecked[this.ID] = 1; // When the conditions are met we save this info.
                                        }
                                    }
                                }
                            }
                        }
                        // diffType == 0 means that the card requires all the shapes to be the same tile type.
                        // allOccPos gets reset so that no shapes with different tile type count towards the card requirements.
                        // ex: If inside the shelf there are three 2x2 squares, one with tile type cat and the other two with tile type book.
                        //     When we first iter looking for cat tiles we need to reset allOccPos at the end because when we
                        //     later iter looking for book tiles we don't want the first cat square to count.
                        //     If allOccPos doesn't get reset then the card requirement gets met with one square made of cat tiles
                        //     and one square made of book tiles, but that's not the card requirement!
                        //     If we reset then when we later look for book tiles the two 2x2 squares with tile type book
                        //     will be correctly found.
                        if (this.diffType == 0) {
                            allOccPos.clear();
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

    public int getDiffType() {
        return this.diffType;
    }

    public int getStairs() {
        return this.stairs;
    }

    public String getType() {
        return this.type;
    }
    public List<List<Position>> getPositions() {
        return this.positions;
    }
    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }
}
