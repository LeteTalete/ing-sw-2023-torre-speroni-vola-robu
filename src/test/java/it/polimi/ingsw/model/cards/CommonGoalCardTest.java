package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CommonGoalCardTest {

    /**
     * Method cardTypeTest tests if the cardType method returns the correct card type.
     */
    @Test
    public void cardTypeTest() {
        CommonGoalCard card = new CommonGoalCard();
        assertNotNull(card);

        CommonGoalCard card1 = new CommonGoalCard(0);
        assertEquals(card1.cardType().getType(), "Shape");

        CommonGoalCard card2 = new CommonGoalCard(5);
        assertEquals(card2.cardType().getType(), "Groups");

        CommonGoalCard card3 = new CommonGoalCard(10);
        assertEquals(card3.cardType().getType(), "RowCol");
    }

}
