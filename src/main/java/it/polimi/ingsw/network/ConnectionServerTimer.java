package it.polimi.ingsw.network;

import it.polimi.ingsw.client.IClientConnection;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.TimerTask;

public class ConnectionServerTimer extends TimerTask implements Serializable {
    private IClientListener clientListener;
    public ConnectionServerTimer(IClientListener cListener){
        this.clientListener=cListener;
    }
    @Override
    public void run() {
        try {
            clientListener.sendPingSyn();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
