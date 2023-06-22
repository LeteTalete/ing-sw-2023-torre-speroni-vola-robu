package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;

/**used by the client to send a ping to the server*/

public class PingRequest extends Request{
    private final String token;

    public PingRequest(String t) {
        this.token = t;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try {
            serverManager.sendPing(token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
