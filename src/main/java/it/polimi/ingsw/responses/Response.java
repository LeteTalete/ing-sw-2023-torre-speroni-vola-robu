package it.polimi.ingsw.responses;

import java.io.Serializable;
import java.rmi.RemoteException;

/**generic response interface implemented by specific responses classes*/

public interface Response extends Serializable {
    /**
     * method handleResponse is implemented by the specific response classes
     * @param responseHandler is the object that will handle the response
     * @throws RemoteException if the reference could not be accessed
     * */
    void handleResponse(ResponseHandler responseHandler) throws RemoteException;
}
