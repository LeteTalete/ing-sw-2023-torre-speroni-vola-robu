package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;
import java.util.List;

/**request from the client to re-arrange the tiles chosen*/

public class RearrangeTilesRequest implements Request{
    private final String token;
    private final List<String> tilesOrder;

    /**RearrangeTilesRequest constructor.
     * @param multipleChoiceNumber - positions of the re-ordered tiles.
     * @param userToken - token used to identify the client.*/
    public RearrangeTilesRequest(String userToken, List<String> multipleChoiceNumber) {
        tilesOrder = multipleChoiceNumber;
        token = userToken;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try{
            serverManager.rearrangeTiles(token, tilesOrder);
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }
}
