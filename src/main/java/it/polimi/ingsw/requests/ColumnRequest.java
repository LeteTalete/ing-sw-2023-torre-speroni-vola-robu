package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;

/**request from a client to choose a column to place the tiles in.*/

public class ColumnRequest extends Request{
    private final String token;
    private final int column;

    /**columnRequest constructor.
     * @param userToken - token used to identify the client.
     * @param number - number of column chosen.*/
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
