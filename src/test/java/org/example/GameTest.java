package org.example;

import model.Game;
import model.Player;
import model.Position;
import model.board.LivingRoom;
import model.cards.CG_Group;
import model.cards.CG_Shape;
import model.cards.CommonGoalCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTest {

    // Method generateCGC generates and returns an ArrayList containing CommonGoalCard objects
    // Those will be the cards that will be used in the game
    // First it generates 2 different random numbers from 1 to 12
    // (inside the code it's from 1 to 13 because the upper bound is exclusive)
    // Then it iterates for how many cards are needed and adds the cards to the ArrayList
    public ArrayList<CommonGoalCard> generateCGC(){
        ArrayList<CommonGoalCard> commonGoalCards = new ArrayList<>();
        int numberOfCommonGoalCards = 2; // Change this number if you want to use more cards
        int[] idsOfTheCards = new Random().ints(1, 13).distinct().limit(numberOfCommonGoalCards).toArray();

        for ( int i = 0; i < numberOfCommonGoalCards; i++){
            CommonGoalCard dummy = new CommonGoalCard(idsOfTheCards[i]);
            commonGoalCards.add(dummy.typeGroupOrShape());
        }

        return commonGoalCards;
    }

    // This tests if the list of CGC is correctly created
    // It prints the parameters saved inside each card
    // If the card ia a CG_Group card ( IDs from 7 to 12 ) it prints only its ID ( I haven't worked yet on CG_Group )
    // If the card is a CG_Shape card ( IDs from 1 to 6 ) first it checks for RandomTiles and Stairs
    // When those parameters are set to anything other than 0 it means that the card has not saved any coordinates
    // as the coordinates themselves are not inside the json file for the specific card
    // If RandomTiles and Stairs are both 0 then it also prints the coordinates that are saved
    @Test
    public static void main( String[] args ) {
        Game game = new Game();
        List<CommonGoalCard> commonGoalCards = game.generateCGC();

        for (CommonGoalCard card : commonGoalCards) {
            if ( card.getType().equals("Shape") ) {
                if (  card.getStairs() == 0 ){

                    System.out.println( card.getID() );
                    System.out.println( card.getType() );
                    System.out.println( card.getMirror() );
                    System.out.println( card.getSameType() );
                    System.out.println( card.getStairs() );

                    for (Position position : card.getPositions() ) {
                        System.out.println("x: " + position.getX() + ", y: " + position.getY());
                    }
                } else {
                    System.out.println( card.getID() );
                    System.out.println( card.getType() );
                    System.out.println( card.getMirror() );
                    System.out.println( card.getSameType() );
                    System.out.println( card.getStairs() );
                }
            } else if ( card.getType().equals("Group") ) {
                System.out.println( card.getID() );
                System.out.println( card.getType() );
            }
        }

    }

}
