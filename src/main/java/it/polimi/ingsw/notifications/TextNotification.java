package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**used by the server to send a generic text notification.*/

public class TextNotification implements Response {
    private final String message;

    public TextNotification(String message) {
        this.message = message;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
