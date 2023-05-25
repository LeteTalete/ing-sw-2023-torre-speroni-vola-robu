package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.responses.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {
    //void processLogin(String result) throws RemoteException;
    //should become event driven
    void sendNotification(Response response) throws RemoteException;
    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    void notifySuccessfulRegistration(LoginResponse response) throws RemoteException;

    void setClientTurn() throws RemoteException;

    void setGameOn() throws RemoteException;

    void changeTurn(String currentPlayer) throws RemoteException;

    void showTextNotification(String waitingRoomCreated) throws RemoteException;

    void notifyTilesResponse(GetTilesResponse getTilesResponse) throws RemoteException;

    void notifyMoveOk(MoveOk moveOk) throws RemoteException;
}
