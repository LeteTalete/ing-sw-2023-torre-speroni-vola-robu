package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

public class GameEnd implements Response {

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }
}
