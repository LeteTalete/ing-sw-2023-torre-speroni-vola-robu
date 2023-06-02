package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {

    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException;

    void setGameOn() throws RemoteException;

    void changeTurn(String currentPlayer) throws RemoteException;

    void showTextNotification(String waitingRoomCreated) throws RemoteException;

    void notifyColumnOk(boolean ok) throws RemoteException;

    void notifyEndTurn() throws RemoteException;

    void notifyLastTurn(String firstDoneUser) throws RemoteException;

    void notifyChatMessage(String sender, String message) throws RemoteException;

    void updateModel(ModelUpdate modelUpdate) throws RemoteException;

    void notifyRearrangeOk(boolean ok) throws RemoteException;

    void notifyTilesOk(boolean ok) throws RemoteException;

    void notifyGameStart() throws RemoteException;

    void notifyStartTurn(String currentPlayer) throws RemoteException;

    void notifyEndGame() throws RemoteException;

    void notifyOnCGC(String nickname, int id) throws RemoteException;

    void notifyAboutDisconnection(String disconnectedUser) throws RemoteException;
}
