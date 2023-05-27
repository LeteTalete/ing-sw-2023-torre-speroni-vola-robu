package it.polimi.ingsw.model.enumerations;

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

    // todo: those two methods were added two months ago, but they are not used anywhere
    public void setTileType(T_Type t) {
        this.tileType = t;
    }

    public void setFigure(int d) {
        this.figure = d;
    }

}
