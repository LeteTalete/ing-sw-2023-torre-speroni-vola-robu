package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.board.Position;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**this interface's methods are called mainly by the server to communicate with the client via RMI (using
 * ClientListenerGUI and ClientListenerTUI) or via Socket (using ServerSocketClientHandler)*/

public interface IClientListener extends Remote, Serializable {

    String getTypeConnection() throws RemoteException;

    void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException;

    void setGameOn() throws RemoteException;

    void showTextNotification(String waitingRoomCreated) throws RemoteException;

    void notifyColumnOk(boolean ok) throws RemoteException;

    void notifyEndTurn() throws RemoteException;

    void notifyLastTurn(String firstDoneUser) throws RemoteException;

    void notifyChatMessage(String sender, String message, String receiver) throws RemoteException;

    void updateModel(ModelUpdate modelUpdate) throws RemoteException;

    void notifyRearrangeOk(boolean ok, ArrayList<Position> tiles) throws RemoteException;

    void notifyTilesOk(boolean ok, ArrayList<Position> tiles) throws RemoteException;

    void notifyGameStart() throws RemoteException;

    void notifyStartTurn(String currentPlayer) throws RemoteException;

    void notifyEndGame() throws RemoteException;

    void notifyOnCGC(String nickname, int id) throws RemoteException;

    void notifyAboutDisconnection(String disconnectedUser) throws RemoteException;
    String getToken() throws RemoteException;
    void setToken(String token) throws RemoteException;
}
