package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.timers.ConnectionClientTimer;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;

/**ClientRMI class used to manage the RMI connection to the server.*/

public class ClientRMI implements IClientConnection, Remote, Serializable {
    private static final Logger fileLog = LogManager.getRootLogger();
    private final IRemoteController remoteController;
    private View viewClient;
    private String userToken;
    private boolean isConnected;
    private boolean syn;
    private final int synCheckTime = 15000;
    private Timer synCheckTimer;

    /**clientRMI constructor.
     * @param rc - it's the rmi registry used to invoke the server's methods from the client.*/
    public ClientRMI(IRemoteController rc) {
        this.remoteController = rc;
        synCheckTimer = new Timer();
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
            viewClient.printError("Network error. Please, try again");
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
            viewClient.printError("Network error. Please, try again");
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
            viewClient.printError("Network error. Please, try again");
        }
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



    /**rearrangeTiles method used to send the re-arranged tiles to the server.
     * @param userToken - token which identifies the client.
     * @param multipleChoiceNumber - list of positions of the re-arranged tiles.*/
    @Override
    public void rearrangeTiles(String userToken, List<String> multipleChoiceNumber) {
        try {
            remoteController.rearrangeTiles(userToken, multipleChoiceNumber);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
            viewClient.printError("Network error. Please, try again");
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
            viewClient.printError("Network error. Please, try again");
        }
    }

    /**quit method used to quit the game*/
    @Override
    public void quit(String token) {
        try {
            remoteController.disconnect(token);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
            viewClient.printError("Network error. Please, try again");
        }
    }

    @Override
    public void setSyn(boolean b) {
        this.syn = b;
    }

    @Override
    public boolean isSyn() {
        return syn;
    }

    @Override
    public void setSynCheckTimer(boolean startTimer) {
        if (startTimer) {
            synCheckTimer = new Timer();
            synCheckTimer.scheduleAtFixedRate(new ConnectionClientTimer(this), (synCheckTime/2)+synCheckTime, synCheckTime);
        } else {
            synCheckTimer.purge();
            synCheckTimer.cancel();
        }
    }

    @Override
    public void sendAck() {
        try{
            remoteController.sendAck(userToken);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
            viewClient.printError("Network error. Please, try again");
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
            viewClient.printError("Network error. Please, try again");
        }
    }

    @Override
    public void setConnected(boolean connected) {
        isConnected = connected;
        //todo when the server terminates, rmi connections ned to be notified about it
        if(!connected){
            fileLog.debug("Lost connection to the server. Setting isConnected to false");
            viewClient.displayNotification("You lost connection to the server, please try to reconnect");
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

}
