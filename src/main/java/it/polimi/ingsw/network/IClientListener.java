package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.responses.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {
    //void processLogin(String result) throws RemoteException;
    //should become event driven
    void sendNotification(Response response) throws RemoteException;
    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException;

    void setGameOn() throws RemoteException;

    void changeTurn(String currentPlayer) throws RemoteException;

    void showTextNotification(String waitingRoomCreated) throws RemoteException;

    void notifyColumnOk(boolean ok) throws RemoteException;

    void notifyEndTurn() throws RemoteException;

    void notifyGameEnd(GameEnd gameEnd) throws RemoteException;

    void notifyLastTurn(LastTurn lastTurn) throws RemoteException;

    void notifyCommonGoalGained(CommonGoalGained commonGoalGained) throws RemoteException;

    void notifyChatMessage(String sender, String message) throws RemoteException;

    void updateModel(ModelUpdateNotification modelUpdateNotification) throws RemoteException;

    void notifyRearrangeOk(boolean ok) throws RemoteException;

    void notifyTilesOk(boolean ok) throws RemoteException;

    void notifyGameStart() throws RemoteException;

    void notifyStartTurn(String currentPlayer) throws RemoteException;


}
