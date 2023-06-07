package it.polimi.ingsw.model.board;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }


    //cleanup those methods are never used
    public Position() {
    }

    public int getX()
    {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
