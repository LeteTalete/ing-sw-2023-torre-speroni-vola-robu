package it.polimi.ingsw.timers;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.TimerTask;

/**this is used to check whether a player has sent an ack. If not, it means they are unreachable, and they
 * get disconnected. Otherwise, the server will wait for another ack.*/
public class ConnectionAckTimer extends TimerTask implements Serializable {
    private IClientListener clientListener;
    private static final Logger fileLog = LogManager.getRootLogger();

    public ConnectionAckTimer(IClientListener clientListener){
        this.clientListener=clientListener;
    }

    @Override
    public void run() {
        try {
            if(!ConnectionManager.get().getAckMap().get(clientListener.getToken())) {
                fileLog.error("Network error! Impossible to reach the Client. Disconnecting the client, now");
                ConnectionManager.get().disconnectToken(clientListener.getToken());
                ConnectionManager.get().stopSynTimer(clientListener.getToken());
                ConnectionManager.get().stopAckTimer(clientListener.getToken());
            }
            else {
                ConnectionManager.get().setAck(clientListener.getToken(),true);
            }
        } catch (RemoteException e) {
            fileLog.error("Error: "+ e.getMessage());
        }
    }
}
