package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;
import java.util.List;

public class RearrangeTilesRequest extends Request{
    private final String token;
    private final List<String> tilesOrder;

    public RearrangeTilesRequest(String userToken, List<String> multipleChoiceNumber) {
        tilesOrder = multipleChoiceNumber;
        token = userToken;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try{
            serverManager.rearrangeTiles(token, tilesOrder);
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }
    }
}
