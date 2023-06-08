package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.board.Position;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RearrangeOk implements Response {
    private final boolean moveOk;
    private final ArrayList<Position> tiles;

    public RearrangeOk(boolean moveOk, ArrayList<Position> choice) {
        this.moveOk = moveOk;
        this.tiles = choice;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public boolean isMoveOk() {
        return moveOk;
    }
    public ArrayList<Position> getTiles() {
        return tiles;
    }
}

