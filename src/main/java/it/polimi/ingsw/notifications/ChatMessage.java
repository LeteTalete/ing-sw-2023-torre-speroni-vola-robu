package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**chat message sent to the client via server*/

public class ChatMessage implements Response {
    private final String sender;
    private final String message;
    private final String receiver;

    /**chatMessage constructor.
     * @param s - sender of the message.
     * @param r - receiver of the message.
     * @param m - text of the message.*/
    public ChatMessage(String s, String m, String r) {
        this.sender = s;
        this.message = m;
        this.receiver = r;
    }

    /**
     * Method handleResponse is used to handle a response sent by server socket
     * @param responseHandler - the response handler
     * @throws RemoteException - throws exception if the remote method call fails
     */
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    /**
     * Method getSender returns the sender of the chat message
     * @return the sender of the chat message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Method getMessage returns the chat message
     * @return the chat message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Method getReceiver returns the receiver of the chat message
     * @return the receiver of the chat message
     */
    public String getReceiver() {
        return receiver;
    }
}
