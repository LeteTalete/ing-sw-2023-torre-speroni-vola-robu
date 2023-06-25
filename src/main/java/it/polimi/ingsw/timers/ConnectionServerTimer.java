package it.polimi.ingsw.timers;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.ConnectionManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.TimerTask;

/**this class is used to send the ping/syn to the client*/

public class ConnectionServerTimer extends TimerTask implements Serializable {
    //todo needs to be renamed as ServerSynTimer
    private IClientListener clientListener;
    private String token;

    public ConnectionServerTimer(IClientListener clientListener){
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
            System.out.println("Sending syn to client");
            clientListener.onSyn();
        } catch (RemoteException e) {
            ConnectionManager.get().disconnectToken(token);
            throw new RuntimeException(e);
        }
    }
}
