package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CG_Shape extends CommonGoalCard implements Serializable {
    private int ID;
    private Stack<Integer> points;
    private String type;
    private List<List<Position>> positions;
    private int numOfOccurrences;
    private int diffType;
    private int surrounded;
    private int stairs;
    private String description;

    /**
     * Constructor for CG_Shape saves the CGC parameters given the card ID.
     *
     * @param id - Card ID used to identify the card.
     * type - The card's type.
     * numOfOccurrences - Specifies how many occurrences of the same shape are needed to satisfy the card conditions.
     * diffType - When set to 0 all shape's occurrences must be of the same tile type.
     *            When set to 1 each of the shape's occurrences can be of a different tile type from the others.
     * surrounded - When set to 0 neighbour tiles to the shape occurrence can share the same tile type with it.
     *              When set to 1 neighbour tiles to the shape occurrence must have a different tile type from it.
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
                    this.surrounded = cardNode.get("surrounded").asInt();
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

                    for (int i = 0; i < Shelf.ROWS && found != 1; i++) {
                        for (int j = 0; j < Shelf.COLUMNS; j++) {
                            if (!shelf.getShelfsMatrix()[i][j].getState().equals(State.EMPTY) && shelf.getShelfsMatrix()[i][j].getTile().getTileType().equals(tile)) {
                                if ( shapeFinder(shelf, singleShape, i, j) && requirementsCheck(shelf, allOccPos, singleShape, i, j) ) {
                                    found = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (found == 1) {
                        break;
                    }
                }
                if (found == 1) {
                    break;
                }
                if (this.diffType == 0) {
                    allOccPos.clear();
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
    public boolean shapeFinder(Shelf shelf, List<Position> singleShape, int i, int j){

        for (Position position : singleShape) {
            int rowCheck = i + position.getY();
            int columnCheck = j + position.getX();

            if (rowCheck < Shelf.ROWS && columnCheck < Shelf.COLUMNS && rowCheck >= 0 && columnCheck >= 0 ) {
                if (this.stairs != 1) {
                    if (shelf.getShelfsMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !shelf.getShelfsMatrix()[i][j].getTile().getTileType().equals(shelf.getShelfsMatrix()[rowCheck][columnCheck].getTile().getTileType())) {
                        return false;
                    }
                } else {
                    if (rowCheck >= 1) {
                        if (shelf.getShelfsMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !( shelf.getShelfsMatrix()[rowCheck - 1][columnCheck].getState().equals(State.EMPTY) )) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Method surroundCheck checks if any of the neighbour tiles of the current shape occurrence inside the shelf
     * share the same tile type with the shape occurrence.
     * @param shelf - The shelf of the current player.
     * @param dummy - A list of coordinates that represents the current shape's positions inside the shelf.
     * @return - The method returns true if none of the neighbour tiles have the same tile type of the shape, false otherwise.
     */
    public boolean surroundCheck(Shelf shelf,List<Position> dummy){

        for( Position position : dummy ){

            /*
            if ( position.getY() - 1 >= 0 && position.getX() - 1 >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() - 1 < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() - 1][position.getX() - 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY() - 1][position.getX() - 1].getTile().getTileType())) {
                    if ( !dummy.stream().anyMatch(o -> ( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() - 1 )) ) {
                        return false;
                    }

                }
            }
            */

            if ( position.getY() - 1 >= 0 && position.getX() >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() - 1][position.getX()].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY() - 1][position.getX()].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX()) && ( o.getY() == position.getY() - 1 ))) {
                        return false;
                    }

                }
            }

            /*
            if ( position.getY() - 1 >= 0 && position.getX() + 1 >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() - 1][position.getX() + 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY() - 1][position.getX() + 1].getTile().getTileType())) {
                    if ( !dummy.stream().anyMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() - 1 )) ) {
                        return false;
                    }

                }
            }
             */

            if ( position.getY() >= 0 && position.getX() + 1 >= 0 && position.getY() < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY()][position.getX() + 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY()][position.getX() + 1].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() ))) {
                        return false;
                    }

                }
            }

            /*
            if ( position.getY() + 1 >= 0 && position.getX() + 1 >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() + 1][position.getX() + 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfsMatrix()[position.getY() + 1][position.getX() + 1].getTile().getTileType())) {
                    if ( !dummy.stream().anyMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() + 1 )) ) {
                        return false;
                    }

                }
            }
            */

            if ( position.getY() + 1 >= 0 && position.getX() >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() + 1][position.getX()].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY() + 1][position.getX()].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() ) && ( o.getY() == position.getY() + 1 ))) {
                        return false;
                    }

                }
            }

            /*
            if ( position.getY() + 1 >= 0 && position.getX() - 1 >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() - 1< Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY() + 1][position.getX() - 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfsMatrix()[position.getY() + 1][position.getX() - 1].getTile().getTileType())) {
                    if ( !dummy.stream().anyMatch(o -> ( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() + 1 )) ) {
                        return false;
                    }

                }
            }
            */

            if ( position.getY() >= 0 && position.getX() - 1 >= 0 && position.getY() < Shelf.ROWS && position.getX() - 1 < Shelf.COLUMNS ) {
                if ( !shelf.getShelfsMatrix()[position.getY()][position.getX() - 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfsMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfsMatrix()[position.getY()][position.getX() - 1].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> (( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() )) )) {
                        return false;
                    }

                }
            }

        }

        return true;
    }

    /**
     * Method requirementsCheck sorts the logic.
     * @param shelf - The player's shelf.
     * @param allOccPos - List of Lists of Positions where each list identifies a shape occurrence inside the shelf.
     * @param singleShape - A list of coordinates that identifies a shape.
     * @param i - Current couple y coordinate.
     * @param j - Current couple x coordinate.
     * @return - The method returns 1 if the card requirements have been met, 0 otherwise.
     */
    public boolean requirementsCheck(Shelf shelf, List<List<Position>> allOccPos, List<Position> singleShape, int i, int j) {

        // To allow an easier check in method surroundCheck a dummy is created.
        // This dummy saves the current shape's positions inside the shelf.
        List<Position> dummy = saveShape(singleShape, i, j);

        if ( this.numOfOccurrences == 1 ) {
            if ( this.surrounded == 0 ) {
                return true;
            } else {
                if ( surroundCheck(shelf, dummy) ){
                    return true;
                }
            }

        } else {

            if (this.surrounded == 1 && surroundCheck(shelf, dummy)) {
                allOccPos.add(dummy);
            } else if (this.surrounded == 0) {
                allOccPos.add(dummy);
            }

            if (allOccPos.size() >= this.numOfOccurrences) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method savShape is an auxiliary method used to save a shape occurrence.
     * The shape coordinates (positions) saved are the ones that the shape has inside the shelf.
     * @param singleShape -  A list of coordinates (positions) that identifies a shape.
     * @param i - Current couple y coordinate.
     * @param j - Current couple x coordinate.
     */
    public List<Position> saveShape(List<Position> singleShape, int i, int j){

        List<Position> dummy = new ArrayList<>();
        for (Position position : singleShape) {
            dummy.add(new Position(position.getX() + j, position.getY() + i));
        }
        return dummy;
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
    public int getSurrounded() {
        return surrounded;
    }
}

