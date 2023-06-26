package it.polimi.ingsw.model.cards.PGC;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.enumerations.T_Type;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PGC1Test {

    /**
     * This test checks if the score of the personal goal card is calculated correctly.
     * It starts by gradually filling the shelf with tiles and checking if the score is correct when there is a match
     * between the tiles on the shelf and the tiles on the personal goal card.
     */
    @Test
    public void scoreTest() {
        Player player = new Player();
        Shelf shelf = player.getMyShelf();
        ArrayList<Tile> tiles = new ArrayList<>();

        player.setGoalCard(1);
        assertEquals(1, player.getGoalCard().getNumPGC());

        assertEquals("0:P:2:F", player.getGoalCard().getPositionTilePC().get(0));
        assertEquals("4:C", player.getGoalCard().getPositionTilePC().get(1));
        assertEquals("3:B", player.getGoalCard().getPositionTilePC().get(2));
        assertEquals("1:G", player.getGoalCard().getPositionTilePC().get(3));
        assertEquals("-", player.getGoalCard().getPositionTilePC().get(4));
        assertEquals("2:T", player.getGoalCard().getPositionTilePC().get(5));

        assertEquals(0, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        tiles.add(new Tile(T_Type.PLANT,1));
        shelf.insertTiles(0, tiles);
        assertEquals(1, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.clear();
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        tiles.add(new Tile(T_Type.GAMES,1));
        shelf.insertTiles(1, tiles);
        assertEquals(2, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.clear();
        tiles.add(new Tile(T_Type.TROPHY,1));
        shelf.insertTiles(2, tiles);
        assertEquals(4, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.clear();
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        tiles.add(new Tile(T_Type.FRAME,1));
        shelf.insertTiles(2, tiles);
        assertEquals(6, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.clear();
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        tiles.add(new Tile(T_Type.BOOK,1));
        shelf.insertTiles(3, tiles);
        assertEquals(9, player.getGoalCard().scorePersonalGoalCard(shelf));

        tiles.clear();
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        tiles.add(new Tile(T_Type.CAT,1));
        shelf.insertTiles(4, tiles);
        assertEquals(12, player.getGoalCard().scorePersonalGoalCard(shelf));
    }
}
