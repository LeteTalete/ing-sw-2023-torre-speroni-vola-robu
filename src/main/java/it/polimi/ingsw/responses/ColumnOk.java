package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

/**response after the choice of a column*/

public class ColumnOk implements Response {
    private final boolean moveOk;

    /**columnOk response constructor.
     * @param moveOk - boolean signalling the success or failure of the move.*/
    public ColumnOk(boolean moveOk) {
        this.moveOk = moveOk;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public boolean isMoveOk() {
        return moveOk;
    }
}
