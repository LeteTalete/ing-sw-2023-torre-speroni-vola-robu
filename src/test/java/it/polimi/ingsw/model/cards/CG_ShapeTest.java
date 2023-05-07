package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CG_ShapeTest {
    private int ID;
    private String type;
    private List<List<Position>> positions;
    int numOfOccurrences;
    private int diffType;
    private int stairs;
    private String description;

    @Test
    public void constructorTest() {

        int id = 3; // Random shape card chosen

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CG_Shape.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
            JsonNode rootNode = mapper.readTree(inputStream);

            assertNotNull(mapper);
            assertNotNull(inputStream);
            assertNotNull(rootNode);

            this.ID = id;
            assertEquals(3, this.ID);
            this.positions = new ArrayList<>();
            assertNotNull(this.positions);

            for (JsonNode cardNode : rootNode) {
                int cardId = cardNode.get("id").asInt();
                if (cardId == this.ID) {
                    assertEquals(3, cardId);

                    this.type = cardNode.get("type").asText();
                    this.numOfOccurrences = cardNode.get("numOfOccurrences").asInt();
                    this.diffType = cardNode.get("diffType").asInt();
                    this.stairs = cardNode.get("stairs").asInt();
                    this.description = cardNode.get("description").asText();

                    assertEquals("Shape", this.type);
                    assertEquals(1, this.numOfOccurrences);
                    assertEquals(0, this.diffType);
                    assertEquals(0, this.stairs);
                    assertEquals("Five tiles of the same type forming an X.", this.description);


                    JsonNode allShapes = cardNode.get("coordinates");
                    for (JsonNode singleShape : allShapes ) {
                        List<Position> dummy = new ArrayList<>();
                        for (JsonNode singleCoordinateNode : singleShape) {
                            int x = singleCoordinateNode.get("x").asInt();
                            int y = singleCoordinateNode.get("y").asInt();
                            dummy.add(new Position(x, y));
                        }
                        this.positions.add(dummy);
                    }


                    assertEquals(0, this.positions.get(0).get(0).getX());
                    assertEquals(0, this.positions.get(0).get(0).getY());
                    assertEquals(1, this.positions.get(0).get(1).getX());
                    assertEquals(1, this.positions.get(0).get(1).getY());
                    assertEquals(2, this.positions.get(0).get(2).getX());
                    assertEquals(2, this.positions.get(0).get(2).getY());
                    assertEquals(2, this.positions.get(0).get(3).getX());
                    assertEquals(0, this.positions.get(0).get(3).getY());
                    assertEquals(0, this.positions.get(0).get(4).getX());
                    assertEquals(2, this.positions.get(0).get(4).getY());
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

}




