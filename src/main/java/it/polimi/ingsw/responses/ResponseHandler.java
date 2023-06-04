package it.polimi.ingsw.responses;

import it.polimi.ingsw.notifications.*;

import java.rmi.RemoteException;

public interface ResponseHandler {
    void handle(GetTilesResponse getTilesResponse) throws RemoteException;
    void handle(Pinged pinged) throws RemoteException;
    void handle(LoginResponse loginResponse) throws RemoteException;

    void handle(GameStart gameStart) throws RemoteException;

    void handle(NotifyOnTurn notifyOnTurn) throws RemoteException;

    void handle(DisconnectionNotif disconnectionNotif) throws RemoteException;

    void handle(ColumnOk moveOk) throws RemoteException;

    void handle(EndTurn endTurn) throws RemoteException;

    void handle(GameEnd gameEnd) throws RemoteException;

    void handle(LastTurn lastTurn) throws RemoteException;

    void handle(CommonGoalGained commonGoalGained) throws RemoteException;

    void handle(ChatMessage chatMessage) throws RemoteException;

    void handle(TextNotification textNotification) throws RemoteException;

    void handle(ModelUpdateNotification modelUpdateNotification) throws RemoteException;

    void handle(RearrangeOk rearrangeOk) throws RemoteException;

    void handle(TilesOk tilesOk) throws RemoteException;
}
