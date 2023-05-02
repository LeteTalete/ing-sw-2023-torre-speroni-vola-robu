package it.polimi.ingsw.requests;

import it.polimi.ingsw.client.ClientHandler;
import it.polimi.ingsw.server.ServerManager;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private final String uID;
    public Request() {uID = null;}
    public Request(String demand){
        this.uID = demand;
    }

    public abstract void handleRequest(ClientHandler clientHandler, ServerManager serverManager);

    public String getUID(){
        return uID;
    }

}
