package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.board.LivingRoom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class CG_Group extends CommonGoalCard {
    private int ID;
    private String type;


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
}
