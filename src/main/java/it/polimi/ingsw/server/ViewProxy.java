package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IClientListener;

import java.io.Serializable;
import java.rmi.RemoteException;

public class ViewProxy implements Serializable {
    private final ConnectionManager connectionManager;
    private final IClientListener clientListener;
    private final String token;

    public ViewProxy(IClientListener clientListener, String token) {
        this.connectionManager = ConnectionManager.get();
        this.clientListener = clientListener;
        this.token = token;
    }

    public void notifyRearrange(boolean b) throws RemoteException {
        clientListener.notifyRearrangeOk(true);
    }

    public String getToken() {
        return token;
    }

    public void notifyColumn(boolean b) throws RemoteException {
        clientListener.notifyColumnOk(b);
    }

    public void notifyGameStart() throws RemoteException {
        clientListener.notifyGameStart();
    }

    public void notifyStartTurn(String player) throws RemoteException {
        clientListener.notifyStartTurn(player);
    }

    public void notifyEndTurn() throws RemoteException {
        clientListener.notifyEndTurn();
    }
}
