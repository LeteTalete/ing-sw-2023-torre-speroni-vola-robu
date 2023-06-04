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

    public Stack<Tile> getTiles() {
        return tiles;
    }

    public void shuffle(){
        //this method shuffles the deck so tiles will be drawn randomly
        Collections.shuffle(tiles);
   }

   public Tile draw()
   {
       return tiles.pop();
   }

   public int getSize()
   {
       return this.tiles.size();
   }

}
