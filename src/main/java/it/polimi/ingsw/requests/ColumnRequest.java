package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class ColumnRequest extends Request{
    private final String token;
    private final int column;

    public ColumnRequest(String userToken, int number) {
        token = userToken;
        column = number;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try{
            serverManager.selectColumn(token, column);
        }catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
