package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocket implements IClientConnection
{
    private String name;

    private String ip;
    private int port;
    private View viewClient;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Scanner stdin;


    public ClientSocket(String ip, int port)
    {
        //you have to save the client controller as 'master' and call the login method using
        //'master.userLogin()
        this.ip = ip;
        this.port = port;
    }

    public void openStreams()
    {
        try
        {
            this.socketIn = new Scanner(socket.getInputStream());
            this.socketOut = new PrintWriter(socket.getOutputStream());
            this.stdin = new Scanner(System.in);
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() throws IOException
    {
        //open a socket by specifying server's ip address and listening port
        Socket socket = new Socket(ip,port);
        System.out.println(">> Connection established");
        this.socket = socket;

        //if the connection is successful i can use socket's streams to communicate with the server
        openStreams();

        System.out.println(">> Insert username");
        try
        {
            String input = stdin.nextLine();
            String success = null;

            success = login(input);
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Connection closed");
        }
        finally
        {
            closeStreams();
            socket.close();
        }
    }

    public void closeStreams()
    {
        //closing streams
        stdin.close();
        socketIn.close();
        socketOut.close();
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void setViewClient(View currentView)
    {
        this.viewClient = currentView;
    }

    @Override
    public void chooseTiles(String name, String tilesChosen) {

    }

    @Override
    public String login(String name)
    {
        String success = null;

        //sending nickname
        socketOut.println(name);
        socketOut.flush();

        //receiving response
        success = socketIn.nextLine();
        System.out.println(success);

        return success;
    }
}
