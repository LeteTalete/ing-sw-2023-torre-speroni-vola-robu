package model.cards;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Position;
import model.enumerations.State;
import model.enumerations.Tile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CG_Shape extends CommonGoalCard {
    private int ID;
    private String type;
    private List<Position> positions;
    private int mirror;
    private int sameType;
    private int randomTiles;
    private int stairs;

    // Constructor for CG_Shape saves the CGC parameters given the card ID
    // ID - card ID used to identify the card
    // mirror - Default 0. If set to 1 then the shape must be checked both normally and mirrored
    // sameType - If set to 0 there are no conditions. If set to 1 then the tiles must be of the same type
    // randomTiles - Default 0. If set to anything other than 0 it identifies the 11th CGC on the rulebook.
    //               sameType must be also set to 1.
    //               Ex: if set to 8 the condition becomes at least 8 tiles on the shelf with the same type
    // stairs - Default 0. If set to 1 it identifies the 12th CGC on the rulebook.
    //          mirror must be also set to 1.
    //          Ex: if set to 1 then it checks the shelf for the stairs (normal and mirrored)
    // If the card has stairs or randomTiles set to anything other than 0 then there is no need to save the coordinates
    // as none are written on the json file itself

    public CG_Shape(int id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File("src/main/java/JSONFile/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(jsonFile);

            this.ID = id;
            this.positions = new ArrayList<>();

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {

                    this.type = cardNode.get("type").asText();
                    this.mirror = cardNode.get("mirror").asInt();
                    this.sameType = cardNode.get("sameType").asInt();
                    this.randomTiles = cardNode.get("randomTiles").asInt();
                    this.stairs = cardNode.get("stairs").asInt();

                    if ( ( this.stairs == 0 ) && ( this.randomTiles == 0)) {
                        JsonNode coordinatesNode = cardNode.get("coordinates");
                        for (JsonNode singleCoordinateNode : coordinatesNode) {
                            int x = singleCoordinateNode.get("x").asInt();
                            int y = singleCoordinateNode.get("y").asInt();
                            this.positions.add(new Position(x, y));
                        }
                    }
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

    public int getID() {
        return this.ID;
    }

    public int getMirror() {
        return this.mirror;
    }

    public int getSameType() {
        return this.sameType;
    }

    public int getStairs() {
        return this.stairs;
    }

    public int getRandomTiles() {
        return this.randomTiles;
    }

    public String getType() {
        return this.type;
    }
    public List<Position> getPositions() {
        return this.positions;
    }
}
