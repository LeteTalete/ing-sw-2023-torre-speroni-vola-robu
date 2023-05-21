package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class ChooseTilesRequest extends Request
{
    public final String username;
    public final String tiles;
    public ChooseTilesRequest(String name, String tilesChosen)
    {
        this.username = name;
        this.tiles = tilesChosen;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager)
    {
        try
        {
            //TODO here i pass the username but maybe i should pass the token
            serverManager.pickedTiles(username,tiles);
        }
        catch (RemoteException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
