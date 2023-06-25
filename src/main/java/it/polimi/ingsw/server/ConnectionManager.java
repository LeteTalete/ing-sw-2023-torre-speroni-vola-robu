package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.timers.CTimer;
import it.polimi.ingsw.timers.ConnectionAckTimer;
import it.polimi.ingsw.timers.ConnectionServerTimer;
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
    public List<String> inactiveUsers = new ArrayList<>();
    //tokens and listeners
    Map<String, IClientListener> viewListenerMap = new HashMap<>();
    //tokens and usernames
    Map<String, String> tokenNames = new HashMap<>();
    //usernames and tokens
    Map<String, String> namesTokens = new HashMap<>();
    private final Map<String, CTimer> synTimer = new HashMap<>();
    private final int synTime = 15000;
    private final int ackTime = 30000;
    private Map<String, Boolean> ackMap = new HashMap<>();
    private final Map<String, CTimer> ackCheckTimer = new HashMap<>();
    private static final Logger fileLog = LogManager.getRootLogger();


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
    //todo check whether this needs the id of the game, if we want to implement multiple matches
    public void disconnectToken(String token) {
        fileLog.debug("Disconnecting token " + token + ", username: "+ tokenNames.get(token));
        if(!inactiveUsers.contains(token)){
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
        viewListenerMap.remove(token);
        tokenNames.remove(token);
    }

    /**startSynTimer method is used to start the timer to ping the client and see if they are still
     * reachable.
     * @param token - token used to identify the client*/
    synchronized void startSynTimer(String token){
        fileLog.debug("Starting syn timer for client: "+tokenNames.get(token));
        synTimer.put(token, new CTimer());
        synTimer.get(token).scheduleAtFixedRate(new ConnectionServerTimer(viewListenerMap.get(token)), synTime, synTime);
    }

    public void stopSynTimer(String token){
        if(synTimer.containsKey(token)){
            fileLog.debug("Stopping syn timer for client: "+tokenNames.get(token));
            synTimer.get(token).purge();
            synTimer.get(token).cancel();
        }
    }

    public void setAck(String token, boolean received) {
        if(ackMap.containsKey(token)){
            fileLog.debug("Setting ackmap for client: "+tokenNames.get(token));
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
        fileLog.debug("Starting ack timer for client: "+tokenNames.get(token));
        ackCheckTimer.put(token, new CTimer());
        ackMap.put(token, true);
        ackCheckTimer.get(token).scheduleAtFixedRate(new ConnectionAckTimer(viewListenerMap.get(token)), ackTime, ackTime);
    }

    /**stopAckTimer method clears the ack timer.
     * @param token - to identify the client related to the timer*/
    public void stopAckTimer(String token){
        if(ackCheckTimer.containsKey(token)){
            fileLog.debug("Stopping timer for client: "+tokenNames.get(token));
            ackCheckTimer.get(token).purge();
            ackCheckTimer.get(token).cancel();
        }
    }

}

