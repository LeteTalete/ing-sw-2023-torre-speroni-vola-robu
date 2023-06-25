package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.board.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**response about the choice of tiles*/

public class TilesOk implements Response {
    private final int moveOk;
    private final ArrayList<Position> tiles;

    /**constructor of TilesOK response.
     * @param moveOk - boolean signalling the success or failure of the move.
     * @param choice - list of tiles chosen*/
    public TilesOk(int moveOk, ArrayList<Position> choice) {
        this.moveOk = moveOk;
        this.tiles = choice;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public int isMoveOk() {
        return moveOk;
    }

    public ArrayList<Position> getTiles() {
        return tiles;
    }
}

