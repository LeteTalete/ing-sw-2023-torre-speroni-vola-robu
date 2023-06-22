package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.io.Serializable;

/**Abstract class Request defines the common methods and attributes of all the requests. */
public abstract class Request implements Serializable {
    private final String uID;
    public Request() {uID = null;}
    public Request(String demand){
        this.uID = demand;
    }

    public abstract void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager);

    //todo cleanup???? do we need this?
    public String getUID(){
        return uID;
    }

}
