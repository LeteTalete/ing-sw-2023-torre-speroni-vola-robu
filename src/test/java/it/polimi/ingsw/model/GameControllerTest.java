package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.CG_Group;
import it.polimi.ingsw.model.cards.CG_Shape;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class GameControllerTest {

    /** Test generateCGCTest randomly chooses 2 CGCs from the 12 cards of the game and prints their parameters */
    @Test
    public void generateCGCTest() {
        GameController game = new GameController();
        assertNotNull(game);
        int i = 0;
        int j;
        game.generateCGC(4);
        assertNotNull(game.getCommonGoalCards());

        for (CommonGoalCard card : game.getCommonGoalCards()) {
            j = 0;
            if ( card.getType().equals("Shape") ) {

                i++;
                System.out.println("Common goal card n°: " + i);
                assertNotNull(card.getID());
                System.out.println("Card ID: " + card.getID());
                assertNotNull(card.getType());
                System.out.println("Card Type: " + card.getType());
                assertNotNull(card.getNumOfOccurrences());
                System.out.println("Number of Occurrences: " + card.getNumOfOccurrences());
                assertNotNull(card.getDiffType());
                System.out.println("Different type: " + card.getDiffType());
                assertNotNull(card.getStairs());
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
                assertEquals(2, card.getPoints().get(0).intValue());
                assertEquals(4, card.getPoints().get(1).intValue());
                assertEquals(6, card.getPoints().get(2).intValue());
                assertEquals(8, card.getPoints().get(3).intValue());
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

            } else if ( card.getType().equals("Group") ) {

                i++;
                System.out.println("Common goal card n°: " + i);
                assertNotNull(card.getID());
                System.out.println("Card ID: " + card.getID());
                assertNotNull(card.getType());
                System.out.println("Card Type: " + card.getType());
                assertNotNull(card.getNumOfOccurrences());
                System.out.println("Number of Occurrences: " + card.getNumOfOccurrences());
                assertNotNull(card.getDiffType());
                System.out.println("How many diff tiles: " + card.getDiffUpTo());
                assertNotNull(card.getVertical());
                System.out.println("Vertical: " + card.getVertical());
                assertNotNull(card.getHorizontal());
                System.out.println("Horizontal: " + card.getHorizontal());
                assertNotNull(card.getDescription());
                System.out.println("Description: " + card.getDescription());
                assertEquals(2, card.getPoints().get(0).intValue());
                assertEquals(4, card.getPoints().get(1).intValue());
                assertEquals(6, card.getPoints().get(2).intValue());
                assertEquals(8, card.getPoints().get(3).intValue());
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
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        Player player = new Player();
        player.setNickname("lalala");
        player.setMyShelf(shelf);
        player.setGoalCard(1);

        List<CommonGoalCard> commonGoalCards = new ArrayList<>();
        commonGoalCards.add(new CG_Shape(1));
        commonGoalCards.add(new CG_Group(9));
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
