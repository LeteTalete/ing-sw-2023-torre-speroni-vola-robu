package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class TilesOk implements Response {
    private final boolean moveOk;

    public TilesOk(boolean moveOk) {
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

