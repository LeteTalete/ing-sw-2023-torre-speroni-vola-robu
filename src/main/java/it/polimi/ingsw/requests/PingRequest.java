package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

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
