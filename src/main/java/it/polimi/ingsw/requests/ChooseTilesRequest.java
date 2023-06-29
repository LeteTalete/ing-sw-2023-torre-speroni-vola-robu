package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;
import java.util.List;

/**request from the client to choose tiles from the board.*/

public class ChooseTilesRequest implements Request {
    private final String token;
    private final List<String> tiles;
    /**ChooseTilesRequest constructor.
     * @param t - token of the client.
     * @param tilesChosen - tiles chosen by the client.*/
    public ChooseTilesRequest(String t, List<String> tilesChosen) {
        this.token = t;
        this.tiles = tilesChosen;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try {
            serverManager.pickedTiles(token,tiles);
        }
        catch (RemoteException e) {
           throw new RuntimeException(e);
        }
    }
}
