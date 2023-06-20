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

}
