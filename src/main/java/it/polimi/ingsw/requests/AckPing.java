package it.polimi.ingsw.requests;

import it.polimi.ingsw.network.ServerSocketClientHandler;
import it.polimi.ingsw.server.ServerManager;

import java.rmi.RemoteException;

public class AckPing extends Request{
    private String token;
    public AckPing(String tToken){
        token = tToken;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try {
            serverManager.sendAck(token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
