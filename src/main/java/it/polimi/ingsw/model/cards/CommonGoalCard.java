package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommonGoalCard extends GoalCard {
    private int ID;


    public CommonGoalCard(){
    }

    public CommonGoalCard(int id){
        this.ID = id;
    }

    // Method typeGroupOrShape is used to sort the cards into their respective types ( Group card or Shape card )
    // The method uses the card ID to find its respective CardType from the json file
    // Then it initializes the card and returns it
    // CommonGoalCard dummy is a null card, it won't be ever returned and its purpose is to eliminate the warning
    public CommonGoalCard typeGroupOrShape(){

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

            if (cardType.equals("Group")) {
                CG_Group card = new CG_Group(this.ID);
                return card;
            } else if (cardType.equals("Shape")) {
                CG_Shape card = new CG_Shape(this.ID);
                return card;
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
}
