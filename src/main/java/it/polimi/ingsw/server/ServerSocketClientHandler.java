package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


//this class handles the communication with the client associated to the assigned socket
public class ServerSocketClientHandler implements Runnable
{
    private Socket socket;

    public ServerSocketClientHandler(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            //leggo e scrivo nella connessione finche non ricevo "quit" (DEBUG)
            String debug = "q";
            while(!debug.equals("quit"))
            {
                String line = in.nextLine();
                System.out.println("Client sent: " + line);
                if(line.equals("quit")) break;
                else
                {
                    out.println("from Server: " + line);
                    out.flush();
                }
            }

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
}
