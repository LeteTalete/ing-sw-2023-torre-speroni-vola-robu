package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.enumerations.Tile;

import java.rmi.RemoteException;
import java.util.List;

public class GetTilesResponse implements Response {
    private final List<Tile> tilesChosen;

    public GetTilesResponse(List<Tile> tilesChosen){
        this.tilesChosen = tilesChosen;
    }
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public List<Tile> getTilesChosen(){
        return tilesChosen;
    }
}
