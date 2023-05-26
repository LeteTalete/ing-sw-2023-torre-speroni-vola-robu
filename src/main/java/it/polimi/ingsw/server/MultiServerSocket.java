package it.polimi.ingsw.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//this is the principal class of server socket which only instantiate the ServerSocket, execute accept(),
//and create threads to handle accepted connections
public class MultiServerSocket {
    private static Logger fileLog = LogManager.getRootLogger();

    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private int port;
    private ServerManager serverManager;

    public MultiServerSocket(int port, ServerManager serverManager) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        this.port = port;
        this.serverManager = serverManager;
    }

    public void startServer()
    {
        fileLog.info("Socket server started on port "+port);
        while(true)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                fileLog.info("Server accepting from "+clientSocket.getInetAddress());
                pool.submit(new ServerSocketClientHandler(clientSocket,serverManager));
            }
            catch (IOException e)
            {
                fileLog.error(e.getMessage());
                break; //enter here if serverSocket get closed
            }
        }
    }

    public void close() throws IOException{
        serverSocket.close();
        pool.shutdown();
    }
}

