package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the start of a new turn*/

public class NotifyOnTurn implements Response {
    private final String currentPlayer;

    /**
     * notifyOnTurn constructor
     * @param currentPlayer - the nickname of the current player
     */
    public NotifyOnTurn(String currentPlayer) {
        this.currentPlayer = currentPlayer;
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
     * Method getCurrentPlayer returns the nickname of the current player
     * @return the nickname of the current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
