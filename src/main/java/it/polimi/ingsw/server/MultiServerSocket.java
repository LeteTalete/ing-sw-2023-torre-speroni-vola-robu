package it.polimi.ingsw.server;

import it.polimi.ingsw.network.ServerSocketClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**this is the main class of the server socket, which only instantiates the ServerSocket, executes
 * accepts() method and creates thread to handle multiple accepted connections*/

public class MultiServerSocket {
    /**fileLog is a logger to keep track of the events happening during the game*/
    private static final Logger fileLog = LogManager.getRootLogger();
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private final int port;
    private final ServerManager serverManager;

    /**MultiServerSocket constructor, it creates a ServerSocket and a thread pool.
     * @param port - port of the server.
     * @param serverManager - to invoke the serverManager's methods*/
    public MultiServerSocket(int port, ServerManager serverManager) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        this.port = port;
        this.serverManager = serverManager;
    }

    /**run method to start a thread and accept from a client.*/
    public void run() {
        fileLog.info("Socket server started on port "+port);
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                fileLog.info("Server accepting from "+clientSocket.getInetAddress());
                pool.submit(new ServerSocketClientHandler(clientSocket,serverManager));
            }
            catch (IOException e) {
                fileLog.error(e.getMessage());
                break; //enter here if serverSocket get closed
            }
        }
    }

    /**close method to close the serverSocket and the thread pool.*/
    public void close() throws IOException{
        serverSocket.close();
        pool.shutdown();
    }
}

