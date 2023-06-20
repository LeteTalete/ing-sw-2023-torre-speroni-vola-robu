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

    /** Default constructor for a couple. */
    public Couple(){

    }

    /**
     * Constructor for a Couple that can be picked.
     * @param tile - the couple's tile.
     */
    public Couple( Tile tile ){
        this.state = State.PICKABLE;
        this.tile = tile;
    }

    /**
     * Constructor for a space on the board that can be:
     * - INVALID: not meant to be played on.
     * - EMPTY_AND_UNUSABLE: an empty space that should not ever be used. (There aren't enough players to unlock this space)
     * @param check - 1 if the space is invalid, 0 if it's empty and unusable.
     */
    public Couple( int check ){
        if ( check == 1 ) {
            this.state = State.INVALID;
        } else {
            //todo: EMPTY_AND_UNUSABLE should be deleted from the state enumeration.
            this.state = State.EMPTY_AND_UNUSABLE;
        }
    }

}
