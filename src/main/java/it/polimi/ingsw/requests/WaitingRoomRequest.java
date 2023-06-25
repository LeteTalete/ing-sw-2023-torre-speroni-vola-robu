package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.network.ServerSocketClientHandler;

import java.rmi.RemoteException;

/**request from the client to create a waiting room*/

public class WaitingRoomRequest extends Request{
    public final String token;
    public final String name;
    public final int num;

    /**WaitingRoomRequest constructor.
     * @param username - name of the client.
     * @param number - number of players expected in the next match.
     * @param tokenGot - token to identify the client.*/
    public WaitingRoomRequest(String tokenGot, String username, int number) {
        this.token = tokenGot;
        this.name = username;
        this.num = number;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        try {
            serverManager.setPlayersWaitingRoom(token, num);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
