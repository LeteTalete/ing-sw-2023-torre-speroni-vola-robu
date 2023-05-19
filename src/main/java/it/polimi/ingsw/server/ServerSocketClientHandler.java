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
    private Scanner in;
    private PrintWriter out;

    public ServerSocketClientHandler(Socket socket)
    {
        this.socket = socket;
    }
    public ServerSocketClientHandler(Socket socket, ServerManager serverManager)
    {
        this.socket = socket;
        this.serverManager = serverManager;
    }

    public void openStreams()
    {
        try
        {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void run()
    {
        openStreams();

        try {


            String clientInput = in.nextLine();
            String response = serverManager.login(clientInput, this);
            out.println(response);
            out.flush();

            //closing streams
            closeStreams();

            socket.close();
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void closeStreams()
    {
        this.in.close();
        this.out.close();
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
