package it.polimi.ingsw.responses;

import it.polimi.ingsw.model.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class GetOrderResponse implements Response
{
    private final ArrayList<Position> tilesChosen;
    private final boolean moveOk;

    public GetOrderResponse(ArrayList<Position> tilesChosen, boolean moveOk)
    {
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
