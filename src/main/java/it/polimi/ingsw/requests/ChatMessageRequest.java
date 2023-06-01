package it.polimi.ingsw.requests;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class ChatMessageRequest extends Request {
    private final String sender;
    private final String message;
    private final String receiver;

    public ChatMessageRequest(String s, String m, String r) {
        this.sender = s;
        this.message = m;
        this.receiver = r;
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


    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try {
            serverManager.sendChat(sender, message, receiver);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
