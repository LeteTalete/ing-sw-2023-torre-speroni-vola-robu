package model.enumerations;

public class Couple {
    private Tile tileType;

    private State state;

    public Tile getTile() {
        return this.tileType;
    }

    public void setTile(Tile tile) {
        this.tileType = tile;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State newState) {
        this.state = newState;
    }
}
