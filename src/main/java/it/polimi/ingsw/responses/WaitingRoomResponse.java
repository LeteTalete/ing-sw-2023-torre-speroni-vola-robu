package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class WaitingRoomResponse implements Response {

    private final String message;

    public WaitingRoomResponse(String m) {
        this.message = m;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
