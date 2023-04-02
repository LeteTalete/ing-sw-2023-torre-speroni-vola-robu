package model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

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
            File jsonFile = new File("src/main/java/JSONFile/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(jsonFile);

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


    public void checkConditions(){
    }

}
