package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteController extends Remote {
    //this is our rmi registry
    String login (String name, IClientListener viewListener) throws RemoteException;
    void pickedTiles(String username, String tilesCoordinates) throws RemoteException;
    void rearrangeTiles(String username, String tilesOrdered) throws RemoteException;
    //selectColumn method also calls endTurn method, which notifies the player about the end of their turn,
    //changes the currentPlayer, and checks if it;s the last turn
    void selectColumn (String username, int column) throws RemoteException;

}
