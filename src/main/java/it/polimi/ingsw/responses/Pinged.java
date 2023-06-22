package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

/**ping response to check whether the client is still connected and reachable*/

public class Pinged implements Response {
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException{
        responseHandler.handle(this);
    }
}
