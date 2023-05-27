package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

public class ChatMessage implements Response {
    private final String sender;
    private final String message;

    public ChatMessage(String s, String m) {
        this.sender = s;
        this.message = m;
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

}
