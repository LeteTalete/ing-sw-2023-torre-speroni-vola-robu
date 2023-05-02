package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public interface ResponseHandler {
    void handle(GetTilesResponse getTilesResponse) throws RemoteException;
    void handle(Pinged pinged) throws RemoteException;
}
