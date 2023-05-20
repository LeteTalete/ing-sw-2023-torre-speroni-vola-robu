package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class loginRequest extends Request {
    //public is set because the attribute needs to be viewable to whoever receives the request
    //final so that the attribute is only set once and never changed again
    public final String username;
    public loginRequest(String name) {
        this.username = name;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager)
    {
        try{
            serverManager.login(username, socketClientHandler);
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }
    }


}
