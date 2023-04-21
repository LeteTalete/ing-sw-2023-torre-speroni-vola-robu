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
                    this.points = new Stack<>();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @Override
    public int checkConditions(Shelf shelf){

        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        int[] cardsAlreadyChecked = shelf.getCardsAlreadyChecked();
        List<T_Type> typesInside = new ArrayList<>();
        int flag;
        int count = 0;

        if( this.vertical == 1 ) {
            for ( int i = 0; i < shelf.COLUMNS; i++) {
                typesInside.clear();
                flag = 0;
                for ( int j = 0; j < shelf.ROWS; j++) {
                    if (!shelfsMatrix[j][i].getState().equals(State.EMPTY)) {
                        for (T_Type t_type : typesInside) {
                            if (shelfsMatrix[j][i].getTile().getTileType() == t_type) {
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            typesInside.add(shelfsMatrix[j][i].getTile().getTileType());
                        }
                    } else {
                        break;
                    }
                }
                if (typesInside.size() == this.diffUpTo) {
                    count++;
                }
            }
        }

        if (this.horizontal == 1 ) {
            for ( int i = 0; i < shelf.ROWS; i++) {
                for ( int j = 0; j < shelf.COLUMNS; j++) {

                }
            }
        }
        return 0;
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
}
