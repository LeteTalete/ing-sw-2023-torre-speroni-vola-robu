package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IClientListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager implements Serializable {
    private static ConnectionManager instance;
    //we create two maps to keep track of the active clients and their Listeners
    //it's important for these to be maps, so that the search is made easier with the usage of
    //a client's username as a key. The servercontroller will have a map with clients' username as keys and the
    //number of their "room" (i.e. the game controller for their game) as values

    /**instead of viewListener we need a generic ConnectionType that allows me to send something to the client without
    the need for the need for the server to know what kind of connection the single clients are using**/

    Map<String, IClientListener> viewListenerMap = new HashMap<>();
    Map<String, String> tokenNames = new HashMap<>();
    Map<String, String> namesTokens = new HashMap<>();
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
        return StaticStrings.LOGIN_OK_NEW_ROOM;
    }

    synchronized IClientListener getLocalView(String token){
        return viewListenerMap.get(token);
    }

    synchronized String getNameToken(String token){
        return tokenNames.get(token);
    }
}
