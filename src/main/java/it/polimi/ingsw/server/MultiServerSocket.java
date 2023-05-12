package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//this is the principal class of server socket which only instantiate the ServerSocket, execute accept(),
//and create threads to handle accepted connections
public class MultiServerSocket
{
    private int port;

    public MultiServerSocket(int port)
    {
        this.port = port;
    }

    public void startServer()
    {
        //newCachedThreadPool() is used for creating threads only when it is necessary (reuse already existing ones if it is possible)
        ExecutorService executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Server ready");
        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                executor.submit(new ServerSocketClientHandler(socket));
            }
            catch (IOException e)
            {
                break; //enter here if serverSocket get closed
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args)
    {
        MultiServerSocket server = new MultiServerSocket(1420);
        server.startServer();
    }
}

