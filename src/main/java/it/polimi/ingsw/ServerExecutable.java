package it.polimi.ingsw;

import it.polimi.ingsw.server.MultiServerSocket;
import it.polimi.ingsw.server.ServerManager;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerExecutable {
    public static void main(String[] args) {
        try {
            ServerManager serverManager = new ServerManager();
            Registry registry = LocateRegistry.createRegistry(8089);
            registry.rebind("Login", serverManager);

            //Socket
            MultiServerSocket server = new MultiServerSocket(1420, serverManager);
            server.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
