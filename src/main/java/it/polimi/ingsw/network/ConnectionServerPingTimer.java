package it.polimi.ingsw.network;

import it.polimi.ingsw.server.ConnectionManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.TimerTask;

public class ConnectionServerPingTimer extends TimerTask implements Serializable {
    private final IClientListener clientListener;

    public ConnectionServerPingTimer(IClientListener clientL){
        this.clientListener = clientL;
    }

    /***
     * if the client has sent the ack, it waits for a new one; if the client has not sent the ack, it disconnects the client
     */
    @Override
    public void run() {
        boolean b;
        try {
            b = ConnectionManager.get().getPingMap().get(clientListener.getToken());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if(!b) {
            try {
                ConnectionManager.get().disconnectToken(clientListener.getToken());
                ConnectionManager.get().stopPingTimer(clientListener.getToken());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                ConnectionManager.get().setPingMap(clientListener.getToken(),false);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
