package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class ChooseColumnResponse implements Response{
    private final String optional;
    private final boolean moveOk;

    public ChooseColumnResponse(boolean move, String message){
        this.moveOk = move;
        this.optional = message;
    }
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public boolean isMoveOk() {
        return moveOk;
    }

    public String getOptional() {
        return optional;
    }
}
