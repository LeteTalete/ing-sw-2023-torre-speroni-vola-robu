package it.polimi.ingsw.network;

import it.polimi.ingsw.notifications.ChatMessage;
import it.polimi.ingsw.requests.ChatMessageRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteController extends Remote {
    //this is our rmi registry
    void login (String name, IClientListener viewListener) throws RemoteException;
    void pickedTiles(String token, List<String> tilesCoordinates) throws RemoteException;
    void rearrangeTiles(String token, List<String> tilesOrdered) throws RemoteException;
    //selectColumn method also calls endTurn method, which notifies the player about the end of their turn,
    //changes the currentPlayer, and checks if it;s the last turn
    void selectColumn (String token, int column) throws RemoteException;
    void createWaitingRoom(String username, String userToken) throws RemoteException;

    void sendChat(String username, String toString, String choice) throws RemoteException;
    void generateTokenRMI(IClientListener viewListener, String token) throws RemoteException;
    void sendPing(String token) throws RemoteException;

    void setPlayersWaitingRoom(String token, int num) throws RemoteException;

    void disconnect(String token) throws RemoteException;
}
