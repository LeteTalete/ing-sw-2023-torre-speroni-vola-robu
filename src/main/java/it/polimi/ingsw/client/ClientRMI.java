package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ConnectionClientTimer;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class ClientRMI implements IClientConnection, Remote, Serializable {
    private static Logger fileLog = LogManager.getRootLogger();
    private String username;
    private ClientController master;
    private final IRemoteController remoteController;
    private View viewClient;
    private String userToken;
    private ResponseDecoder responseDecoder;
    private boolean isConnected;
    private boolean syn;
    private Timer checkTimer;
    private final int synCheckTime = 1000;

    public ClientRMI(ClientController clientHandler, IRemoteController rc) {
        this.master = clientHandler;
        this.remoteController = rc;
    }

    @Override
    public void login(String name) {
        try {
            //needs the view to implement getListener method
            remoteController.login(name, viewClient.getListener());

        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void setUserToken(String token) {
        this.userToken = token;
    }

    @Override
    public String getToken() {
        return userToken;
    }

    @Override
    public void setReceivedResponse(boolean b) {
        //unused in rmi
    }

    @Override
    public void numberOfPlayers(String name, String token, int number) {
        try {
            remoteController.createWaitingRoom(name, userToken, number);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseColumn(int column) {
        try {
            remoteController.selectColumn(userToken, column);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());        }
    }


    @Override
    public void setPing(boolean b) {
        this.syn = b;
    }

    @Override
    public void close() {
        fileLog.info(System.getProperty("line.separator") + "Closing RMI connection..." );
        System.exit(0);
    }

    @Override
    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        this.responseDecoder = responseDecoder;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void rearrangeTiles(String userToken, List<String> multipleChoiceNumber) {
        try {
            remoteController.rearrangeTiles(userToken, multipleChoiceNumber);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void passTiles(ArrayList<Position> tilesChosen) {
        master.passTiles(tilesChosen);
    }

    @Override
    public void sendChat(String username, String toString, String choice) {
        try {
            remoteController.sendChat(username, toString, choice);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void sendPing(String token) {
        try {
            remoteController.sendPing(token);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void setCheckTimer(boolean b) {
        if(b){
            checkTimer = new Timer();
            checkTimer.scheduleAtFixedRate(new ConnectionClientTimer(this), synCheckTime, synCheckTime);
        }
        else{
            checkTimer.purge();
            checkTimer.cancel();
        }
    }

    @Override
    public void setName(String name) {
        this.username=name;
    }

    public void setViewClient(View currentView) {
        this.viewClient = currentView;
    }

    @Override
    public void chooseTiles(String token, List<String> tilesChosen) {
        try {
            remoteController.pickedTiles(token, tilesChosen);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public String getName() {
        return username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isSyn() {
        return syn;
    }
}
