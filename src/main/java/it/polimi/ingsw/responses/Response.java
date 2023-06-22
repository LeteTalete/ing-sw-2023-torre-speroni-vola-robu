package it.polimi.ingsw.responses;

import java.io.Serializable;
import java.rmi.RemoteException;

/**generic response interface implemented by specific responses classes*/

public interface Response extends Serializable {
    void handleResponse(ResponseHandler responseHandler) throws RemoteException;
}
