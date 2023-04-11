package it.polimi.ingsw.model.enumerations;

public class Tile {
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

    public void setTileType(T_Type t) {
        this.tileType = t;
    }

    public void setFigure(int d) {
        this.figure = d;
    }

}
