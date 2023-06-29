package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;

/**request from a client to log in*/

public class LoginRequest implements Request {

    public final String username;
    public LoginRequest(String name) {
        this.username = name;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try{
            serverManager.login(username, socketClientHandler);
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }
}
