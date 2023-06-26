package it.polimi.ingsw.timers;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.ConnectionManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.TimerTask;

/**this class is used to send the ping/syn to the client*/

public class ServerSynTimer extends TimerTask implements Serializable {
    private IClientListener clientListener;
    private String token;

    public ServerSynTimer(IClientListener clientListener){
        this.clientListener=clientListener;
        try {
            this.token = clientListener.getToken();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            clientListener.onSyn();
        } catch (RemoteException e) {
            ConnectionManager.get().disconnectToken(token);
        }
    }
}
