package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IListener;
import it.polimi.ingsw.network.LocalView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager implements Serializable {
    private static ConnectionManager instance;
    //we create two maps to keep track of the active clients and their Listeners
    //it's important for these to be maps, so that the search is made easier with the usage of
    //a client's username as a key. The servercontroller will have a map with clients' username as keys and the
    //number of their "room" (i.e. the game controller for their game) as values
    Map<String, IListener> viewListenerMap = new HashMap<>();
    private Map<String, LocalView> clientLocals = new HashMap<>();
    private ConnectionManager(){

    }
    public static synchronized ConnectionManager get(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }

    synchronized String addClientView(String name, IListener viewListener) {
        if(viewListenerMap.get(name)==null)
        {
            viewListenerMap.put(name, viewListener);
            clientLocals.put(name, new LocalView(viewListener, name));
            return StaticStrings.LOGIN_OK_NEW_ROOM;
        }
        return StaticStrings.LOGIN_KO;
    }
    synchronized LocalView getLocalView(String username){
        return clientLocals.get(username);
    }
}
