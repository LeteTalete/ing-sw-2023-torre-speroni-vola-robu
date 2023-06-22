package it.polimi.ingsw.client;

import it.polimi.ingsw.network.ConnectionClientTimer;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;


public class ClientRMI implements IClientConnection, Remote, Serializable {
    private static final Logger fileLog = LogManager.getRootLogger();
    private final IRemoteController remoteController;
    private View viewClient;
    private String userToken;
    private boolean isConnected;
    private boolean syn;
    private Timer checkTimer;
    private final int synCheckTime = 1000;

    /**clientRMI constructor.
     * @param rc - it's the rmi registry used to invoke the server's methods from the client.*/
    public ClientRMI(IRemoteController rc) {
        this.remoteController = rc;
    }

    /**login method used to log the client in, and to pass the viewListener so that the server will be
     * able to contact the client.
     * @param name - username chosen by the client*/
    @Override
    public void login(String name) {
        try {
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

    /**numberOfPlayers method used to send the number of players of the new match to the server.
     * @param name - username of the client.
     * @param token - token used to identify the client.
     * @param number - number of players for the next match.*/
    @Override
    public void numberOfPlayers(String name, String token, int number) {
        try {
            remoteController.setPlayersWaitingRoom(token, number);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**chooseColumn method used to send the choice of column to the server.
     * @param column - choice of column.*/
    @Override
    public void chooseColumn(int column) {
        try {
            remoteController.selectColumn(userToken, column);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }


    @Override
    public void setPing(boolean b) {
        this.syn = b;
    }

    /**close method used to close the connection*/
    @Override
    public void close() {
        fileLog.info(System.getProperty("line.separator") + "Closing RMI connection..." );
        System.exit(0);
    }

    @Override
    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        //only for socket
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**rearrangeTiles method used to send the re-arranged tiles to the server.
     * @param userToken - token which identifies the client.
     * @param multipleChoiceNumber - list of positions of the re-arranged tiles.*/
    @Override
    public void rearrangeTiles(String userToken, List<String> multipleChoiceNumber) {
        try {
            remoteController.rearrangeTiles(userToken, multipleChoiceNumber);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**sendChat method used to send a chat message.
     * @param receiver - receiver of the message.
     * @param toString - the text of the message.
     * @param username - the sender of the message.*/
    @Override
    public void sendChat(String username, String toString, String receiver) {
        try {
            remoteController.sendChat(username, toString, receiver);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**sendPing method used to send a ping to the server to let it know that the client is still active
     * and reachable.*/
    @Override
    public void sendPing(String token) {
        try {
            remoteController.sendPing(token);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**quit method used to quit the game*/
    @Override
    public void quit(String token) {
        try {
            remoteController.disconnect(token);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**setCheckTimer is a method which resets the timer or creates a new one*/
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

    public void setViewClient(View currentView) {
        this.viewClient = currentView;
    }

    /**chooseTiles method is used to pass the choice of tiles to the server.
     * @param token - token used to identify the client.
     * @param tilesChosen - list of tiles chosen by the player.*/
    @Override
    public void chooseTiles(String token, List<String> tilesChosen) {
        try {
            remoteController.pickedTiles(token, tilesChosen);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isSyn() {
        return syn;
    }
}
