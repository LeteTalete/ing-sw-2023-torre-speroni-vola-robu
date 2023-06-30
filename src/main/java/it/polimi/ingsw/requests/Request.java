package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.io.Serializable;

/**Abstract class Request defines the common methods and attributes of all the requests. */
public interface Request extends Serializable {
    /**
     * handleRequest method is used to handle the request sent by the client.
     * @param socketClientHandler - the socket client handler
     * @param serverManager - the server manager
     * */
    void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager);


}
