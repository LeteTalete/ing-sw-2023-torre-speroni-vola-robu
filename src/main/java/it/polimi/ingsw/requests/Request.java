package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.io.Serializable;

/**Abstract class Request defines the common methods and attributes of all the requests. */
public abstract class Request implements Serializable {
    public abstract void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager);


}
