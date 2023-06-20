package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.enumerations.State;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class CG_Groups extends CommonGoalCard implements Serializable {
    private int ID;
    private Stack<Integer> points;
    private String type;
    private int numOfOccurrences;
    private int atLeast;
    private String description;

    /**
     * Constructor for CG_Groups given the card ID reads CommonGoalCards.json and saves the CGC parameters.
     *
     * @param id - Card ID used to identify the card.
     * type - The card's type.
     * numOfOccurrences - Specifies how many occurrences of the group are needed to satisfy the card conditions.
     * atLeast - Specifies the least number of tiles needed to identify an eligible group.
     * description - The card description.
     */
    public CG_Groups(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_Shape.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            this.ID = id;
            this.points = new Stack<>();

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    if ( !cardNode.get("type").asText().equals("Groups") ) {
                        throw new IllegalArgumentException("The card ID does not correspond to a card of this type");
                    } else this.type = cardNode.get("type").asText();

                    if ( cardNode.get("numOfOccurrences").asInt() < 1 ) {
                        throw new IllegalArgumentException("The number of occurrences must be at least 1");
                    } else this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();

                    if ( cardNode.get("atLeast").asInt() < 1 ) {
                        throw new IllegalArgumentException("The number of tiles must be at least 1");
                    } else this.atLeast = cardNode.get("atLeast").asInt();

                    if ( cardNode.get("description").asText().equals("") || cardNode.get("description") == null ) {
                        throw new IllegalArgumentException("The card must have a description");
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
     * @return - If the card's conditions have been met for the first time it returns 1 else 0.
     */
    @Override
    public int checkConditions(Shelf shelf) {

        int x, y;
        ArrayList<Position> group;
        ArrayList<ArrayList<Position>> groups = new ArrayList<>();
        ArrayList<Couple> visited = new ArrayList<>();

        if (!shelf.getCardsAlreadyClaimed().contains(this.ID)) {

            for (int i = 0; i < Shelf.ROWS; i++) {
                for (int j = 0; j < Shelf.COLUMNS; j++) {
                    if (( shelf.getShelfMatrix()[i][j].getState() == State.PICKABLE ) && ( !visited.contains(shelf.getShelfMatrix()[i][j]) )) {
                        group = new ArrayList<>();
                        group.add(new Position(i, j));
                        visited.add(shelf.getShelfMatrix()[i][j]);
                        for (int k = 0; k < group.size(); k++) {
                            x = group.get(k).getX();
                            y = group.get(k).getY();

                            if (y + 1 < Shelf.COLUMNS) {
                                if (( shelf.getCoordinate(x, y + 1).getState() == State.PICKABLE ) &&
                                        ( shelf.getCoordinate(x, y + 1).getTile().getTileType() == shelf.getCoordinate(x, y).getTile().getTileType() ) &&
                                        ( !visited.contains(shelf.getCoordinate(x, y + 1)) )) {
                                    group.add(new Position(x, y + 1));
                                    visited.add(shelf.getShelfMatrix()[x][y + 1]);
                                }
                            }
                            if (x + 1 < Shelf.ROWS) {
                                if (( shelf.getCoordinate(x + 1, y).getState() == State.PICKABLE ) &&
                                        ( shelf.getCoordinate(x + 1, y).getTile().getTileType() == shelf.getCoordinate(x, y).getTile().getTileType() ) &&
                                        ( !visited.contains(shelf.getCoordinate(x + 1, y)) )) {
                                    group.add(new Position(x + 1, y));
                                    visited.add(shelf.getShelfMatrix()[x + 1][y]);
                                }
                            }
                            if (y - 1 >= 0) {
                                if (( shelf.getCoordinate(x, y - 1).getState() == State.PICKABLE ) &&
                                        ( shelf.getCoordinate(x, y - 1).getTile().getTileType() == shelf.getCoordinate(x, y).getTile().getTileType() ) &&
                                        ( !visited.contains(shelf.getCoordinate(x, y - 1)) )) {
                                    group.add(new Position(x, y - 1));
                                    visited.add(shelf.getShelfMatrix()[x][y - 1]);
                                }
                            }
                            if (x - 1 >= 0) {
                                if (( shelf.getCoordinate(x - 1, y).getState() == State.PICKABLE ) &&
                                        ( shelf.getCoordinate(x - 1, y).getTile().getTileType() == shelf.getCoordinate(x, y).getTile().getTileType() ) &&
                                        ( !visited.contains(shelf.getCoordinate(x - 1, y)) )) {
                                    group.add(new Position(x - 1, y));
                                    visited.add(shelf.getShelfMatrix()[x - 1][y]);
                                }
                            }
                        }
                        if (group.size() >= this.atLeast) {
                            groups.add(group);
                        }
                    } else {
                        visited.add(shelf.getShelfMatrix()[i][j]);
                    }
                }
            }
        }


        if ( groups.size() >= this.numOfOccurrences && !shelf.getCardsAlreadyClaimed().contains(this.ID) ) {
            shelf.getCardsAlreadyClaimed().add(this.ID);
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public Stack<Integer> getPoints() {
        return points;
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getAtLeast() {
        return atLeast;
    }
    @Override
    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }
    @Override
    public String getDescription() {
        return description;
    }

}
