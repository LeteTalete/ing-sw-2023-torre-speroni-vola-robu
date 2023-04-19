package it.polimi.ingsw.server;


import it.polimi.ingsw.client.ConnectionHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetworkManager extends Remote {
    String registerUser(String name) throws RemoteException;
    String howMany(int num, String name) throws RemoteException;

}
