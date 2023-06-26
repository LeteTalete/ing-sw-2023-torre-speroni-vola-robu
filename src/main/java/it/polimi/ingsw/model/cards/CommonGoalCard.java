package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class CommonGoalCard extends GoalCard implements Serializable {
    private int ID;

    /**
     * Default constructor for CommonGoalCard.
     */
    public CommonGoalCard(){
    }

    /**
     * Constructor for CommonGoalCard. Sets the card ID.
     * @param id - Card ID used to identify the card.
     */
    public CommonGoalCard(int id){
        this.ID = id;
    }

    /**
     * Method cardType is used to sort the cards into their respective types.
     * The method uses the card ID to find its respective CardType from the json file.
     * Then it initializes the card and returns it.
     */
    public CommonGoalCard cardType(){

        CommonGoalCard dummy = new CommonGoalCard();
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CommonGoalCard.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            String cardType = null;
            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {
                    cardType = cardNode.get("type").asText();
                }
            }

            switch (cardType) {
                case "RowCol":
                    return new CG_RowCol(this.ID);
                case "Shape":
                    return new CG_Shape(this.ID);
                case "Groups":
                    return new CG_Groups(this.ID);
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();
        }

        return dummy;
    }

    public int getID() {
        return 0;
    }

    public int getDiffType() {
        return 0;
    }

    public int getStairs() {
        return 0;
    }

    public String getType() {
        return null;
    }
    public List<List<Position>> getPositions() { return null; }

    public int checkConditions(Shelf shelf){ return 0; }

    public int getNumOfOccurrences() {
        return 0;
    }
    public Stack<Integer> getPoints(){
        return null;
    }

    public int getDiffUpTo() { return 0;}
    public int getHorizontal() { return 0;}
    public int getVertical() { return 0;}

    public String getDescription() {
        return null;
    }

    public int getAtLeast() {
        return 0;
    }
    public int getSurrounded() {
        return 0;
    }
}
