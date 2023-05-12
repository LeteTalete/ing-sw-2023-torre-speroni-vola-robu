package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocket
{
    private String ip;
    private int port;


    public ClientSocket(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }


    public static void main(String[] args)
    {
        ClientSocket client = new ClientSocket("127.0.0.1",1420);
        try
        {
            client.startClient();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() throws IOException
    {
        //open a socket by specifying server's ip address and listening port
        Socket socket = new Socket(ip,port);
        System.out.println("Connection established");

        //if the connection is successful i can use socket's streams to communicate with the server
        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());


        Scanner stdin = new Scanner(System.in);
        try
        {
            String debug = "q";
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
}
