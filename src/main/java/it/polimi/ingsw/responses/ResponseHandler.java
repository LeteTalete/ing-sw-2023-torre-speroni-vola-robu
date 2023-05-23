package it.polimi.ingsw.responses;

import it.polimi.ingsw.notifications.GameStart;

import java.rmi.RemoteException;

public interface ResponseHandler {
    void handle(GetTilesResponse getTilesResponse) throws RemoteException;
    void handle(Pinged pinged) throws RemoteException;
    void handle(LoginResponse loginResponse) throws RemoteException;

    void handle(GameStart gameStart) throws RemoteException;
}
