package it.polimi.ingsw.timers;

import it.polimi.ingsw.client.IClientConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

/**this is the timer client-side to check whether the client received the syn/ping by the server. If not, it
 * means there is a network error and that the client has lost connection. Otherwise, the client will
 * reply by sending an ack to the server.*/

public class ConnectionClientTimer extends TimerTask {
    private IClientConnection clientConnection;
    private static final Logger fileLog = LogManager.getRootLogger();

    public ConnectionClientTimer(IClientConnection clientConnection){
        this.clientConnection=clientConnection;
    }

    @Override
    public void run() {
        if(!clientConnection.isSyn()){
            fileLog.error("Network error! Impossible to reach the Server...");
            clientConnection.setSynCheckTimer(false);
            clientConnection.setConnected(false);
        }
        else{
            fileLog.debug("Received syn from server");
            clientConnection.setSyn(false);
            clientConnection.sendAck();
            clientConnection.setConnected(true);
        }
    }
}
