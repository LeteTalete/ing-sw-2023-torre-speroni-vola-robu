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

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }
}
