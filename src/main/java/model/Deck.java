package model;

import model.enumerations.Tile;

import java.util.Collections;
import java.util.Stack;



public class Deck{

    // attribute size and attribute tileKind are set with a JSON file
    private int size;
    private int tileKind;

    private Stack<Tile> tiles = new Stack<Tile>();

    public Deck()
    {
        for(int i=0; i<size/tileKind;i++)
        {
            for(Tile t : Tile.values())
            {
                tiles.push(t);
            }
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
