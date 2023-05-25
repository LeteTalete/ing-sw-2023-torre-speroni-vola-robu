package it.polimi.ingsw.responses;

import it.polimi.ingsw.notifications.DisconnectionNotif;
import it.polimi.ingsw.notifications.GameStart;
import it.polimi.ingsw.notifications.NotifyOnTurn;

import java.rmi.RemoteException;

public interface ResponseHandler {
    void handle(GetTilesResponse getTilesResponse) throws RemoteException;
    void handle(Pinged pinged) throws RemoteException;
    void handle(LoginResponse loginResponse) throws RemoteException;

    void handle(GameStart gameStart) throws RemoteException;

    void handle(NotifyOnTurn notifyOnTurn) throws RemoteException;

    void handle(DisconnectionNotif disconnectionNotif) throws RemoteException;

    void handle(MoveOk moveOk) throws RemoteException;
    void handle(GetOrderResponse getOrderResponse) throws RemoteException;
}
