package model.cards;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonalGoalCard extends GoalCard {
    private ArrayList<String> positionCards;
    public void setPositionCards(int numPersonalCard){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/java/JSONFile/PersonalGoals.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode cardNode = null;
            switch (numPersonalCard){
                case 1:
                    cardNode = rootNode.get("PersonalGoals1");
                    break;
                case 2:
                    cardNode = rootNode.get("PersonalGoals2");
                    break;
                case 3:

            }
            for (JsonNode node : cardNode) {
                this.positionCards.add(node.fields().next().getValue().asText());
            }


        } catch (IOException e){
            e.printStackTrace();
        }

        return returnValue;
    }




}
