package it.polimi.ingsw.network;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote, Serializable {
    //void processLogin(String result) throws RemoteException;
    //should become event driven
    int askHowMany() throws RemoteException;
    void sendNotification(String message) throws RemoteException;
}
