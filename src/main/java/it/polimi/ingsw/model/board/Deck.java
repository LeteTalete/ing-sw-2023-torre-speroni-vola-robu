package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.board.Tile;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;



public class Deck implements Serializable {

    public static final int size = 132;
    public static final int tileKind = 6;

    private Stack<Tile> tiles;

    /** Constructor Deck creates a new Deck instance, it adds 132 tiles to the deck's tiles stack and shuffles them. */
    public Deck()
    {
        tiles = new Stack<>();
        int count = 1;
        for(int i=0; i<size/tileKind;i++)
        {
            for(T_Type t_type : T_Type.values())
            {
                Tile t = new Tile(t_type,count);
                tiles.push(t);
            }
            count++;
            if(count > 3) count = 1;
        }
        this.shuffle();
    }

    /**
     * Method getTiles returns the tiles of this Deck object.
     * @return - tiles.
     */
    public Stack<Tile> getTiles() {
        return tiles;
    }

    /** Method shuffle shuffles the tiles of the deck. */
    public void shuffle(){
        //this method shuffles the deck so tiles will be drawn randomly
        Collections.shuffle(tiles);
   }

    /**
     * Method draw returns the last tile of the deck.
     * @return - the last tile of the deck.
     */
   public Tile draw()
   {
       return tiles.pop();
   }

    /**
     * Method getSize returns the size of the deck.
     * @return - the size of the deck.
     */
   public int getSize()
   {
       return this.tiles.size();
   }

}
