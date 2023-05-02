package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteController extends Remote {
    //this is our rmi registry
    String login (String name, IListener viewListener) throws RemoteException;

}
