package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.requests.Request;
import it.polimi.ingsw.responses.LoginResponse;
import it.polimi.ingsw.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;


//this class handles the communication with the client associated to the assigned socket
public class ServerSocketClientHandler implements Runnable, IClientListener
{
    private Socket socket;
    private ServerManager serverManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean stop;

    public ServerSocketClientHandler(Socket socket)
    {
        this.socket = socket;
    }
    public ServerSocketClientHandler(Socket socket, ServerManager serverManager) throws IOException {
        this.socket = socket;
        this.serverManager = serverManager;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }


    public void run()
    {
       do{
           try{
               System.out.println("i'm reading!");
               Request request = (Request) in.readObject();
               request.handleRequest(this, serverManager);
           } catch (ClassNotFoundException | IOException e) {
               String token = serverManager.getTokenFromHandler(this);
               serverManager.disconnect(token);
               close();
           }
       }while(!stop);
    }

    public void close()
    {
        stop = true;
        if(out != null){
            try{
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(in!=null){
            try{
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String sendNotification(String message) throws RemoteException
    {
        //respond(new TextMessage(message));
        return message;
    }

    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException
    {
        //i think this has to serialize the modelupdate
    }

    @Override
    public String notifySuccessfulRegistration(String name, boolean nameExistsAlready, String token, boolean first) throws RemoteException{
        respond(new LoginResponse(name, nameExistsAlready, token, first));
        return null;
    }

    private void respond(Response response) {
        try{
            System.out.println("i'm about to reply");
            out.writeObject(response);
            out.reset();
        } catch (IOException e) {
            if(!stop){
                close();
            }
        }
    }
}
