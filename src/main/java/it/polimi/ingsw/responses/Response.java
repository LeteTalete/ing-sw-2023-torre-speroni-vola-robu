package it.polimi.ingsw.responses;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Response extends Serializable {
    void handleResponse(ResponseHandler responseHandler) throws RemoteException;
}
