package it.polimi.ingsw.server;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.network.ConnectionServerTimer;
import it.polimi.ingsw.network.ConnectionTimer;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.ConnectionServerPingTimer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManager implements Serializable {
    private static ConnectionManager instance;
    public List<String> inactiveUsers = new ArrayList<>();
    //we create two maps to keep track of the active clients and their Listeners
    //it's important for these to be maps, so that the search is made easier with the usage of
    //a client's username as a key. The servercontroller will have a map with clients' username as keys and the
    //number of their "room" (i.e. the game controller for their game) as values

    //tokens and listeners
    Map<String, IClientListener> viewListenerMap = new HashMap<>();

    //tokens and usernames
    Map<String, String> tokenNames = new HashMap<>();
    //usernames and tokens
    Map<String, String> namesTokens = new HashMap<>();
    //tokens and timers
    private final Map<String, ConnectionTimer> timers = new HashMap<>();
    private final int synTime=100000;
    private final int ackTime = 200000;
    private Map<String, Boolean> pingCheck = new HashMap<>();
    private final Map<String, ConnectionTimer> synTimers = new HashMap<>();

    private ConnectionManager(){
    }
    public static synchronized ConnectionManager get(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }

    synchronized String addClientView(String token, String name, IClientListener viewListener) {
        viewListenerMap.put(token, viewListener);
        tokenNames.put(token, name);
        namesTokens.put(name, token);
        return StaticStrings.LOGIN_OK_NEW_ROOM;
    }

    synchronized IClientListener getLocalView(String token){
        return viewListenerMap.get(token);
    }

    synchronized String getNameToken(String token){
        return tokenNames.get(token);
    }

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

    void startPingTimer(String token){
        timers.put(token, new ConnectionTimer());
        timers.get(token).scheduleAtFixedRate(new ConnectionServerPingTimer(viewListenerMap.get(token)), ackTime, ackTime);
    }

    void startSynTimer(String token){
        synTimers.put(token, new ConnectionTimer());
        synTimers.get(token).scheduleAtFixedRate(new ConnectionServerTimer(viewListenerMap.get(token)), synTime, synTime);
    }

    public Map<String, Boolean> getPingMap() {
        return pingCheck;
    }

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
