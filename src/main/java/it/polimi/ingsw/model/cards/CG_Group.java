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
    public CG_Group(int id, int numOfPlayers) {

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
                    if ( numOfPlayers == 2 ){
                        this.points.push(4);
                        this.points.push(8);
                    } else if ( numOfPlayers == 3) {
                        this.points.push(4);
                        this.points.push(6);
                        this.points.push(8);
                    } else if ( numOfPlayers == 4 ) {
                        this.points.push(2);
                        this.points.push(4);
                        this.points.push(6);
                        this.points.push(8);
                    }
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
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
