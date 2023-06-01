package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.responses.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IClientListener extends Remote, Serializable {

    void sendNotification(Response response) throws RemoteException;
    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException;

    void setGameOn() throws RemoteException;

    void changeTurn(String currentPlayer) throws RemoteException;

    void showTextNotification(String waitingRoomCreated) throws RemoteException;

    void notifyColumnOk(boolean ok) throws RemoteException;

    void notifyEndTurn() throws RemoteException;

    void notifyLastTurn(String firstDoneUser) throws RemoteException;

    void notifyCommonGoalGained(CommonGoalGained commonGoalGained) throws RemoteException;

    void notifyChatMessage(String sender, String message) throws RemoteException;

    void updateModel(ModelUpdateNotification modelUpdateNotification) throws RemoteException;

    void notifyRearrangeOk(boolean ok) throws RemoteException;

    void notifyTilesOk(boolean ok) throws RemoteException;

    void notifyGameStart() throws RemoteException;

    void notifyStartTurn(String currentPlayer) throws RemoteException;


    void notifyEndGame() throws RemoteException;

    void notifyOnCGC(String nickname, int id) throws RemoteException;
}
