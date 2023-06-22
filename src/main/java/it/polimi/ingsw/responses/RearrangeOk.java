package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.board.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**response about the re-arrange of the tiles chosen*/

public class RearrangeOk implements Response {
    private final boolean moveOk;
    private final ArrayList<Position> tiles;

    /**RearrangeOk response constructor.
     * @param moveOk - boolean signalling the success or failure of the move.
     * @param choice - list of tiles re-arranged.*/
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

