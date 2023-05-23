package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {
    //void processLogin(String result) throws RemoteException;
    //should become event driven
    String sendNotification(String message) throws RemoteException;
    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    String notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException;

    void setClientTurn() throws RemoteException;
}
