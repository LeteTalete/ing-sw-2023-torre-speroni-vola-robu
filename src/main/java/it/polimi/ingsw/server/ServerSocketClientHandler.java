package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.requests.Request;
import it.polimi.ingsw.responses.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;


//this class handles the communication with the client associated to the assigned socket
public class ServerSocketClientHandler implements Runnable, IClientListener
{
    private static Logger fileLog = LogManager.getRootLogger();
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
        this.stop = false;
    }


    public void run()
    {
       do{
           try{
               Request request = (Request) in.readObject();
               request.handleRequest(this, serverManager);
           } catch (ClassNotFoundException | IOException e) {
               fileLog.error(e.getMessage());
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
                fileLog.error(e.getMessage());
            }
        }
        if(in!=null){
            try{
                in.close();
            } catch (IOException e) {
                fileLog.error(e.getMessage());
            }
        }

        try{
            socket.close();
        } catch (IOException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void sendNotification(Response response) throws RemoteException
    {
        respond(response);
    }

    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException
    {
        respond(new ModelUpdateNotification(updated));
    }

    @Override
    public void notifySuccessfulRegistration(LoginResponse loginResponse) throws RemoteException{
        respond(loginResponse);
    }

    @Override
    public void setGameOn() throws RemoteException {
        //only for rmi?
    }

    @Override
    public void changeTurn(String name) throws RemoteException {
        respond(new NotifyOnTurn(name));
    }

    @Override
    public void showTextNotification(String waitingRoomCreated) {
        respond(new TextNotification(waitingRoomCreated));
    }



    @Override
    public void notifyColumnOk(ColumnOk moveOk) throws RemoteException {
        respond(moveOk);
    }

    @Override
    public void notifyEndTurn(EndTurn endTurn) throws RemoteException {
        respond(endTurn);
    }

    @Override
    public void notifyGameEnd(GameEnd gameEnd) throws RemoteException {
        respond(gameEnd);
    }

    @Override
    public void notifyLastTurn(LastTurn lastTurn) throws RemoteException {
        respond(lastTurn);
    }

    @Override
    public void notifyCommonGoalGained(CommonGoalGained commonGoalGained) throws RemoteException {
        respond(commonGoalGained);
    }

    @Override
    public void notifyChatMessage(ChatMessage chatMessage) throws RemoteException {
        respond(chatMessage);
    }

    @Override
    public void updateModel(ModelUpdateNotification modelUpdateNotification) throws RemoteException {
        //idk
    }

    @Override
    public void notifyRearrangeOk(RearrangeOk rearrangeOk) throws RemoteException {
        respond(rearrangeOk);
    }

    @Override
    public void notifyTilesOk(TilesOk tilesOk) throws RemoteException {
        respond(tilesOk);
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
