package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.Position;
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
     * Constructor for CG_Shape, given the card ID it finds the corresponding card in CommonGoalCards.json and
     * saves the CGC parameters. For more information on the parameters see the documentation.
     *
     * @param id - Card ID used to identify the card.
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

                    if ( !cardNode.get("type").asText().equals("Shape") ) {
                        throw new IllegalArgumentException("The card ID does not correspond to a card of this type");
                    } else this.type = cardNode.get("type").asText();

                    if ( cardNode.get("numOfOccurrences").asInt() < 1 ) {
                        throw new IllegalArgumentException("The number of occurrences must be greater than 0");
                    } else this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();

                    if ( cardNode.get("diffType").asInt() != 0 && cardNode.get("diffType").asInt() != 1 ) {
                        throw new IllegalArgumentException("The diffType value must be either 0 or 1");
                    } else this.diffType = cardNode.get("diffType").asInt();

                    if ( cardNode.get("surrounded").asInt() != 0 && cardNode.get("surrounded").asInt() != 1
                            && cardNode.get("surrounded").asInt() != 2) {
                        throw new IllegalArgumentException("The surrounded value must be either 0, 1 or 2");
                    } else this.surrounded = cardNode.get("surrounded").asInt();

                    if ( cardNode.get("stairs").asInt() != 0 && cardNode.get("stairs").asInt() != 1 ) {
                        throw new IllegalArgumentException("The stairs value must be either 0 or 1");
                    } else if ( cardNode.get("stairs").asInt() == 1 ) {
                        if ( cardNode.get("surrounded").asInt() != 0 || cardNode.get("numOfOccurrences").asInt() != 1 ) {
                            throw new IllegalArgumentException("The surrounded value must be 0 and the number of occurrences must be 1 for the stairs card");
                        } else this.stairs = cardNode.get("stairs").asInt();
                    }

                    if ( cardNode.get("description").asText().equals("") || cardNode.get("description") == null ) {
                        throw new IllegalArgumentException("The description cannot be empty");
                    } else this.description = cardNode.get("description").asText();


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

                    if ( !this.positions.stream().allMatch(o -> o.stream().anyMatch(e -> (e.getX() == 0 && e.getY() == 0)) ) ) {
                        throw new IllegalArgumentException("The coordinates must contain the (0,0) position");
                    }

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
     * @return - If the card's conditions have been met for the first time it returns 1 else 0.
     */
    @Override
    public int checkConditions(Shelf shelf){

        int found = 0;
        T_Type tile;
        List<List<Position>> allOccPos = new ArrayList<>();

        if ( !shelf.getCardsAlreadyClaimed().contains(this.ID) ) {

            for (T_Type t_type : T_Type.values()) {
                tile = t_type;

                for (List<Position> singleShape : this.positions) {

                    for (int i = 0; i < Shelf.ROWS && found != 1; i++) {
                        for (int j = 0; j < Shelf.COLUMNS; j++) {
                            if (!shelf.getShelfMatrix()[i][j].getState().equals(State.EMPTY) && shelf.getShelfMatrix()[i][j].getTile().getTileType().equals(tile)) {
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

        if ( found == 1 && !shelf.getCardsAlreadyClaimed().contains(this.ID) ) {
            shelf.getCardsAlreadyClaimed().add(this.ID);
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
                    if (shelf.getShelfMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !shelf.getShelfMatrix()[i][j].getTile().getTileType().equals(shelf.getShelfMatrix()[rowCheck][columnCheck].getTile().getTileType())) {
                        return false;
                    }
                } else {
                    if (rowCheck >= 1) {
                        if (shelf.getShelfMatrix()[rowCheck][columnCheck].getState().equals(State.EMPTY) || !( shelf.getShelfMatrix()[rowCheck - 1][columnCheck].getState().equals(State.EMPTY) )) {
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

        for( Position position : dummy ) {


            if (position.getY() - 1 >= 0 && position.getX() >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() < Shelf.COLUMNS) {
                if (!shelf.getShelfMatrix()[position.getY() - 1][position.getX()].getState().equals(State.EMPTY) &&
                        shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfMatrix()[position.getY() - 1][position.getX()].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() ) && ( o.getY() == position.getY() - 1 ))) {
                        return false;
                    }

                }
            }


            if (position.getY() >= 0 && position.getX() + 1 >= 0 && position.getY() < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS) {
                if (!shelf.getShelfMatrix()[position.getY()][position.getX() + 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfMatrix()[position.getY()][position.getX() + 1].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() ))) {
                        return false;
                    }

                }
            }

            if (position.getY() + 1 >= 0 && position.getX() >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() < Shelf.COLUMNS) {
                if (!shelf.getShelfMatrix()[position.getY() + 1][position.getX()].getState().equals(State.EMPTY) &&
                        shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfMatrix()[position.getY() + 1][position.getX()].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() ) && ( o.getY() == position.getY() + 1 ))) {
                        return false;
                    }

                }
            }

            if (position.getY() >= 0 && position.getX() - 1 >= 0 && position.getY() < Shelf.ROWS && position.getX() - 1 < Shelf.COLUMNS) {
                if (!shelf.getShelfMatrix()[position.getY()][position.getX() - 1].getState().equals(State.EMPTY) &&
                        shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                .equals(shelf.getShelfMatrix()[position.getY()][position.getX() - 1].getTile().getTileType())) {
                    if (dummy.stream().noneMatch(o -> ( ( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() ) ))) {
                        return false;
                    }

                }
            }

            // if param surround == 2 then the method checks for the neighbour tiles in the corners
            if (this.surrounded == 2) {

                if (position.getY() - 1 >= 0 && position.getX() - 1 >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() - 1 < Shelf.COLUMNS) {
                    if (!shelf.getShelfMatrix()[position.getY() - 1][position.getX() - 1].getState().equals(State.EMPTY) &&
                            shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfMatrix()[position.getY() - 1][position.getX() - 1].getTile().getTileType())) {
                        if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() - 1 ))) {
                            return false;
                        }

                    }
                }

                if (position.getY() - 1 >= 0 && position.getX() + 1 >= 0 && position.getY() - 1 < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS) {
                    if (!shelf.getShelfMatrix()[position.getY() - 1][position.getX() + 1].getState().equals(State.EMPTY) &&
                            shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfMatrix()[position.getY() - 1][position.getX() + 1].getTile().getTileType())) {
                        if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() - 1 ))) {
                            return false;
                        }

                    }
                }

                if (position.getY() + 1 >= 0 && position.getX() + 1 >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() + 1 < Shelf.COLUMNS) {
                    if (!shelf.getShelfMatrix()[position.getY() + 1][position.getX() + 1].getState().equals(State.EMPTY) &&
                            shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfMatrix()[position.getY() + 1][position.getX() + 1].getTile().getTileType())) {
                        if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() + 1 ) && ( o.getY() == position.getY() + 1 ))) {
                            return false;
                        }

                    }
                }

                if (position.getY() + 1 >= 0 && position.getX() - 1 >= 0 && position.getY() + 1 < Shelf.ROWS && position.getX() - 1 < Shelf.COLUMNS) {
                    if (!shelf.getShelfMatrix()[position.getY() + 1][position.getX() - 1].getState().equals(State.EMPTY) &&
                            shelf.getShelfMatrix()[dummy.get(0).getY()][dummy.get(0).getX()].getTile().getTileType()
                                    .equals(shelf.getShelfMatrix()[position.getY() + 1][position.getX() - 1].getTile().getTileType())) {
                        if (dummy.stream().noneMatch(o -> ( o.getX() == position.getX() - 1 ) && ( o.getY() == position.getY() + 1 ))) {
                            return false;
                        }

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

            if (this.surrounded == 0 ) {
                allOccPos.add(dummy);
            } else if ( surroundCheck(shelf, dummy) ) {
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

    /**
     * Method getID overrides getID in class CommonGoalCard.
     * @return - The ID of the card.
     */
    @Override
    public int getID() {
        return this.ID;
    }

    /**
     * Method getPoints overrides getPoints in class CommonGoalCard.
     * @return - The points remaining on the card.
     */
    @Override
    public Stack<Integer> getPoints() { return this.points; }

    /**
     * Method getDiffType overrides getDiffType in class CommonGoalCard.
     * @return - 1 if the card allows shapes of different types, 0 otherwise.
     */
    @Override
    public int getDiffType() {
        return this.diffType;
    }

    /**
     * Method getDiffColor overrides getDiffColor in class CommonGoalCard.
     * @return - 1 if the card is the 12th CGC on the rulebook, 0 otherwise.
     */
    public int getStairs() {
        return this.stairs;
    }

    /**
     * Method getType overrides getType in class CommonGoalCard.
     * @return - The type of the card.
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Method getPositions overrides getPositions in class CommonGoalCard.
     * @return - The list of lists of positions that identify the shapes.
     */
    @Override
    public List<List<Position>> getPositions() {
        return this.positions;
    }

    /**
     * Method getNumOfOccurrences overrides getNumOfOccurrences in class CommonGoalCard.
     * @return - The number of occurrences needed to satisfy the card's requirements.
     */
    @Override
    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }

    /**
     * Method getSurrounded overrides getSurrounded in class CommonGoalCard.
     * @return - The card description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Method getSurrounded overrides getSurrounded in class CommonGoalCard.
     * @return - An int that represents the card's requirement on the tiles that surround the shape.
     */
    @Override
    public int getSurrounded() {
        return surrounded;
    }
}

