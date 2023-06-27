package it.polimi.ingsw.model.board;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    /**
     * Constructor Position creates a new Position instance.
     * @param x - the x coordinate of the position.
     * @param y - the y coordinate of the position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method getY returns the y coordinate.
     * @return - the y coordinate.
     */
    public int getY() {
        return this.y;
    }


    /**
     * Method getX returns the x coordinate.
     * @return - the x coordinate.
     */
    public int getX()
    {
        return this.x;
    }

}
