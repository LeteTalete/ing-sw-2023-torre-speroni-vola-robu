package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;
import java.util.List;

public class ChooseTilesRequest extends Request {
    public final String token;
    public final List<String> tiles;
    public ChooseTilesRequest(String name, List<String> tilesChosen) {
        this.token = name;
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
