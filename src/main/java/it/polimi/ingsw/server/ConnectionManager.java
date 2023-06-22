package it.polimi.ingsw.server;

import it.polimi.ingsw.network.ConnectionServerPingTimer;
import it.polimi.ingsw.network.ConnectionServerTimer;
import it.polimi.ingsw.network.ConnectionTimer;
import it.polimi.ingsw.network.IClientListener;

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
    //tokens and timers
    private final Map<String, ConnectionTimer> timers = new HashMap<>();
    private final int ackTime = 200000;
    private final Map<String, Boolean> pingCheck = new HashMap<>();
    private final Map<String, ConnectionTimer> synTimers = new HashMap<>();

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

    /**method used to start the timer of the ping. Each time the timer resets, a ping is sent.
     * @param token - token of the timer's player*/
    void startPingTimer(String token){
        timers.put(token, new ConnectionTimer());
        timers.get(token).scheduleAtFixedRate(new ConnectionServerPingTimer(viewListenerMap.get(token)), ackTime, ackTime);
    }

    /**method used to start the timer of the single player. Each time the server receives a ping from the player,
     * their timer resets.
     * @param token - token of the timer's player*/
    void startSynTimer(String token){
        synTimers.put(token, new ConnectionTimer());
        int synTime = 100000;
        synTimers.get(token).scheduleAtFixedRate(new ConnectionServerTimer(viewListenerMap.get(token)), synTime, synTime);
    }

    public Map<String, Boolean> getPingMap() {
        return pingCheck;
    }

    /**this method is used to keep track of the players whose ping has been received by the server*/
    public void setPingMap(String token, boolean received) {
        if(pingCheck.containsKey(token)){
            pingCheck.replace(token, received);
        }
        else{
            pingCheck.put(token, received);
        }
    }

    public void stopPingTimer(String token) {
        if(timers.containsKey(token)){
            timers.get(token).cancel();
            timers.remove(token);
        }
    }
}
