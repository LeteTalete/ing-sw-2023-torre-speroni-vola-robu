package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.network.IClientListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;


//this class handles the communication with the client associated to the assigned socket
public class ServerSocketClientHandler implements Runnable, IClientListener
{
    private Socket socket;
    private ServerManager serverManager;

    public ServerSocketClientHandler(Socket socket)
    {
        this.socket = socket;
    }
    public ServerSocketClientHandler(Socket socket, ServerManager serverManager)
    {
        this.socket = socket;
        this.serverManager = serverManager;
    }

    public void run()
    {
        try
        {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String clientInput = in.nextLine();
            //check if the nickname is not already used
            //...
            serverManager.login(clientInput,this);
            out.println("from Server: " + clientInput);
            out.flush();

            //closing streams
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public int askHowMany() throws RemoteException
    {
        return 0;
    }

    @Override
    public void sendNotification(String message) throws RemoteException
    {

    }

    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException {
        //i think this has to serialize the modelupdate
    }
}
