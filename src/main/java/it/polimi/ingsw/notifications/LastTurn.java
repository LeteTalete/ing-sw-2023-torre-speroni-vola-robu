package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the beginning of the last turn after someone completed their shelf*/

public class LastTurn implements Response {
    private final String name;

    /**lastTurn constructor.
     * @param user - nickname of the first user who completes his shelf.
     */
    public LastTurn(String user) {
        this.name = user;
    }

    /**
     * Method handleResponse is used to handle a response sent by server socket
     * @param responseHandler - the response handler
     * @throws RemoteException
     */
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }


    /**
     * Method getName returns the nickname of the first user who completes his shelf.
     * @return the first user who completes his shelf.
     */
    public String getName() {
        return name;
    }
}
