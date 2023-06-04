package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.T_Type;

import java.io.Serializable;

public class Tile implements Serializable {
    private T_Type tileType;
    private int figure;


    public Tile(T_Type t, int d)
    {
        this.tileType = t;
        this.figure = d;
    }

    public T_Type getTileType()
    {
        return this.tileType;
    }

    public int getFigure()
    {
        return this.figure;
    }

}