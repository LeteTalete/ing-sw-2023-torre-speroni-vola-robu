package it.polimi.ingsw.requests;

import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.ServerSocketClientHandler;

import java.rmi.RemoteException;

public class WaitingRoomRequest extends Request{
    public final String token;
    public final String name;
    public final int num;

    public WaitingRoomRequest(String tokenGot, String username, int number) {
        this.token = tokenGot;
        this.name = username;
        this.num = number;
    }

    @Override
    public void handleRequest(ServerSocketClientHandler socketClientHandler, ServerManager serverManager) {
        System.out.println("I created waiting room for " + name);
        serverManager.createWaitingRoom(name, token, num);
    }
}
