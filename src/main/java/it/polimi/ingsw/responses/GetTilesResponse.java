package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.board.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class GetTilesResponse implements Response {
    private final ArrayList<Position> tilesChosen;

    public GetTilesResponse(ArrayList<Position> tilesChosen){
        this.tilesChosen = tilesChosen;
    }
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public ArrayList<Position> getTilesChosen() {
        return tilesChosen;
    }

}
