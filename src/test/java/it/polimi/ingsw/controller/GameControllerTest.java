package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.CG_RowCol;
import it.polimi.ingsw.model.cards.CG_Shape;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.server.ServerManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameControllerTest {

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


}
