package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteController extends Remote {
    //this is our rmi registry
    void login (String name, IClientListener viewListener) throws RemoteException;
    void pickedTiles(String token, String tilesCoordinates) throws RemoteException;
    void rearrangeTiles(String token, String tilesOrdered) throws RemoteException;
    //selectColumn method also calls endTurn method, which notifies the player about the end of their turn,
    //changes the currentPlayer, and checks if it;s the last turn
    void selectColumn (String token, int column) throws RemoteException;

}
