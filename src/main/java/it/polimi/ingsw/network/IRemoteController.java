package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**this is our RMI registry*/

public interface IRemoteController extends Remote {
    void login (String name, IClientListener viewListener) throws RemoteException;
    void pickedTiles(String token, List<String> tilesCoordinates) throws RemoteException;
    void rearrangeTiles(String token, List<String> tilesOrdered) throws RemoteException;

    void selectColumn (String token, int column) throws RemoteException;
    void createWaitingRoom(String username, String userToken) throws RemoteException;

    void sendChat(String username, String toString, String choice) throws RemoteException;
    void generateTokenRMI(IClientListener viewListener, String token) throws RemoteException;
    void sendPing(String token) throws RemoteException;

    void setPlayersWaitingRoom(String token, int num) throws RemoteException;

    void disconnect(String token) throws RemoteException;
}
