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
    private String token;
    private String connectionType = "SOCKET";

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
    public String getTypeConnection() throws RemoteException {
        return connectionType;
    }

    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException
    {
        respond(new ModelUpdateNotification(updated));
    }

    @Override
    public void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException{
        fileLog.debug("server about to say registration successful: "+name+" "+b+" "+token+" "+first);
        setToken(token);
        respond(new LoginResponse(name, b, token, first));
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
    public void notifyColumnOk(boolean ok) throws RemoteException {
        respond(new ColumnOk(ok));
    }

    @Override
    public void notifyEndTurn() throws RemoteException {
        respond(new EndTurn());
    }


    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        respond(new LastTurn(firstDoneUser));
    }


    @Override
    public void notifyChatMessage(String sender, String message, String receiver) throws RemoteException {
        respond(new ChatMessage(sender, message, receiver));
    }

    @Override
    public void updateModel(ModelUpdate modelUpdate) throws RemoteException {
        respond(new ModelUpdateNotification(modelUpdate));
    }

    @Override
    public void notifyRearrangeOk(boolean ok) throws RemoteException {
        respond(new RearrangeOk(ok));
    }

    @Override
    public void notifyTilesOk(boolean ok) throws RemoteException {
        respond(new TilesOk(ok));
    }

    @Override
    public void notifyGameStart() throws RemoteException {
        respond(new GameStart());
    }

    @Override
    public void notifyStartTurn(String currentPlayer) throws RemoteException {
        respond(new NotifyOnTurn(currentPlayer));
    }

    @Override
    public void notifyEndGame() throws RemoteException {
        respond(new GameEnd());
    }

    @Override
    public void notifyOnCGC(String nickname, int id) throws RemoteException {
        respond(new CommonGoalGained(nickname, id));
    }

    @Override
    public void notifyAboutDisconnection(String disconnectedUser) throws RemoteException {
        respond(new DisconnectionNotif(disconnectedUser));
    }

    @Override
    public void sendPingSyn() throws RemoteException {
        respond(new Pinged());
    }

    @Override
    public String getToken() throws RemoteException {
        return token;
    }

    @Override
    public void setToken(String token) throws RemoteException {
        this.token = token;
    }

    private void respond(Response response) {
        fileLog.debug("server about to send response");
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
