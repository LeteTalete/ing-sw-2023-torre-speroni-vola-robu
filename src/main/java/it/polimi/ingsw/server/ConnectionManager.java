package it.polimi.ingsw.server;

import it.polimi.ingsw.network.ClientTimer;
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

}
