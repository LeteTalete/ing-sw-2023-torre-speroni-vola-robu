package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
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
    private List<Position> positions;
    int numOfOccurrences;
    private int mirror;
    private int stairs;

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
                    this.mirror = cardNode.get("mirror").asInt();
                    this.stairs = cardNode.get("stairs").asInt();

                    assertEquals("Shape", this.type);
                    assertEquals(1, this.numOfOccurrences);
                    assertEquals(0, this.mirror);
                    assertEquals(0, this.stairs);

                    JsonNode coordinatesNode = cardNode.get("coordinates");
                    assertNotNull(coordinatesNode);
                    for (JsonNode singleCoordinateNode : coordinatesNode) {
                        assertNotNull(singleCoordinateNode);
                        int x = singleCoordinateNode.get("x").asInt();
                        int y = singleCoordinateNode.get("y").asInt();
                        this.positions.add(new Position(x, y));
                    }

                    assertEquals(0, this.positions.get(0).getX());
                    assertEquals(0, this.positions.get(0).getY());
                    assertEquals(1, this.positions.get(1).getX());
                    assertEquals(1, this.positions.get(1).getY());
                    assertEquals(2, this.positions.get(2).getX());
                    assertEquals(2, this.positions.get(2).getY());
                    assertEquals(2, this.positions.get(3).getX());
                    assertEquals(0, this.positions.get(3).getY());
                    assertEquals(0, this.positions.get(4).getX());
                    assertEquals(2, this.positions.get(4).getY());
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @Test
    public void checkConditionsTest() {

        this.ID = 4;
        this.type = "Shape";
        this.numOfOccurrences = 1;
        this.mirror = 1;
        this.stairs = 1;
        this.positions = new ArrayList<>();
        assertNotNull(this.positions);
        this.positions.add(new Position(0, 1));
        this.positions.add(new Position(1, 2));
        this.positions.add(new Position(2, 3));
        this.positions.add(new Position(3, 4));
        this.positions.add(new Position(4, 5));

        Shelf shelf = new Shelf();
        assertNotNull(shelf);
        Deck deck = new Deck();
        assertNotNull(deck);
        int notFound = 0;
        int numOfOccFound = 0;
        T_Type tile = null;
        Couple[][] shelfsMatrix = shelf.getShelfsMatrix();
        assertNotNull(shelfsMatrix);
        int[] cardsAlreadyChecked = shelf.getCardsAlreadyChecked();
        assertNotNull(cardsAlreadyChecked);

        for (int i = 0; i < shelf.ROWS; i++) {
            for (int j = 0; j < shelf.COLUMNS; j++) {
                if ( i + j >= 5 ){
                    Tile tile0 = new Tile(T_Type.CAT, 3);
                    Couple couple0 = new Couple(tile0);
                    shelf.setCoordinate(i, j, couple0);
                    assertEquals(couple0.getTile(), shelfsMatrix[i][j].getTile());
                    assertEquals(couple0.getState(), shelfsMatrix[i][j].getState());
                }
            }
        }

        if (cardsAlreadyChecked[this.ID] != 1) {
            assertEquals(0,cardsAlreadyChecked[this.ID]);

            for (int i = 0; i < shelf.ROWS && cardsAlreadyChecked[this.ID] != 1; i++) {
                for (int j = 0; j < shelf.COLUMNS && cardsAlreadyChecked[this.ID] != 1; j++) {
                    notFound = 0;

                    if (( i == 0 && j == 0 ) || !shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                        if (tile == null || shelfsMatrix[i][j].getTile().getTileType().equals(tile)) {

                            for (Position position : this.positions) {
                                assertNotNull(position);
                                int rowCheck = i + position.getY();
                                int columnChecK = j + position.getX();
                                if (rowCheck < shelf.ROWS && columnChecK < shelf.COLUMNS) {
                                    if (this.stairs != 1) {
                                        if (shelfsMatrix[i][j].getState().equals(State.EMPTY) || shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY)) {
                                            if ( shelfsMatrix[i][j].getState().equals(State.EMPTY) ) {
                                                assertEquals(State.EMPTY, shelfsMatrix[i][j].getState());
                                            } else if ( shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) ) {
                                                assertEquals(State.EMPTY, shelfsMatrix[rowCheck][columnChecK].getState());
                                            }
                                            notFound = 1;
                                            break;
                                        } else if (!shelfsMatrix[i][j].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                            assertNotEquals(shelfsMatrix[i][j].getTile().getTileType(), shelfsMatrix[rowCheck][columnChecK].getTile().getTileType());
                                            notFound = 1;
                                            break;
                                        }
                                    } else {
                                        if (( i == 0 && j == 0 ) && shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                                            assertEquals(State.EMPTY, shelfsMatrix[i][j].getState());
                                            if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) || !( shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY) )) {
                                                if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) ) {
                                                    assertEquals(State.EMPTY, shelfsMatrix[rowCheck][columnChecK].getState());
                                                } else if ( !(shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY)) ){
                                                    assertNotEquals(State.EMPTY, shelfsMatrix[rowCheck - 1][columnChecK].getState());
                                                }
                                                notFound = 1;
                                                break;
                                            }
                                        } else {
                                            if ( i != 0 ) {
                                                assertNotEquals(0, i);
                                            } else if (j != 0) {
                                                assertNotEquals(0, j);
                                            } else if (!shelfsMatrix[i][j].getState().equals(State.EMPTY)){
                                                assertNotEquals(State.EMPTY, shelfsMatrix[i][j].getState());
                                            }
                                            notFound = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    notFound = 1;
                                    break;
                                }
                            }
                            if (notFound != 1) {
                                assertEquals(0, notFound);
                                if (this.numOfOccurrences > 1) {
                                    numOfOccFound++;
                                    tile = shelfsMatrix[i][j].getTile().getTileType();
                                    assertEquals(tile, shelfsMatrix[i][j].getTile().getTileType());
                                    if (numOfOccFound == this.numOfOccurrences) {
                                        cardsAlreadyChecked[this.ID] = 1;
                                        assertEquals(1, cardsAlreadyChecked[this.ID]);
                                    }
                                } else if (this.numOfOccurrences == 1) {
                                    cardsAlreadyChecked[this.ID] = 1;
                                    assertEquals(1, cardsAlreadyChecked[this.ID]);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.mirror == 1 && cardsAlreadyChecked[this.ID] != 1) {
            assertEquals(0,cardsAlreadyChecked[this.ID]);
            for (int r = 0; r < shelf.ROWS && cardsAlreadyChecked[this.ID] != 1; r++) {
                for (int c = shelf.COLUMNS - 1; c >= 0 && cardsAlreadyChecked[this.ID] != 1; c--) {
                    notFound = 0;

                    if (( r == 0 && c == shelf.COLUMNS - 1 ) || !shelfsMatrix[r][c].getState().equals(State.EMPTY)) {
                        if (tile == null || shelfsMatrix[r][c].getTile().getTileType().equals(tile)) {

                            for (Position position : this.positions) {
                                assertNotNull(position);
                                int rowCheck = r + position.getY();
                                int columnChecK = c - position.getX();
                                if (rowCheck < shelf.ROWS && columnChecK >= 0) {
                                    if (this.stairs != 1) {
                                        if (shelfsMatrix[r][c].getState().equals(State.EMPTY) || shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY)) {
                                            if ( shelfsMatrix[r][c].getState().equals(State.EMPTY) ) {
                                                assertEquals(State.EMPTY, shelfsMatrix[r][c].getState());
                                            } else if ( shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) ) {
                                                assertEquals(State.EMPTY, shelfsMatrix[rowCheck][columnChecK].getState());
                                            }
                                            notFound = 1;
                                            break;
                                        } else if (!shelfsMatrix[r][c].getTile().getTileType().equals(shelfsMatrix[rowCheck][columnChecK].getTile().getTileType())) {
                                            assertNotEquals(shelfsMatrix[r][c].getTile().getTileType(), shelfsMatrix[rowCheck][columnChecK].getTile().getTileType());
                                            notFound = 1;
                                            break;
                                        }
                                    } else {
                                        if (( r == 0 && c == shelf.COLUMNS - 1 ) && shelfsMatrix[r][c].getState().equals(State.EMPTY)) {
                                            assertEquals(State.EMPTY, shelfsMatrix[r][c].getState());
                                            if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) || !( shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY) )) {
                                                if (shelfsMatrix[rowCheck][columnChecK].getState().equals(State.EMPTY) ) {
                                                    assertEquals(State.EMPTY, shelfsMatrix[rowCheck][columnChecK].getState());
                                                } else if ( !(shelfsMatrix[rowCheck - 1][columnChecK].getState().equals(State.EMPTY)) ){
                                                    assertNotEquals(State.EMPTY, shelfsMatrix[rowCheck - 1][columnChecK].getState());
                                                }
                                                notFound = 1;
                                                break;
                                            }
                                        } else {
                                            if ( r != 0 && c != shelf.COLUMNS - 1 ) {
                                                assertNotEquals(0, r);
                                            } else if( c != shelf.COLUMNS - 1 ){
                                                assertNotEquals(shelf.COLUMNS - 1, c);
                                            } else if (!shelfsMatrix[r][c].getState().equals(State.EMPTY)){
                                                assertNotEquals(State.EMPTY, shelfsMatrix[r][c].getState());
                                            }
                                            notFound = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    notFound = 1;
                                    break;
                                }
                            }
                            if (notFound != 1) {
                                assertEquals(0, notFound);
                                cardsAlreadyChecked[this.ID] = 1;
                                assertEquals(1, cardsAlreadyChecked[this.ID]);
                            }
                        }
                    }
                }
            }
        }

        if (cardsAlreadyChecked[this.ID] == 1) {
            assertEquals(1, cardsAlreadyChecked[this.ID]);
        } else {
            assertNotEquals(1, cardsAlreadyChecked[this.ID]);
        }
    }
}




