package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.io.Serializable;

/**Abstract class Request defines the common methods and attributes of all the requests. */
public interface Request extends Serializable {
    void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager);


}
