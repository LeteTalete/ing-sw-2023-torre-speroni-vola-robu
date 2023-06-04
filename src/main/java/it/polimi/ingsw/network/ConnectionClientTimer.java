package it.polimi.ingsw.network;

import it.polimi.ingsw.client.IClientConnection;

import java.util.TimerTask;

public class ConnectionClientTimer extends TimerTask {
    private IClientConnection clientConnection;

    public ConnectionClientTimer(IClientConnection clientConnection){
        this.clientConnection=clientConnection;
    }
    @Override
    public void run() {
        if(clientConnection.isSyn()){
            clientConnection.setCheckTimer(false);
        }
        else{
            clientConnection.setPing(false);
            clientConnection.sendPing(clientConnection.getToken());
        }
    }
}
