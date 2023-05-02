package it.polimi.ingsw.requests;

import it.polimi.ingsw.client.ClientHandler;
import it.polimi.ingsw.server.ServerManager;

public class Ping extends Request{
    public Ping(String uID){
        super(uID);
    }
    @Override
    public void handleRequest(ClientHandler clientHandler, ServerManager serverManager){
    }
}
