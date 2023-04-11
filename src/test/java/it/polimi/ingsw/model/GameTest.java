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

        List<CommonGoalCard> commonGoalCards = game.generateCGC();
        assertNotNull(commonGoalCards);

        for (CommonGoalCard card : commonGoalCards) {
            if ( card.getType().equals("Shape") ) {

                    assertNotNull(card.getID());
                    System.out.println( card.getID() );
                    assertNotNull(card.getType());
                    System.out.println( card.getType() );
                    assertNotNull(card.getNumOfOccurrences());
                    System.out.println( card.getNumOfOccurrences() );
                    assertNotNull(card.getMirror());
                    System.out.println( card.getMirror() );
                    assertNotNull(card.getStairs());
                    System.out.println( card.getStairs() );

                    for (Position position : card.getPositions() ) {
                        assertNotNull(position);
                        System.out.println("x: " + position.getX() + ", y: " + position.getY());
                    }

            } else if ( card.getType().equals("Group") ) {

                assertNotNull(card.getID());
                System.out.println( card.getID() );
                assertNotNull(card.getType());
                System.out.println( card.getType() );
            }
        }

    }

}
