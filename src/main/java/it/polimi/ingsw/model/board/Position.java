package it.polimi.ingsw.model.board;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }


    public int getX()
    {
        return this.x;
    }

}
