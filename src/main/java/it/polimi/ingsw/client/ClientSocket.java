package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocket implements IClientConnection
{
    private String ip;
    private int port;
    private View viewClient;


    public ClientSocket(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }


    public void startClient() throws IOException
    {
        //open a socket by specifying server's ip address and listening port
        Socket socket = new Socket(ip,port);
        System.out.println(">> Connection established");

        //if the connection is successful i can use socket's streams to communicate with the server
        System.out.println(">> Insert username");
        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());


        Scanner stdin = new Scanner(System.in);
        try
        {
            String input = stdin.nextLine();
            String success = null;
            do
            {
                //success = login(input);

                //sending nickname
                socketOut.println(input);
                socketOut.flush();

                //receiving response
                success = socketIn.nextLine();
                System.out.println(success);
            }
            while(!success.equals("success"));

            //------------
            String debug = "quit";
            while(!debug.equals("quit"))
            {
                //sending something to the server
                debug = stdin.nextLine(); //get user input
                socketOut.println(debug);
                socketOut.flush(); //always remember to flush!

                //receiving something from the server
                String socketLine = socketIn.nextLine();
                System.out.println(socketLine);
            }
            //----------
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Connection closed");
        }
        finally
        {
            //closing streams
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

    @Override
    public void setViewClient(View currentView)
    {
        this.viewClient = currentView;
    }

    @Override
    public String login(String name)
    {
        String success = null;

        //DEBUG
        System.out.println("DEBUG: effettuo il login...");

        return success;
    }
}
