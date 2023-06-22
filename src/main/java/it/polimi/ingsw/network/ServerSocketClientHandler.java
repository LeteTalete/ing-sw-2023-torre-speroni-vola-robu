package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.requests.Request;
import it.polimi.ingsw.responses.*;
import it.polimi.ingsw.server.ServerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**this class handles the communication with the client associated to the assigned socket. It implements
 * IClientListener so that the server can send replies and notification without having to know the type
 * of connection chosen by the client.*/
public class ServerSocketClientHandler implements Runnable, IClientListener {
    private static final Logger fileLog = LogManager.getRootLogger();
    private final Socket socket;
    private ServerManager serverManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean stop;
    private String token;
    private final String connectionType = "SOCKET";

    /**ServerSocketClientHandler constructor.
     * @param serverManager - to invoke methods of the ServerManager.
     * @param socket - socket of the single client.*/
    public ServerSocketClientHandler(Socket socket, ServerManager serverManager) throws IOException {
        this.socket = socket;
        this.serverManager = serverManager;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.stop = false;
    }

    /**run method is a loop which constantly reads requests and passes them to the handler*/
    public void run() {
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

    /**close method closes the socket and the streams*/
    public void close() {
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

    /**sendUpdateModel method used to generate a notification containing the model update.
     * @param updated - model update.*/
    @Override
    public void sendUpdatedModel(ModelUpdate updated) throws RemoteException {
        respond(new ModelUpdateNotification(updated));
    }

    /**notifySuccessfulRegistration method used to notify a player about the success (or failure) of their
     * login.
     * @param token - token used to identify the client.
     * @param name - username chosen by the client.
     * @param b - boolean signalling whether the registration was successful or not.
     * @param first - boolean signalling whether the client has to create a waiting room or not.*/
    @Override
    public void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException{
        setToken(token);
        respond(new LoginResponse(name, b, token, first));
    }

    @Override
    public void setGameOn() throws RemoteException {
        //only for rmi
    }

    /**method changeTurn used to generate a response to notify the start of a new turn.
     * @param name - name of the current player.*/
    @Override
    public void changeTurn(String name) throws RemoteException {
        respond(new NotifyOnTurn(name));
    }

    /**method showTextNotification used to send a generic text notification.
     *@param text - text of the notification*/
    @Override
    public void showTextNotification(String text) {
        respond(new TextNotification(text));
    }

    /**notifyColumnOk method used to generate a response about the success or failure of the choice of column.
     * @param ok - boolean signalling the success or failure of the move.*/
    @Override
    public void notifyColumnOk(boolean ok) throws RemoteException {
        respond(new ColumnOk(ok));
    }

    /**method used to generate a notification about the end of a turn.*/
    @Override
    public void notifyEndTurn() throws RemoteException {
        respond(new EndTurn());
    }

    /**method notifyLastTurn used to notify the players about the beginning of the last turn.
     * @param firstDoneUser - the username of the player who completed their shelf first.*/
    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        respond(new LastTurn(firstDoneUser));
    }

    /**notifyChatMessage method used to send a message to a player.
     * @param receiver - receiver of the message.
     * @param message - the text of the message.
     * @param sender - the sender of the message.*/
    @Override
    public void notifyChatMessage(String sender, String message, String receiver) throws RemoteException {
        respond(new ChatMessage(sender, message, receiver));
    }

    /**method used to generate a notification about the model update.
     * @param modelUpdate - it contains the model update.*/
    @Override
    public void updateModel(ModelUpdate modelUpdate) throws RemoteException {
        respond(new ModelUpdateNotification(modelUpdate));
    }

    /**method notifyRearrangeOk used to notify the client about the success (or failure) of the re-arrange.
     * @param ok - boolean signalling the success or failure of the client's move.
     * @param tiles - tiles re-arranged so that the client can view them.*/
    @Override
    public void notifyRearrangeOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        respond(new RearrangeOk(ok, tiles));
    }

    /**method notifyTilesOk used to notify the client about the success (or failure) if the choice of tiles.
     * @param ok - boolean signalling the success or failure of the client's move.
     * @param tiles - tiles chosen by the client.*/
    @Override
    public void notifyTilesOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        respond(new TilesOk(ok, tiles));
    }

    /**method used to notify about the start of a game*/
    @Override
    public void notifyGameStart() throws RemoteException {
        respond(new GameStart());
    }

    /**method notifyStartTurn generates a notification about the start of a player's turn.
     * @param currentPlayer - username of the current player*/
    @Override
    public void notifyStartTurn(String currentPlayer) throws RemoteException {
        respond(new NotifyOnTurn(currentPlayer));
    }

    /**method notifyEndGame generates a notification about the end of a game*/
    @Override
    public void notifyEndGame() throws RemoteException {
        respond(new GameEnd());
    }

    /**method notifyOnCGC generates a notification about the gain of a common goal card.
     * @param nickname - username of the player who won the card.
     * @param id - id of the common goal card.*/
    @Override
    public void notifyOnCGC(String nickname, int id) throws RemoteException {
        respond(new CommonGoalGained(nickname, id));
    }

    /**method to notify about a player's disconnection.
     * @param disconnectedUser - username of the player who disconnecteed*/
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
