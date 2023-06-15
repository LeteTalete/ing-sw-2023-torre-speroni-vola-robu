package it.polimi.ingsw.controller;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CG_RowCol;
import it.polimi.ingsw.model.cards.CG_Shape;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.server.ServerManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    private GameController gameController;
    GameController gC = mock(GameController.class);
    ServerManager master = mock(ServerManager.class);
    Game model = mock(Game.class);
    String gameId = "0";

    ArrayList<Position> choiceOfTiles = new ArrayList<>();

    /** Test generateCGCTest randomly chooses 2 CGCs from the 12 cards of the game and prints their parameters */
    @Test
    public void generateCGCTest() {
        ArrayList<Player> players = new ArrayList<>();
        int gameID = 0;
        int numOfPlayers = new Random().nextInt(3) + 2;
        System.out.println("Number of players: " + numOfPlayers + "\n");
        for ( int i = 0; i < numOfPlayers; i++){
            players.add(new Player());
        }

        GameController game;
        try {
            game = new GameController(players, Integer.toString(gameID),new ServerManager());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(game);
        int i = 0;
        int j;
        game.generateCGC();
        assertNotNull(game.getCommonGoalCards());

        for (CommonGoalCard card : game.getCommonGoalCards()) {

            Integer[] expectedIDs = {0,1,2,3,4,5,6,7,8,9,10,11};
            List<Integer> expectedIDsList = Arrays.asList(expectedIDs);
            assertTrue(expectedIDsList.contains((card.getID())));

            if ( numOfPlayers == 4 ) {
                assertEquals(2, card.getPoints().get(0).intValue());
                assertEquals(4, card.getPoints().get(1).intValue());
                assertEquals(6, card.getPoints().get(2).intValue());
                assertEquals(8, card.getPoints().get(3).intValue());
            } else if ( numOfPlayers == 3 ) {
                assertEquals(4, card.getPoints().get(0).intValue());
                assertEquals(6, card.getPoints().get(1).intValue());
                assertEquals(8, card.getPoints().get(2).intValue());
            } else if ( numOfPlayers == 2 ) {
                assertEquals(4, card.getPoints().get(0).intValue());
                assertEquals(8, card.getPoints().get(1).intValue());
            }

            j = 0;
            if ( card.getType().equals("Shape") ) {

                i++;
                System.out.println("Common goal card n°: " + i);
                System.out.println("Card ID: " + card.getID());
                assertEquals("Shape", card.getType());
                System.out.println("Card Type: " + card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                System.out.println("Number of Occurrences: " + card.getNumOfOccurrences());
                assertTrue(card.getDiffType() == 0 || card.getDiffType() == 1);
                System.out.println("Different type: " + card.getDiffType());
                assertTrue(card.getSurrounded() == 0 || card.getSurrounded() == 1);
                System.out.println("Surrounded: " + card.getSurrounded());
                assertTrue(card.getStairs() == 0 || card.getStairs() == 1);
                System.out.println("Stairs: " + card.getStairs());
                assertNotNull(card.getDescription());
                System.out.println("Description: " + card.getDescription());

                System.out.println("All card's shapes: ");
                for (List<Position> currentShape : card.getPositions()) {
                    j++;
                    System.out.println("Shape n°: " + j);
                    for (Position position : currentShape) {
                        assertNotNull(position);
                        System.out.println("x: " + position.getX() + ", y: " + position.getY());
                    }
                }

                System.out.print("Points: ");
                int size = card.getPoints().size();
                for (int m = 0; m < size; m++) {
                    if ( m == size - 1 ) {
                        System.out.print(card.getPoints().pop());
                    } else {
                        System.out.print(card.getPoints().pop() + ", ");
                    }
                }
                System.out.println();
                System.out.println();

            } else if ( card.getType().equals("RowCol") ) {

                i++;
                System.out.println("Common goal card n°: " + i);
                System.out.println("Card ID: " + card.getID());
                assertNotNull(card.getType());
                System.out.println("Card Type: " + card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                System.out.println("Number of Occurrences: " + card.getNumOfOccurrences());
                assertTrue(card.getDiffType() == 0 || card.getDiffType() == 1);
                System.out.println("How many diff tiles: " + card.getDiffUpTo());
                assertTrue(card.getVertical() == 0 || card.getVertical() == 1);
                System.out.println("Vertical: " + card.getVertical());
                assertTrue(card.getHorizontal() == 0 || card.getHorizontal() == 1);
                System.out.println("Horizontal: " + card.getHorizontal());
                assertNotNull(card.getDescription());
                System.out.println("Description: " + card.getDescription());

                System.out.print("Points: ");
                int size = card.getPoints().size();
                for (int m = 0; m < size; m++) {
                    if ( m == size - 1 ) {
                        System.out.print(card.getPoints().pop());
                    } else {
                        System.out.print(card.getPoints().pop() + ", ");
                    }
                }
                System.out.println();
                System.out.println();

            } else if ( card.getType().equals("Groups") ) {
                i++;
                System.out.println("Common goal card n°: " + i);
                System.out.println("Card ID: " + card.getID());
                assertNotNull(card.getType());
                System.out.println("Card Type: " + card.getType());
                assertTrue(card.getNumOfOccurrences() >= 0);
                System.out.println("Number of Occurrences: " + card.getNumOfOccurrences());
                assertTrue(card.getAtLeast() >= 1);
                System.out.println("At Least: " + card.getAtLeast());
                assertNotNull(card.getDescription());
                System.out.println("Description: " + card.getDescription());

                System.out.print("Points: ");
                int size = card.getPoints().size();
                for (int m = 0; m < size; m++) {
                    if ( m == size - 1 ) {
                        System.out.print(card.getPoints().pop());
                    } else {
                        System.out.print(card.getPoints().pop() + ", ");
                    }
                }
                System.out.println();
                System.out.println();

            }
        }
    }

    @Test
    public void calculateScoreTest(){

        Shelf shelf = new Shelf();
        ArrayList<Tile> tiles = new ArrayList<>();

        Player player = new Player();
        player.setNickname("kiwi");
        player.setMyShelf(shelf);
        player.setGoalCard(1);

        List<CommonGoalCard> commonGoalCards = new ArrayList<>();
        commonGoalCards.add(new CG_Shape(1));
        commonGoalCards.add(new CG_RowCol(9));
        for ( CommonGoalCard card : commonGoalCards ) {
            card.getPoints().push(2);
            card.getPoints().push(4);
            card.getPoints().push(6);
            card.getPoints().push(8);
        }

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        player.getMyShelf().insertTiles(0, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        player.getMyShelf().insertTiles(1, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        player.getMyShelf().insertTiles(2, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        player.getMyShelf().insertTiles(3, tiles);
        tiles.clear();
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        player.getMyShelf().insertTiles(4, tiles);
        player.getMyShelf().printShelf();

        for (CommonGoalCard card : commonGoalCards ) {
            if ( card.checkConditions(player.getMyShelf()) == 1 ) {
                int points = card.getPoints().pop();
                player.setScore(points);
                System.out.println(player.getNickname() + " has received " + points + " points from CGC " + card.getID());
            }
        }

        player.setScore(player.getGoalCard().scorePersonalGoalCard(player.getMyShelf()) + player.getMyShelf().additionalPoints());
        System.out.println("Final score: " + player.getScore());
        System.out.println("\n");
        assertEquals(38,player.getScore());
    }

    @Test
    public void notifyOnStartTurnTest(){
        ArrayList<Player> players = new ArrayList<>();
        gameController = new GameController(players,gameId, master);
        String nameTest = "kiwi";
        doNothing().when(master).notifyOnStartTurn(gameId, nameTest);
        gameController.notifyOnStartTurn(nameTest);
        verify(master, times(1)).notifyOnStartTurn(gameId, nameTest);
    }

    @Test
    public void notifyOnModelUpdateTest(){
        ArrayList<Player> players = new ArrayList<>();
        ModelUpdate modelUpdate = mock(ModelUpdate.class);
        gameController = new GameController(players,gameId, master);
        doNothing().when(master).notifyAllPlayers(gameId, modelUpdate);
        gameController.notifyOnModelUpdate(modelUpdate);
        verify(master, times(1)).notifyAllPlayers(gameId, modelUpdate);
    }

    @Test
    public void notifyOnEndGameTest(){
        ArrayList<Player> players = new ArrayList<>();
        gameController = new GameController(players,gameId, master);
        doNothing().when(master).notifyOnEndGame(gameId);
        gameController.notifyOnGameEnd();
        verify(master, times(1)).notifyOnEndGame(gameId);
    }

    @Test
    public void notifyOnLastTurnTest(){
        ArrayList<Player> players = new ArrayList<>();
        String nameTest = "kiwi";
        gameController = new GameController(players,gameId, master);
        doNothing().when(master).notifyOnLastTurn(gameId, nameTest);
        gameController.notifyOnLastTurn(nameTest);
        verify(master, times(1)).notifyOnLastTurn(gameId, nameTest);
    }

    @Test
    public void chooseColumnTest(){
        int columnTest = new Random().nextInt(5);

        ArrayList<Player> players = new ArrayList<>();

        Player player1 = new Player();
        player1.setNickname("kiwi");
        player1.setTokenId("1");
        player1.setMyShelf(new Shelf());
        player1.setGoalCard(1);
        players.add(player1);

        Player player2 = new Player();
        player2.setNickname("mango");
        player2.setTokenId("2");
        player2.setMyShelf(new Shelf());
        player2.setGoalCard(2);
        players.add(player2);

        gameController = new GameController(players,gameId, master);

        LivingRoom board = new LivingRoom(2);
        board.printBoard();

        choiceOfTiles.add(0, new Position(3,7));
        choiceOfTiles.add(1, new Position(4,7));

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(board.getCouple(choiceOfTiles.get(0)).getTile());
        tiles.add(board.getCouple(choiceOfTiles.get(1)).getTile());

        gameController.chooseColumn(player1.getTokenId(), columnTest);
        player1.getMyShelf().insertTiles(columnTest, tiles);
        Shelf shelfie = player1.getMyShelf();
        shelfie.printShelf();

        //checks if that column has been filled with the correct amount of tiles
        assert((6-tiles.size())==(shelfie.getFreeCol(columnTest)));

        Position p = new Position(5, columnTest);
        System.out.println(p.getX() + " " + p.getY());

        for (Tile tile : tiles) {
            assert(shelfie.getCouple(p).getTile().equals(tile));
            p = new Position(p.getX()-1, p.getY());
            System.out.println(p.getX() + " " + p.getY());
        }
    }

    @Test
    public void chooseTilesTest()
    {
        ArrayList<Player> players = new ArrayList<>();

        Player player1 = new Player();
        player1.setNickname("kiwi");
        player1.setTokenId("1");
        player1.setMyShelf(new Shelf());
        player1.setGoalCard(1);
        players.add(player1);

        Player player2 = new Player();
        player2.setNickname("mango");
        player2.setTokenId("2");
        player2.setMyShelf(new Shelf());
        player2.setGoalCard(2);
        players.add(player2);


        gameController = new GameController(players,gameId, master);

        LivingRoom board = new LivingRoom(2);

        Game gameModel = gameController.getModel();
        gameModel.setBoard(board);
        gameModel.getGameBoard().printBoard();
        gameModel.setCurrentPlayer(player1);


        //test1
        ArrayList<String> choice = new ArrayList<>();
        choice.add("3,7");
        choice.add("4,7");

        gameController.chooseTiles(player1.getTokenId(), choice);

        assert(gameController.getChoiceOfTiles().get(0).getX() == 3);
        assert(gameController.getChoiceOfTiles().get(0).getY() == 7);
        assert(gameController.getChoiceOfTiles().get(1).getX() == 4);
        assert(gameController.getChoiceOfTiles().get(1).getY() == 7);

    }
    @Test
    public void rearrangeTilesTest()
    {
        ArrayList<Player> players = new ArrayList<>();

        Player player1 = new Player();
        player1.setNickname("kiwi");
        player1.setTokenId("1");
        player1.setMyShelf(new Shelf());
        player1.setGoalCard(1);
        players.add(player1);

        Player player2 = new Player();
        player2.setNickname("mango");
        player2.setTokenId("2");
        player2.setMyShelf(new Shelf());
        player2.setGoalCard(2);
        players.add(player2);

        gameController = new GameController(players,gameId, master);

        LivingRoom board = new LivingRoom(2);
        board.printBoard();

        //test 1
        choiceOfTiles.clear();
        choiceOfTiles.add(0, new Position(3,7));
        choiceOfTiles.add(1, new Position(4,7));

        gameController.setChoiceOfTiles(choiceOfTiles);

        ArrayList<String> order = new ArrayList<>();
        order.add("2");
        order.add("1");

        gameController.rearrangeTiles(player1.getTokenId(), order);

        assert(gameController.getChoiceOfTiles().get(0).getX() == choiceOfTiles.get(1).getX());
        assert(gameController.getChoiceOfTiles().get(0).getY() == choiceOfTiles.get(1).getY());
        assert(gameController.getChoiceOfTiles().get(1).getX() == choiceOfTiles.get(0).getX());
        assert(gameController.getChoiceOfTiles().get(1).getY() == choiceOfTiles.get(0).getY());

        choiceOfTiles.clear();
        order.clear();


        //test 2
        choiceOfTiles.add(0, new Position(3,7));
        choiceOfTiles.add(1, new Position(4,7));

        gameController.setChoiceOfTiles(choiceOfTiles);

        order = new ArrayList<>();
        order.add("3");
        order.add("1");

        gameController.rearrangeTiles(player1.getTokenId(), order);

        assert(gameController.getChoiceOfTiles().get(0).getX() == choiceOfTiles.get(0).getX());
        assert(gameController.getChoiceOfTiles().get(0).getY() == choiceOfTiles.get(0).getY());
        assert(gameController.getChoiceOfTiles().get(1).getX() == choiceOfTiles.get(1).getX());
        assert(gameController.getChoiceOfTiles().get(1).getY() == choiceOfTiles.get(1).getY());

        choiceOfTiles.clear();
        order.clear();

        //test 3
        choiceOfTiles.add(0, new Position(3,7));
        choiceOfTiles.add(1, new Position(4,7));

        gameController.setChoiceOfTiles(choiceOfTiles);

        order = new ArrayList<>();
        order.add("1");
        order.add("1");

        gameController.rearrangeTiles(player1.getTokenId(), order);

        assert(gameController.getChoiceOfTiles().get(0).getX() == choiceOfTiles.get(0).getX());
        assert(gameController.getChoiceOfTiles().get(0).getY() == choiceOfTiles.get(0).getY());
        assert(gameController.getChoiceOfTiles().get(1).getX() == choiceOfTiles.get(1).getX());
        assert(gameController.getChoiceOfTiles().get(1).getY() == choiceOfTiles.get(1).getY());

        choiceOfTiles.clear();
        order.clear();

    }

    @Test
    public void nextTurnTest(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setNickname("kiwi");
        player1.setTokenId("1");
        player1.setMyShelf(new Shelf());
        player1.setGoalCard(1);
        players.add(player1);

        Player player2 = new Player();
        player2.setNickname("mango");
        player2.setTokenId("2");
        player2.setMyShelf(new Shelf());
        player2.setGoalCard(2);
        players.add(player2);

        Player player3 = new Player();
        player3.setNickname("banana");
        player3.setTokenId("3");
        player3.setMyShelf(new Shelf());
        player3.setGoalCard(3);
        players.add(player3);

        Player player4 = new Player();
        player4.setNickname("apple");
        player4.setTokenId("4");
        player4.setMyShelf(new Shelf());
        player4.setGoalCard(4);
        players.add(player4);

        gameController = new GameController(players,gameId, master);

        gameController.getModel().setCurrentPlayer(players.get(0));
        assert(player1.equals(gameController.getModel().getCurrentPlayer()));
        gameController.nextTurn("1");
        assertEquals(player2, gameController.getModel().getCurrentPlayer());

    }
}
