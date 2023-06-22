package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.State;

import java.io.Serializable;

public class Couple implements Serializable {
    private Tile tile;

    private State state;

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
     * Getter method getTile returns the tile.
     * @return - the tile.
     */
    public Tile getTile() {
        return this.tile;
    }

    /**
     * Setter method setTile sets the tile.
     * @param tile - the tile.
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Getter method getState returns the tile's state.
     * @return - the tile's state.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Setter method setState sets the tile's state.
     * @param newState - the new state.
     */
    public void setState(State newState) {
        this.state = newState;
    }

}
