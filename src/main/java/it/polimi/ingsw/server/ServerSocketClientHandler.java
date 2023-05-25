package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.requests.Request;
import it.polimi.ingsw.responses.*;

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
               Request request = (Request) in.readObject();
               //request.handleRequest(this, serverManager);
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

//todo fix this switch case
    @Override
    public void sendNotification(Response response) throws RemoteException
    {
        respond(response);
    }

    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException
    {
        //i think this has to serialize the modelupdate
    }

    @Override
    public void notifySuccessfulRegistration(LoginResponse loginResponse) throws RemoteException{
        respond(loginResponse);
    }

    @Override
    public void setClientTurn() {
        //only for clientListener of view
    }

    @Override
    public void setGameOn() throws RemoteException {
        //only for clientListener of view
    }

    @Override
    public void changeTurn(String name) throws RemoteException {
        //only for clientListener of view
    }

    @Override
    public void showTextNotification(String waitingRoomCreated) {
        //only for clientListener of view
    }

    @Override
    public void notifyTilesResponse(GetTilesResponse getTilesResponse) throws RemoteException {
        //only for clientListener of view
    }

    @Override
    public void notifyChooseColumnResponse(ChooseColumnResponse chooseColumnResponse) throws RemoteException {
        //only for clientListener of view
    }

    @Override
    public void notifyMoveOk(MoveOk moveOk) throws RemoteException {

    }

    private void respond(Response response) {
        try{
            out.writeObject(response);
            out.reset();
        } catch (IOException e) {
            if(!stop){
                close();
            }
        }
    }
}
