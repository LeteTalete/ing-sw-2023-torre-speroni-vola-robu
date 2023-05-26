package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private final String uID;
    public Request() {uID = null;}
    public Request(String demand){
        this.uID = demand;
    }

    public abstract void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager);

    public String getUID(){
        return uID;
    }

}
