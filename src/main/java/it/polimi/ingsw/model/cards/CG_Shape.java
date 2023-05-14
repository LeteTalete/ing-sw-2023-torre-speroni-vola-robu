package it.polimi.ingsw.model.cards;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CG_Shape extends CommonGoalCard {
    private int ID;
    private Stack<Integer> points;
    private String type;
    private List<List<Position>> positions;
    private int numOfOccurrences;
    private int diffType;
    private int stairs;
    private String description;

    /**
     * Constructor for CG_Shape saves the CGC parameters given the card ID.
     * id - Card ID used to identify the card.
     * type - Shape or Group. In this case Shape.
     * numOfOccurrences - Specifies how many occurrences of the same shape are needed to satisfy the card conditions.
     * diffType - When set to 0 all the shape's occurrences must be of the same tile type.
     *            When set to 1 each of the shape's occurrences can be of a different tile type from the others.
     * stairs - Default 0. If set to 1 it identifies the 12th CGC on the rulebook.
     * description - The card description.
     * coordinates - This set of coordinates identifies the shape itself that the card requires.
     *             - Those coordinates are saved in positions.
     */
    public CG_Shape(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_Shape.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            this.ID = id;
            this.positions = new ArrayList<>();
            this.points = new Stack<>();

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    this.type = cardNode.get("type").asText();
                    this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();
                    this.diffType = cardNode.get("diffType").asInt();
                    this.stairs = cardNode.get("stairs").asInt();
                    this.description = cardNode.get("description").asText();

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
     * Method checkConditions overrides checkConditions in class CommonGoalCards.
     * This method is called upon a common goal card and takes as param the shelf of the current player.
     * It checks if the card's conditions are met inside the player's shelf.
     * @param shelf - The shelf of the current player.
     * @return - If the card's conditions have been met for the first time it returns 1 else 0.
     */
    @Override
    public int checkConditions(Shelf shelf){

        int found = 0;
        T_Type tile;
        List<List<Position>> allOccPos = new ArrayList<>();

        if ( !shelf.getCardsAlreadyChecked().contains(this.ID) ) {

            for (T_Type t_type : T_Type.values()) {
                tile = t_type;

                for (List<Position> singleShape : this.positions) {

                    for (int i = 0; i < Shelf.ROWS ; i++) {
                        for (int j = 0; j < Shelf.COLUMNS ; j++) {
                            if (!shelf.getShelfsMatrix()[i][j].getState().equals(State.EMPTY) && shelf.getShelfsMatrix()[i][j].getTile().getTileType().equals(tile)) {
                                if (shapeFinder(shelf, singleShape, i, j) == 1) {
                                    addShape(allOccPos, singleShape, i, j);
                                }
                            }
                        }
                    }
                }

                if (requirementsCheck(allOccPos) == 1) {
                    found = 1;
                    break;
                }
            }
        }

        if ( found == 1 && !shelf.getCardsAlreadyChecked().contains(this.ID) ) {
            shelf.getCardsAlreadyChecked().add(this.ID);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Method shapeFinder is called upon all eligible couples inside the shelf.
     * It adds the coordinates inside singleShape to the ones of the couple which the method was called upon.
     * If all the couples tile type match then the shape has been found.
     * @param shelf - The shelf of the current player.
     * @param singleShape - A list of coordinates that identifies a shape.
     * @param i - Current couple y coordinate.
     * @param j - Current couple x coordinate.
     * @return - The method returns 1 if the shape is found, 0 otherwise.
     */
    public int shapeFinder(Shelf shelf, List<Position> singleShape, int i, int j){

        for (Position position : singleShape) {
            int rowCheck = i + position.getY();
            int columnCheck = j + position.getX();

            if (rowCheck < Shelf.ROWS && columnCheck < Shelf.COLUMNS && rowCheck >= 0 && columnCheck >= 0 ) {
                if (this.stairs != 1) {
                    if (shelf.getShelfsMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !shelf.getShelfsMatrix()[i][j].getTile().getTileType().equals(shelf.getShelfsMatrix()[rowCheck][columnCheck].getTile().getTileType())) {
                        return 0;
                    }
                } else {
                    if (rowCheck >= 1) {
                        if (shelf.getShelfsMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !( shelf.getShelfsMatrix()[rowCheck - 1][columnCheck].getState().equals(State.EMPTY) )) {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            } else {
                return 0;
            }
        }
        return 1;
    }

    /**
     * Method duplicateDeleter searches allOccPos and deletes all shape's occurrences that overlap inside the shelf.
     * Let 'size' be the number of cells that a shape occupies inside the shelf.
     * Starting to delete from the shapes that completely overlap with others (num of cells that overlap == size) it then
     * decreases and keeps deleting the shapes that overlap with all cells but one (size - 1) and so on until it stops
     * before size reaches 0. At the end allOccPos will contain only shapes that don't overlap inside the shelf.
     * @param allOccPos - List of Lists of Positions where each list identifies a shape occurrence inside the shelf.
     */
    public void duplicateDeleter(List<List<Position>> allOccPos) {

        int count;
        List<Position> singleShapeToRemove = new ArrayList<>();
        int size = this.positions.get(0).size();

        while (size != 0) {
            for (int i = 0; i < allOccPos.size(); i++) {
                for (List<Position> singleShape1 : allOccPos) {
                    count = 0;
                    for (List<Position> singleShape2 : allOccPos) {
                        if (!singleShape1.equals(singleShape2)) {
                            for (Position position1 : singleShape1) {
                                for (Position position2 : singleShape2) {
                                    if (position1.getY() == position2.getY() && position1.getX() == position2.getX()) {
                                        count++;
                                        break;
                                    }
                                }
                            }
                        }
                        if (count == size) {
                            break;
                        }
                    }
                    if (count == size) {
                        singleShapeToRemove = singleShape1;
                        break;
                    }
                }
                if ( !singleShapeToRemove.isEmpty() ) {
                    allOccPos.remove(singleShapeToRemove);
                    singleShapeToRemove.clear();
                }
            }
            size--;
        }
    }

    /**
     * Method occAdjuster fixes miscounts. When a card allows a shape to be rotated it may happen that duplicateDeleter
     * wrongfully deletes a shape occurrence inside allOccPos (this is due to the order in which shapes are saved
     * inside allOccPos). After duplicateDeleter method occAdjuster compares the shapes inside allOccPosCopy
     * to the ones in allOccPos, if a shape inside allOccPosCopy doesn't overlap with any shape occurrence in allOccPos
     * then that shape can be readded to allOccPos.
     * @param allOccPos - List of non overlapping shapes inside the shelf.
     * @param allOccPosCopy - Copy of allOccPos before duplicateDeleter is called.
     */
    public void occAdjuster(List<List<Position>> allOccPos, List<List<Position>> allOccPosCopy ){

        if ( this.positions.size() > 1 ) {
            for (List<Position> singleShape1 : allOccPosCopy) {
                int count = 0;
                for (List<Position> singleShape2 : allOccPos) {
                    for (Position position1 : singleShape1) {
                        for (Position position2 : singleShape2) {
                            if (position1.getY() == position2.getY() && position1.getX() == position2.getX()) {
                                count++;
                                break;
                            }
                        }
                    }
                }
                if (count == 0) {
                    allOccPos.add(singleShape1);
                }
            }
        }

    }

    /**
     * Method requirementsCheck sorts the logic. Most notably when diffType == 0 if the card requirements have not been
     * met for the current tile type then allOccPoss is cleared. If diffType == 1 then allOccPos is preserved as all
     * shape occurrences can have a different tile type from the others.
     * @param allOccPos - List of Lists of Positions where each list identifies a shape occurrence inside the shelf.
     * @return - The method returns 1 if the card requirements have been met, 0 otherwise.
     */
    public int requirementsCheck(List<List<Position>> allOccPos){

        if (this.numOfOccurrences == 1) {
            if (allOccPos.size() >= 1) {
                return 1;
            }
        } else {
            if (diffType == 0) {
                List<List<Position>> allOccPosCopy = cloneList(allOccPos);
                duplicateDeleter(allOccPos);
                occAdjuster(allOccPos, allOccPosCopy);
                if (allOccPos.size() == this.numOfOccurrences) {
                    return 1;
                } else {
                    allOccPos.clear();
                }
            } else if (diffType == 1) {
                List<List<Position>> allOccPosCopy = cloneList(allOccPos);
                duplicateDeleter(allOccPos);
                occAdjuster(allOccPos, allOccPosCopy);
                if (allOccPos.size() == this.numOfOccurrences) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Method addShape is an auxiliary method used to add a shape occurrence in allOccPos.
     * The shape coordinates (positions) saved are the ones that the shape has inside the shelf.
     * @param allOccPos - List of Lists of Positions where each list identifies a shape occurrence inside the shelf.
     * @param singleShape -  A list of coordinates (positions) that identifies a shape.
     * @param i - Current couple y coordinate.
     * @param j - Current couple x coordinate.
     */
    public void addShape(List<List<Position>> allOccPos, List<Position> singleShape, int i, int j){

        List<Position> dummy = new ArrayList<>();
        for (Position position : singleShape) {
            dummy.add(new Position(position.getX() + j, position.getY() + i));
        }
        allOccPos.add(dummy);
    }

    /**
     * Method cloneList is an auxiliary method used to make a deep copy of allOccPos before duplicateDeleter is called.
     * @param allOccPos - List of shapes that might be overlapping inside the shelf.
     * @return - It returns a deep copy of allOccPos.
     */
    public List<List<Position>> cloneList(List<List<Position>> allOccPos){
        List<List<Position>> allOccPosCopy = new ArrayList<>();
        for( List<Position> sublist : allOccPos) {
            allOccPosCopy.add(new ArrayList<>(sublist));
        }
        return allOccPosCopy;
    }

    public int getID() {
        return this.ID;
    }

    public Stack<Integer> getPoints() { return this.points; }
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
    public String getDescription() {
        return description;
    }
}

