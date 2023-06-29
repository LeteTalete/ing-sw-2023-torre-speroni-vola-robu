package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the disconnection of a user*/

public class DisconnectionNotif implements Response {
    private final String username;

    /**
     * DisconnectionNotif constructor
     * @param username - the username of the player who disconnected
     */
    public DisconnectionNotif(String username) {
        this.username = username;
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
     * Method getName returns the username of the player who disconnected
     * @return the username of the player who disconnected
     */
    public String getName() {
        return username;
    }
}
