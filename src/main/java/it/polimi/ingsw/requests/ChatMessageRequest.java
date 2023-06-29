package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;

/**chat message sent from the client*/

public class ChatMessageRequest implements Request {
    private final String sender;
    private final String message;
    private final String receiver;

    /**ChatMessageRequest constructor.
     * @param m - text of the message.
     * @param r - receiver of the message.
     * @param s - sender of the message.*/
    public ChatMessageRequest(String s, String m, String r) {
        this.sender = s;
        this.message = m;
        this.receiver = r;
    }
    public String getMessage() {
        return message;
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
