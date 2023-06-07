package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.board.Shelf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

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

    @Test
    public void getIDTest() {
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(card.getID(), 0);
    }

    @Test
    public void getDiffTypeTest() {
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(card.getDiffType(), 0);
    }

    @Test
    public void getStairsTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(card.getStairs(), 0);
    }

    @Test
    public void getTypeTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertNull(card.getType());
    }

    @Test
    public void getPositionTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertNull(card.getPositions());
    }

    @Test
    public void checkConditionTest(){
        CommonGoalCard card = new CommonGoalCard();
        Shelf shelf = new Shelf();
        assertEquals(0, card.checkConditions(shelf));
    }

    @Test
    public void getNumOfOccurrencesTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getNumOfOccurrences());
    }

    @Test
    public void getPointsTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertNull(card.getPoints());
    }

    @Test
    public void getDiffUpToTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getDiffUpTo());
    }

    @Test
    public void getHorizontalTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getHorizontal());
    }

    @Test
    public void getVerticalTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getVertical());
    }

    @Test
    public void getDescriptionTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertNull(card.getDescription());
    }

    @Test
    public void getAtLeastTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getAtLeast());
    }

    @Test
    public void getSurroundedTest(){
        CommonGoalCard card = new CommonGoalCard();
        assertEquals(0, card.getSurrounded());
    }


}
