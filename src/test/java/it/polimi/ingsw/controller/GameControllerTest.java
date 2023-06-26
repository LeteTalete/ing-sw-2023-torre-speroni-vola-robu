package it.polimi.ingsw.controller;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CGC.*;
import it.polimi.ingsw.model.cards.CG_RowCol;
import it.polimi.ingsw.model.cards.CG_Shape;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.server.ServerManager;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.ArrayList;
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


        //test2
        gameController.setChoiceOfTiles(null);
        choice.clear();

        choice.add("15,7");

        gameController.chooseTiles(player1.getTokenId(), choice);

        assert(gameController.getChoiceOfTiles() == null);


        //test3
        gameController.setChoiceOfTiles(null);
        choice.clear();

        choice.add("3,9");

        gameController.chooseTiles(player1.getTokenId(), choice);

        assert(gameController.getChoiceOfTiles() == null);


        //test4
        gameController.setChoiceOfTiles(null);
        choice.clear();

        ArrayList<Tile> t = new ArrayList<>();
        for(int i = 0; i<player1.getMyShelf().getShelfMatrix()[0].length; i++)
        {
            t.clear();
            for(int j = 0; j<player1.getMyShelf().getShelfMatrix().length-1; j++)
            {
                t.add(new Tile(T_Type.CAT,0));
            }
            player1.getMyShelf().insertTiles(i,t);
        }
        player1.getMyShelf().printShelf();

        choice.add("3,7");
        choice.add("4,7");

        gameController.chooseTiles(player1.getTokenId(), choice);

        assert(gameController.getChoiceOfTiles() == null);


        //test5
        gameController.setChoiceOfTiles(null);
        choice.clear();
        player1.getMyShelf().clearShelf();

        for(int i = 0; i<player1.getMyShelf().getShelfMatrix()[0].length; i++)
        {
            t.clear();
            for(int j = 0; j<player1.getMyShelf().getShelfMatrix().length-1; j++)
            {
                t.add(new Tile(T_Type.CAT,0));
            }
            player1.getMyShelf().insertTiles(i,t);
        }
        player1.getMyShelf().printShelf();

        choice.add("3,7");

        gameController.chooseTiles(player1.getTokenId(), choice);

        assert(gameController.getChoiceOfTiles().get(0).getX() == 3);
        assert(gameController.getChoiceOfTiles().get(0).getY() == 7);

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
        gameController.nextTurn();
        assertEquals(player2, gameController.getModel().getCurrentPlayer());

    }

    @Test
    public void updateBoardCouplesTest()
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

        //test 1
        assert(gameModel.getGameBoard().getBoard()[3][7].getState().equals(State.PICKABLE));
        assert(gameModel.getGameBoard().getBoard()[3][7].getTile() != null);
        assert(gameModel.getGameBoard().getBoard()[4][7].getState().equals(State.PICKABLE));
        assert(gameModel.getGameBoard().getBoard()[4][7].getTile() != null);

        choiceOfTiles.clear();
        choiceOfTiles.add(0, new Position(3,7));
        choiceOfTiles.add(1, new Position(4,7));

        gameController.setChoiceOfTiles(choiceOfTiles);

        gameController.updateBoardCouples();

        assert(gameModel.getGameBoard().getBoard()[3][7].getState().equals(State.EMPTY));
        assert(gameModel.getGameBoard().getBoard()[3][7].getTile() == null);
        assert(gameModel.getGameBoard().getBoard()[4][7].getState().equals(State.EMPTY));
        assert(gameModel.getGameBoard().getBoard()[4][7].getTile() == null);

        gameModel.getGameBoard().printBoard();


        //test 2
        choiceOfTiles.clear();
        choiceOfTiles.add(0, new Position(3,6));
        choiceOfTiles.add(1, new Position(4,6));
        choiceOfTiles.add(2, new Position(5,6));
        choiceOfTiles.add(3, new Position(2,5));
        choiceOfTiles.add(4, new Position(3,5));
        choiceOfTiles.add(5, new Position(4,5));
        choiceOfTiles.add(6, new Position(5,5));
        choiceOfTiles.add(7, new Position(6,5));
        choiceOfTiles.add(8, new Position(7,5));
        choiceOfTiles.add(9, new Position(1,4));
        choiceOfTiles.add(10, new Position(2,4));
        choiceOfTiles.add(11, new Position(3,4));
        choiceOfTiles.add(12, new Position(4,4));
        choiceOfTiles.add(13, new Position(5,4));
        choiceOfTiles.add(14, new Position(6,4));
        choiceOfTiles.add(15, new Position(7,4));
        choiceOfTiles.add(16, new Position(1,3));
        choiceOfTiles.add(17, new Position(2,3));
        choiceOfTiles.add(18, new Position(3,3));
        choiceOfTiles.add(19, new Position(4,3));
        choiceOfTiles.add(20, new Position(5,3));
        choiceOfTiles.add(21, new Position(6,3));
        choiceOfTiles.add(22, new Position(3,2));
        choiceOfTiles.add(23, new Position(4,2));
        choiceOfTiles.add(24, new Position(5,2));

        gameController.setChoiceOfTiles(choiceOfTiles);

        gameController.updateBoardCouples();

        gameModel.getGameBoard().printBoard();

        assert(gameModel.getGameBoard().getBoard()[4][1].getState().equals(State.PICKABLE));
        assert(gameModel.getGameBoard().getBoard()[4][1].getTile() != null);

        choiceOfTiles.clear();
        choiceOfTiles.add(0, new Position(4,1));

        gameController.setChoiceOfTiles(choiceOfTiles);

        gameController.updateBoardCouples();

        assert(gameModel.getGameBoard().getBoard()[4][1].getState().equals(State.PICKABLE));
        assert(gameModel.getGameBoard().getBoard()[4][1].getTile() != null);

        gameModel.getGameBoard().printBoard();
    }

    @Test
    public void checkCGCsTest(){
        String ID = "1";
        ArrayList<Player> players = new ArrayList<>();
        int numOfPlayers = 2;
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

        Game game = gameController.getModel();
        game.startGame();
        game.setCurrentPlayer(game.getPlayers().get(0));

        CGC0Test cgc0Test = new CGC0Test();
        cgc0Test.setUp();
        cgc0Test.diagonalTest1();
        Shelf testShelf0 = cgc0Test.getTestShelf();

        CGC1Test cgc1Test = new CGC1Test();
        cgc1Test.setUp();
        cgc1Test.cornersTest();
        Shelf testShelf1 = cgc1Test.getTestShelf();

        CGC2Test cgc2Test = new CGC2Test();
        cgc2Test.setUp();
        cgc2Test.squareTest1();
        Shelf testShelf2 = cgc2Test.getTestShelf();

        CGC3Test cgc3Test = new CGC3Test();
        cgc3Test.setUp();
        cgc3Test.xTest1();
        Shelf testShelf3 = cgc3Test.getTestShelf();

        CGC4Test cgc4Test = new CGC4Test();
        cgc4Test.setUp();
        cgc4Test.stairsTest();
        Shelf testShelf4 = cgc4Test.getTestShelf();

        CGC5Test cgc5Test = new CGC5Test();
        cgc5Test.setUp();
        cgc5Test.sixOccTest1();
        Shelf testShelf5 = cgc5Test.getTestShelf();

        CGC6Test cgc6Test = new CGC6Test();
        cgc6Test.setUp();
        cgc6Test.fourOccTest1();
        Shelf testShelf6 = cgc6Test.getTestShelf();

        CGC7Test cgc7Test = new CGC7Test();
        cgc7Test.setUp();
        cgc7Test.eightOccTest1();
        Shelf testShelf7 = cgc7Test.getTestShelf();

        CGC8Test cgc8Test = new CGC8Test();
        cgc8Test.setUp();
        cgc8Test.threeVerticalTest1();
        Shelf testShelf8 = cgc8Test.getTestShelf();

        CGC9Test cgc9Test = new CGC9Test();
        cgc9Test.setUp();
        cgc9Test.threeHorizontalTest1();
        Shelf testShelf9 = cgc9Test.getTestShelf();

        CGC10Test cgc10Test = new CGC10Test();
        cgc10Test.setUp();
        cgc10Test.verticalTest1();
        Shelf testShelf10 = cgc10Test.getTestShelf();

        CGC11Test cgc11Test = new CGC11Test();
        cgc11Test.setUp();
        cgc11Test.horizontalTest1();
        Shelf testShelf11 = cgc11Test.getTestShelf();

        for ( Player player : gameController.getModel().getPlayers() ) {
            for ( CommonGoalCard card : gameController.getModel().getCommonGoalCards() ) {
                if ( card.getID() == 0 ) {
                    player.setMyShelf(testShelf0);
                } else if ( card.getID() == 1 ) {
                    player.setMyShelf(testShelf1);
                } else if ( card.getID() == 2 ) {
                    player.setMyShelf(testShelf2);
                } else if ( card.getID() == 3 ) {
                    player.setMyShelf(testShelf3);
                } else if ( card.getID() == 4 ) {
                    player.setMyShelf(testShelf4);
                } else if ( card.getID() == 5 ) {
                    player.setMyShelf(testShelf5);
                } else if ( card.getID() == 6 ) {
                    player.setMyShelf(testShelf6);
                } else if ( card.getID() == 7 ) {
                    player.setMyShelf(testShelf7);
                } else if ( card.getID() == 8 ) {
                    player.setMyShelf(testShelf8);
                } else if ( card.getID() == 9 ) {
                    player.setMyShelf(testShelf9);
                } else if ( card.getID() == 10 ) {
                    player.setMyShelf(testShelf10);
                } else if ( card.getID() == 11 ) {
                    player.setMyShelf(testShelf11);
                }
            }
        }

        gameController.checkCGCs();
        assertTrue(gameController.getCardsClaimed().containsKey(2));
        assertEquals(8, gameController.getCardsClaimed().get(2) );

    }




}
