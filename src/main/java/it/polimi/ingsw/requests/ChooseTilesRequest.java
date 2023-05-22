package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class ChooseTilesRequest extends Request
{
    public final String token;
    public final String tiles;
    public ChooseTilesRequest(String name, String tilesChosen)
    {
        this.token = name;
        this.tiles = tilesChosen;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager)
    {
        try
        {
            serverManager.pickedTiles(token,tiles);
        }
        catch (RemoteException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
