package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class Pinged implements Response {
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException{
        responseHandler.handle(this);
    }
}
