package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.T_Type;

import java.io.Serializable;

public class Tile implements Serializable {
    private T_Type tileType;
    private int figure;

    /**
     * Constructor Tile given a tile type and a figure creates a new Tile instance.
     * @param t - the tile type.
     * @param d - the figure.
     */
    public Tile(T_Type t, int d)
    {
        this.tileType = t;
        this.figure = d;
    }

    /**
     * Method getTileType returns the tile type.
     * @return - the tile type.
     */
    public T_Type getTileType()
    {
        return this.tileType;
    }

    /**
     * Method getFigure returns the figure.
     * @return - the figure.
     */
    public int getFigure()
    {
        return this.figure;
    }

}
