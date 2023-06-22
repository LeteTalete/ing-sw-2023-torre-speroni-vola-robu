package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the start of a new turn*/

public class NotifyOnTurn implements Response {
    private final String currentPlayer;

    public NotifyOnTurn(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
