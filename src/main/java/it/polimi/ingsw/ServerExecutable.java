package it.polimi.ingsw;

import it.polimi.ingsw.server.MultiServerSocket;
import it.polimi.ingsw.server.ServerManager;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerExecutable {

    private static Logger fileLog = LogManager.getRootLogger();
    public static void main(String[] args) {
        fileLog.debug("Server started");
        fileLog.info("Server started");
        fileLog.warn("Server started");
        fileLog.error("Server started");
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
