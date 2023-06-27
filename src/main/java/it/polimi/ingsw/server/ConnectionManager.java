package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.timers.CTimer;
import it.polimi.ingsw.timers.ConnectionAckTimer;
import it.polimi.ingsw.timers.ServerSynTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**ConnectionManager is an instance used to keep track of the players, their status (inactive or active)
 * and their listeners (which are made to be reachable from the server so that the server can contact the users).*/

public class ConnectionManager implements Serializable {
    private static ConnectionManager instance;
    /**inactiveUsers is a list containing the tokens of all the inactive users*/
    public List<String> inactiveUsers = new ArrayList<>();
    /**viewListenerMap contains the tokens of the players and the respective listeners (i.e. ClientListenerTUI,
     * ClientListenerGUI, ServerSocketClientHandler)*/
    Map<String, IClientListener> viewListenerMap = new HashMap<>();
    /**tokenNames contains the tokens of the players and the respective usernames*/
    Map<String, String> tokenNames = new HashMap<>();
    /**namesTokens contains the usernames of the players and the respective tokens*/
    Map<String, String> namesTokens = new HashMap<>();
    /**synTimer is a map containing the tokens of the clients and the respective timers to send them a ping*/
    private final Map<String, CTimer> synTimer = new HashMap<>();
    private final int synTime = 1000;
    private final int ackTime = 3000;
    /**ackMap contains the tokens of the clients and the boolean indicating whether an ack has been
     * received from them*/
    private Map<String, Boolean> ackMap = new HashMap<>();
    /**ackCheckTimer is a map containing the tokens of the clients and the respective timers to keep track
     * of the ack received in response to a ping*/
    private final Map<String, CTimer> ackCheckTimer = new HashMap<>();
    /**fileLog is a logger to keep track of the events happening during the game*/
    private static final Logger fileLog = LogManager.getRootLogger();
    private ServerManager master;


    private ConnectionManager(){
    }
    public static synchronized ConnectionManager get(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }

    /**method addClientView used to save a client's token, name and view, so that the server can send them a
     * notification or a response.
     * @param token - token used to identify the client.
     * @param name - username of the client.
     * @param viewListener - listener of the client which is reachable from the network.*/
    synchronized void addClientView(String token, String name, IClientListener viewListener) {
        viewListenerMap.put(token, viewListener);
        tokenNames.put(token, name);
        namesTokens.put(name, token);
    }

    synchronized IClientListener getLocalView(String token){
        return viewListenerMap.get(token);
    }

    /**disconnectToken method used to disconnect a client and notify all the other players about it.
     * @param token - token of the disconnecting player.*/
    public void disconnectToken(String token) {
        fileLog.debug("Disconnecting token " + token + ", username: "+ tokenNames.get(token));
        if(!inactiveUsers.contains(token) && master.getWaitingRoom()==null){
            viewListenerMap.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(token))
                    .forEach(e -> {
                        try {
                            e.getValue().notifyAboutDisconnection(tokenNames.get(token));
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        }
        stopSynTimer(token);
        stopAckTimer(token);
        viewListenerMap.remove(token);
        tokenNames.remove(token);
        master.disconnect(token);
    }

    /**startSynTimer method is used to start the timer to ping the client and see if they are still
     * reachable.
     * @param token - token used to identify the client*/
    synchronized void startSynTimer(String token){
        fileLog.info("Starting syn timer for client: "+tokenNames.get(token));
        synTimer.put(token, new CTimer());
        synTimer.get(token).scheduleAtFixedRate(new ServerSynTimer(viewListenerMap.get(token)), synTime, synTime);
    }

    public void stopSynTimer(String token){
        if(synTimer.containsKey(token)){
            fileLog.info("Stopping syn timer for client: "+tokenNames.get(token));
            synTimer.get(token).purge();
            synTimer.get(token).cancel();
        }
    }

    public void setAck(String token, boolean received) {
        if(ackMap.containsKey(token)){
            fileLog.info("Setting ackmap for client: "+tokenNames.get(token));
            ackMap.replace(token,received);
        }
        else{
            ackMap.put(token,received);
        }
    }

    public Map<String, Boolean> getAckMap() {
        return ackMap;
    }

    /**startAckTimer method starts a timer to receive an ack from the client as a response to the ping.
     * @param token - token used to identify the client related to the timer*/
    void startAckTimer(String token){
        fileLog.info("Starting ack timer for client: "+tokenNames.get(token));
        ackCheckTimer.put(token, new CTimer());
        ackMap.put(token, true);
        ackCheckTimer.get(token).scheduleAtFixedRate(new ConnectionAckTimer(viewListenerMap.get(token)), ackTime, ackTime);
    }

    /**stopAckTimer method clears the ack timer.
     * @param token - to identify the client related to the timer*/
    public void stopAckTimer(String token){
        if(ackCheckTimer.containsKey(token)){
            fileLog.info("Stopping timer for client: "+tokenNames.get(token));
            ackCheckTimer.get(token).purge();
            ackCheckTimer.get(token).cancel();
        }
    }

    public void setMaster(ServerManager serverManager) {
        this.master = serverManager;
    }
}

