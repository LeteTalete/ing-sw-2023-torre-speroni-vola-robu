package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

/**request from the client to quit the game*/

public class QuitRequest implements Request {
    private final String token;

    public QuitRequest(String userToken) {
        token = userToken;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try{
            serverManager.disconnect(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
