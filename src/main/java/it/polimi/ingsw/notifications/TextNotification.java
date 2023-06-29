package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**used by the server to send a generic text notification.*/

public class TextNotification implements Response {
    private final String message;

    /**
     * textNotification constructor
     * @param message - the message to show
     */
    public TextNotification(String message) {
        this.message = message;
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
     * Method getMessage returns the message to show
     * @return the message to show
     */
    public String getMessage() {
        return message;
    }
}
