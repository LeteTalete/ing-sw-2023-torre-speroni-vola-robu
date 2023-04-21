package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CommonGoalCard;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.util.List;

public class GameTest {

    // This tests if the list of CGC is correctly created
    // It prints the parameters saved inside each card
    @Test
    public static void main( String[] args ) {
        Game game = new Game();
        assertNotNull(game);
        int i = 0;
        int j;
        List<CommonGoalCard> commonGoalCards = game.generateCGC(4);
        assertNotNull(commonGoalCards);

        for (CommonGoalCard card : commonGoalCards) {
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

}
