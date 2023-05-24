package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.enumerations.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GetTilesResponse implements Response {
    private final ArrayList<Position> tilesChosen;
    private final boolean moveOk;

    public GetTilesResponse(ArrayList<Position> tilesChosen, boolean moveOk){
        this.tilesChosen = tilesChosen;
        this.moveOk = moveOk;
    }
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public ArrayList<Position> getTilesChosen() {
        return tilesChosen;
    }
}
