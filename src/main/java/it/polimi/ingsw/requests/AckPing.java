package it.polimi.ingsw.requests;

import it.polimi.ingsw.network.ServerSocketClientHandler;
import it.polimi.ingsw.server.ServerManager;

import java.rmi.RemoteException;

/**ack response sent by the server to the client*/

public class AckPing implements Request{
    private final String token;

    /**AckPing constructor.
     * @param tToken - token of the client.*/
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
