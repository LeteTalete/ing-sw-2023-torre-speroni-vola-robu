package model.cards;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Position;
import model.board.Shelf;
import model.enumerations.Couple;
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
    int numOfOccurrences;
    private int mirror;
    private int stairs;

    /** Constructor for CG_Shape saves the CGC parameters given the card ID
     * id - Card ID used to identify the card
     * type - Shape or Group. In this case Shape
     * numOfOccurrences - Specifies how many occurrences of the same shape are needed to satisfy the card conditions
     * mirror - Default 0. If set to 1 then the shape must be checked both normally and mirrored
     * stairs - Default 0. If set to 1 it identifies the 12th CGC on the rulebook.
     * coordinates - This set of coordinates identifies the shape itself that the card requires
     *             - Those coordinates are saved in positions
    **/

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
                    this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();
                    this.mirror = cardNode.get("mirror").asInt();
                    this.stairs = cardNode.get("stairs").asInt();


                    JsonNode coordinatesNode = cardNode.get("coordinates");
                    for (JsonNode singleCoordinateNode : coordinatesNode) {
                        int x = singleCoordinateNode.get("x").asInt();
                        int y = singleCoordinateNode.get("y").asInt();
                        this.positions.add(new Position(x, y));
                    }

                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

    // wip
    @Override
    public int checkConditions(Shelf shelf){
        int numOfOccurrences = 0;
        int flag1 = 0;
        int flag2 = 0;
        // Deep copy is not necessary as checkCondition needs to modify array checkOccurrence inside player's shelf
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();

        while ( flag1 == 0 ) {

            if ( ( this.mirror == 0 ) && ( this.stairs == 0 ) ) {
                flag1 = 1;
            }

            for (int i = 0; i < shelf.ROWS; i++) {
                for (int j = 0; j < shelf.COLUMNS; j++) {

                    for ( Position position : this.positions ) {
                        int rowCheck = i + position.getX();
                        int columnChecK = j + position.getY();
                        if ( rowCheck < shelf.ROWS && columnChecK < shelf.COLUMNS ) {
                            if (!shelfsMatrix[i][j].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                flag2 = 1;
                                break;
                            }
                        }
                    }
                    if ( flag2 == 1 ){
                        break;
                    } else {
                        numOfOccurrences++;
                    }
                }

            }

            if ( flag2 == 1 ){
                break;
            }
        }

        return numOfOccurrences;
    }

    public int getID() {
        return this.ID;
    }

    public int getMirror() {
        return this.mirror;
    }

    public int getStairs() {
        return this.stairs;
    }

    public String getType() {
        return this.type;
    }
    public List<Position> getPositions() {
        return this.positions;
    }
    public int getNumOfOccurrences() {
        return this.numOfOccurrences;
    }
}
