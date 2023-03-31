package model;

import model.enumerations.T_Type;
import model.enumerations.Tile;

import java.util.Collections;
import java.util.Stack;



public class Deck{

    private int size = 132;
    private int tileKind = 6;

    private Stack<Tile> tiles = new Stack<Tile>();

    public Deck()
    {
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

    public void shuffle(){
        //this method shuffles the deck so tiles will be drawn randomly
        Collections.shuffle(tiles);
   }

   public Tile draw()
   {
       return tiles.pop();
   }

}
