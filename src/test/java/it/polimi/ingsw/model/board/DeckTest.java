package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.enumerations.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeckTest {

    /**
     * Test deckTest checks if the deck is composed by 132 tiles.
     */
    @Test
    public void deckTest(){
        Deck deck = new Deck();
        assertEquals(132, deck.getSize());
    }

    /**
     * Test drawTest checks if after the draw method the deck size is decreased by 1.
     */
    @Test
    public void drawTest(){
        Deck deck = new Deck();
        deck.draw();
        assertEquals(131, deck.getSize());
    }

    /**
     * Test shuffleTest checks if the shuffle doesn't modify the deck size and if at the end of the shuffle the deck is
     * composed by the same tiles as before the shuffle.
     */
    @Test
    public void shuffleTest(){
        Deck deck = new Deck();

        Stack<Tile> tiles = new Stack<>();
        tiles.addAll(deck.getTiles());

        List<Tile> list1 = new ArrayList<>(tiles);
        list1 = list1.stream().sorted(Comparator.comparing(Tile::getTileType)).collect(Collectors.toList());

        deck.shuffle();
        assertEquals(132, deck.getSize());

        List<Tile> list2 = deck.getTiles().stream().sorted(Comparator.comparing(Tile::getTileType)).toList();

        for (int i = 0; i < deck.getSize(); i++) {
            assertEquals(list1.get(i).getTileType(), list2.get(i).getTileType());
        }
    }
}
