package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.State;

import java.io.Serializable;

public class Couple implements Serializable {
    private Tile tile;

    private State state;

    public Tile getTile() {
        return this.tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State newState) {
        this.state = newState;
    }


    public Couple(){

    }

    // Constructor for a tile that can be either placed or picked
    public Couple( Tile tile ){
        this.state = State.PICKABLE;
        this.tile = tile;
    }

    // Constructor for a space on the board that can be:
    // - INVALID: not meant to be played on.
    // - EMPTY_AND_UNUSABLE: an empty space that should not ever be used. (There aren't enough players to unlock this space)
    public Couple( int check ){
        if ( check == 1 ) {
            this.state = State.INVALID;
        } else {
            this.state = State.EMPTY_AND_UNUSABLE;
        }
    }

}
