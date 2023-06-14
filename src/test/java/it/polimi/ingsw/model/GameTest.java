package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.server.ServerManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
    private Game game;

    /**
     * Method setUp creates a new game with 4 players and an ID before each test.
     * It checks if the game is not null and if the game's ID and the number of players are correctly set.
     */
    @BeforeEach
    public void setUp() {
        String ID = "1";
        ArrayList<Player> players = new ArrayList<>();
        int numOfPlayers = 4;
        for ( int i = 0; i < numOfPlayers; i++){
            players.add(new Player());
            players.get(i).setNickname("Player" + i); // The usernames are Player0, Player1, Player2, ...
        }

        GameController gameController;
        try {
            gameController = new GameController(players, ID, new ServerManager());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        game = gameController.getModel();
    }

    /**
     * Method setNull sets the game to null after each test.
     */
    @AfterEach
    public void setNull(){
        game = null;
    }

    /**
     * Test gameConstructorTest checks if the game is created correctly.
     * It checks if the number of players, the players' usernames, the players' shelves and the game's ID are correctly set.
     * It also checks if the game board, the end game token, the common goal cards, the current player and the previous
     * player are still correctly null.
     */
    @Test
    public void gameConstructorTest() {

        assertNotNull(game);
        assertEquals("1", game.getGameId());
        assertEquals(4, game.getPlayers().size());


        for (int i = 0; i < game.getPlayers().size(); i++){
            assertEquals("Player" + i, game.getPlayers().get(i).getNickname());
            assertNotNull(game.getPlayers().get(i).getMyShelf());
            assertNull(game.getPlayers().get(i).getGoalCard());
        }

        assertNull(game.getGameBoard());
        assertNull(game.getEndGame());
        assertNull(game.getCurrentPlayer());
        assertNull(game.getPreviousPlayer());
        assertNull(game.getCommonGoalCards());
    }

    /**
     * Test initializeTest checks if the game is initialized correctly.
     * After the initialization, the game board, the common goal cards, the current player and the personal goal cards
     * are all correctly set.
     * The end game token and the previous player are still correctly null.
     */
    @Test
    public void initializeTest() {

        assertNull(game.getGameBoard());
        assertNull(game.getEndGame());
        assertNull(game.getCurrentPlayer());
        assertNull(game.getPreviousPlayer());
        assertNull(game.getCommonGoalCards());

        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNull(game.getPlayers().get(i).getGoalCard());
        }

        game.initialize();

        assertNotNull(game.getGameBoard());
        assertNull(game.getEndGame());
        assertNotNull(game.getCurrentPlayer());
        assertNull(game.getPreviousPlayer());
        assertNotNull(game.getCommonGoalCards());
        assertEquals(2, game.getCommonGoalCards().size());

        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNotNull(game.getPlayers().get(i).getGoalCard());
        }
    }

    /**
     * Test chooseFirstPlayerTest checks if the first player is correctly chosen.
     * After the first player is chosen, the current player is correctly set and the previous player is still null.
     */
    @Test
    public void chooseFirstPlayerTest() {

        assertNull(game.getCurrentPlayer());
        assertNull(game.getPreviousPlayer());

        game.chooseFirstPlayer();

        assertNotNull(game.getCurrentPlayer());
        assertTrue(game.getCurrentPlayer().getChair());
        assertNull(game.getPreviousPlayer());
    }

    /**
     * Test nextTurnTest checks if nextTurn() correctly sets the current player and the previous player.
     * The current player is saved in a variable and then nextTurn() is called.
     * It checks if the current player is different from the (old) one saved and if the previous player is the one saved.
     */
    @Test
    public void nextTurnTest() {

        assertNull(game.getCurrentPlayer());
        game.initialize();
        assertNotNull(game.getCurrentPlayer());

        Player currentPlayer = game.getCurrentPlayer();
        game.nextTurn();
        assertNotEquals(currentPlayer, game.getCurrentPlayer());
        assertEquals(currentPlayer, game.getPreviousPlayer());
    }

    /**
     * Test scoreboardTest checks if the scoreboard is correctly sorted.
     * It sets the score of the players and then checks if the scoreboard is correctly sorted in descending order with
     * the player with the highest score in first position.
     */
    @Test
    public void scoreboardTest(){

        game.initialize();

        game.getPlayers().get(0).setScore(3);
        assertEquals(3, game.getPlayers().get(0).getScore());
        game.getPlayers().get(1).setScore(40);
        assertEquals(40, game.getPlayers().get(1).getScore());
        game.getPlayers().get(2).setScore(99); // The player with the highest score
        assertEquals(99, game.getPlayers().get(2).getScore());
        game.getPlayers().get(3).setScore(32);
        assertEquals(32, game.getPlayers().get(3).getScore());

        game.scoreBoard(game.getPlayers());

        assertEquals(game.getPlayers().get(2), game.getScoreBoard().get(0)); // The player with the highest score
        assertEquals(game.getPlayers().get(2).getScore(), game.getScoreBoard().get(0).getScore());

        assertEquals(game.getPlayers().get(1), game.getScoreBoard().get(1));
        assertEquals(game.getPlayers().get(1).getScore(), game.getScoreBoard().get(1).getScore());

        assertEquals(game.getPlayers().get(3), game.getScoreBoard().get(2));
        assertEquals(game.getPlayers().get(3).getScore(), game.getScoreBoard().get(2).getScore());

        assertEquals(game.getPlayers().get(0), game.getScoreBoard().get(3));
        assertEquals(game.getPlayers().get(0).getScore(), game.getScoreBoard().get(3).getScore());
    }

    /**
     * Test startGameTest checks if the CGCs and PGCs are correctly initialized.
     */
    @Test
    public void startGameTest(){

        assertNull(game.getCommonGoalCards());
        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNull(game.getPlayers().get(i).getGoalCard());
        }

        game.startGame();

        assertNotNull(game.getCommonGoalCards());
        assertEquals(2, game.getCommonGoalCards().size());

        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNotNull(game.getPlayers().get(i).getGoalCard());
        }
    }

    /**
     * Test gameHasEndedTest checks if the scores are correctly calculated and if the scoreBoard is correctly sorted.
     * The second, third and fourth player get respectively 4, 5, 6 tiles of the same color on the first column of their shelf.
     * That means the second player gets 3 points, the third 5 and the fourth 8. NB: The first player gets 0 points.
     * Since the PGCs are randomly assigned it is possible for player 4, 3 and 2 to have plus one point each.
     */
    @Test
    public void gameHasEndedTest(){

        game.initialize();

        ArrayList<Tile> tiles = new ArrayList<>();

        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        game.getPlayers().get(1).getMyShelf().insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        game.getPlayers().get(2).getMyShelf().insertTiles(0, tiles);

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        game.getPlayers().get(3).getMyShelf().insertTiles(0, tiles);

        assertEquals(0, game.getPlayers().get(0).getScore());
        assertEquals(0, game.getPlayers().get(1).getScore());
        assertEquals(0, game.getPlayers().get(2).getScore());
        assertEquals(0, game.getPlayers().get(3).getScore());

        assertNull(game.getScoreBoard());

        game.gameHasEnded();

        assertNotNull(game.getScoreBoard());

        assertEquals(0, game.getPlayers().get(0).getScore());
        assertTrue(game.getPlayers().get(1).getScore() == 3 || game.getPlayers().get(1).getScore() == 4);
        assertTrue(game.getPlayers().get(2).getScore() == 5 || game.getPlayers().get(2).getScore() == 6);
        assertTrue(game.getPlayers().get(3).getScore() == 8 || game.getPlayers().get(3).getScore() == 9);

        // Even if player 4, 3 and 2 have plus one point each, the scores don't overlap.
        assertEquals(game.getPlayers().get(3), game.getScoreBoard().get(0));
        assertEquals(game.getPlayers().get(2), game.getScoreBoard().get(1));
        assertEquals(game.getPlayers().get(1), game.getScoreBoard().get(2));
        assertEquals(game.getPlayers().get(0), game.getScoreBoard().get(3));
    }

    /**
     * Test calculateScoreTest checks if the scores are correctly calculated.
     * Each player gets 3 tiles of tile type GAMES on the third column of their shelf.
     * Each player gets 2 points but the end game token is set to the first player who gets 1 point more.
     */
    @Test
    public void calculateScoreTest(){

        game.initialize();

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES, 1));
        tiles.add(new Tile(T_Type.GAMES, 1));

        for (Player player : game.getPlayers() ){
            player.getMyShelf().insertTiles(2,tiles);
        }

        game.setEndGame(game.getPlayers().get(0).getNickname());

        assertEquals(0, game.getPlayers().get(0).getScore());
        assertEquals(0, game.getPlayers().get(1).getScore());
        assertEquals(0, game.getPlayers().get(2).getScore());
        assertEquals(0, game.getPlayers().get(3).getScore());

        game.calculateScore();

        assertEquals(3, game.getPlayers().get(0).getScore());
        assertEquals(2, game.getPlayers().get(1).getScore());
        assertEquals(2, game.getPlayers().get(2).getScore());
        assertEquals(2, game.getPlayers().get(3).getScore());
    }

    /**
     * Test generateCGCTest checks if the common goal cards are correctly generated.
     */
    @Test
    public void generateCGCTest(){

        assertNull(game.getCommonGoalCards());
        game.generateCGC(game.getPlayers().size());
        assertNotNull(game.getCommonGoalCards());

        for (CommonGoalCard card : game.getCommonGoalCards()) {

            Integer[] expectedIDs = {0,1,2,3,4,5,6,7,8,9,10,11};
            List<Integer> expectedIDsList = Arrays.asList(expectedIDs);
            assertTrue(expectedIDsList.contains((card.getID())));

            if ( game.getPlayers().size() == 4 ) {
                assertEquals(2, card.getPoints().get(0).intValue());
                assertEquals(4, card.getPoints().get(1).intValue());
                assertEquals(6, card.getPoints().get(2).intValue());
                assertEquals(8, card.getPoints().get(3).intValue());
            } else if ( game.getPlayers().size() == 3 ) {
                assertEquals(4, card.getPoints().get(0).intValue());
                assertEquals(6, card.getPoints().get(1).intValue());
                assertEquals(8, card.getPoints().get(2).intValue());
            } else if ( game.getPlayers().size() == 2 ) {
                assertEquals(4, card.getPoints().get(0).intValue());
                assertEquals(8, card.getPoints().get(1).intValue());
            }

            if ( card.getType().equals("Shape") ) {
                assertEquals("Shape", card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                assertTrue(card.getDiffType() == 0 || card.getDiffType() == 1);
                assertTrue(card.getSurrounded() == 0 || card.getSurrounded() == 1);
                assertTrue(card.getStairs() == 0 || card.getStairs() == 1);
                assertNotNull(card.getDescription());
                for (List<Position> currentShape : card.getPositions()) {
                    for (Position position : currentShape) {
                        assertNotNull(position);
                    }
                }
            } else if ( card.getType().equals("RowCol") ) {
                assertNotNull(card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                assertTrue(card.getDiffType() == 0 || card.getDiffType() == 1);
                assertTrue(card.getVertical() == 0 || card.getVertical() == 1);
                assertTrue(card.getHorizontal() == 0 || card.getHorizontal() == 1);
                assertNotNull(card.getDescription());
            } else if ( card.getType().equals("Groups") ) {
                assertNotNull(card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                assertTrue(card.getAtLeast() >= 1);
                assertNotNull(card.getDescription());
            }
        }
    }

    /**
     * Test generatePGCTest checks if the personal goal cards are correctly generated for each player.
     */
    @Test
    public void generatePGCTest(){

        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNull(game.getPlayers().get(i).getGoalCard());
        }

        game.generatePGC(game.getPlayers());

        Integer[] expectedIDs = {1,2,3,4,5,6,7,8,9,10,11,12};
        List<Integer> expectedIDsList = Arrays.asList(expectedIDs);

        for (int i = 0; i < game.getPlayers().size(); i++){
            assertNotNull(game.getPlayers().get(i).getGoalCard());
            assertTrue(expectedIDsList.contains((game.getPlayers().get(i).getGoalCard().getNumPGC())));
        }
    }

    /**
     * Test insertTilesTest checks if the tiles are correctly inserted inside the shelf.
     * When the shelf is full and if the end game token is still null it checks if the end game token is correctly set to
     * the current player who has filled his shelf.
     */
    @Test
    public void insertTilesTest(){

        game.initialize();

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));
        tiles.add(new Tile(T_Type.CAT, 1));

        assertNull(game.getEndGame());

        game.insertTiles(0,tiles);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(5,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(4,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(3,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(2,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(1,0)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(0,0)).getTile().getTileType(),T_Type.CAT);

        assertNull(game.getEndGame());

        game.insertTiles(1,tiles);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(5,1)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(4,1)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(3,1)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(2,1)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(1,1)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(0,1)).getTile().getTileType(),T_Type.CAT);

        assertNull(game.getEndGame());

        game.insertTiles(2,tiles);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(5,2)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(4,2)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(3,2)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(2,2)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(1,2)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(0,2)).getTile().getTileType(),T_Type.CAT);

        assertNull(game.getEndGame());

        game.insertTiles(3,tiles);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(5,3)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(4,3)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(3,3)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(2,3)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(1,3)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(0,3)).getTile().getTileType(),T_Type.CAT);

        assertNull(game.getEndGame());

        game.insertTiles(4,tiles);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(5,4)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(4,4)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(3,4)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(2,4)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(1,4)).getTile().getTileType(),T_Type.CAT);
        assertEquals(game.getCurrentPlayer().getMyShelf().getCouple(new Position(0,4)).getTile().getTileType(),T_Type.CAT);

        assertNotNull(game.getEndGame());
        assertEquals(game.getCurrentPlayer().getNickname(), game.getEndGame());
    }
}
