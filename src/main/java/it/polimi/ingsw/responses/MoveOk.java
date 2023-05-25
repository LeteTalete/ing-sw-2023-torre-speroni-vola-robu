package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class MoveOk implements Response {
    private final boolean moveOk;

    public MoveOk(boolean moveOk) {
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
