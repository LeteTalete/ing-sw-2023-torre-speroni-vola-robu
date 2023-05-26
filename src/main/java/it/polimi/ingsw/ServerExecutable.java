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
        fileLog.info("Server started");

        try {
            ServerManager serverManager = new ServerManager();
            Registry registry = LocateRegistry.createRegistry(8089);
            registry.rebind("Login", serverManager);
            fileLog.info("Successfully created an Rmi registry");

            //Socket
            MultiServerSocket server = new MultiServerSocket(8899, serverManager);
            server.run();
            fileLog.info("Successfully created a Socket server");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
