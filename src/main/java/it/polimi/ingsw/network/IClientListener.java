package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {
    //void processLogin(String result) throws RemoteException;
    //should become event driven
    int askHowMany() throws RemoteException;
    String sendNotification(String message) throws RemoteException;
    void sendUpdatedModel(ModelUpdate message) throws RemoteException;

    String notifySuccessfulRegistration(boolean b, String name) throws RemoteException;
}
